package com.br.stockpro.service;

import com.br.stockpro.dtos.user.ChangePasswordRequest;
import com.br.stockpro.dtos.user.UserCrreateRequest;
import com.br.stockpro.dtos.user.UserResponse;
import com.br.stockpro.dtos.user.UserUpdateRequest;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.exceptions.NotFoundException;
import com.br.stockpro.mapper.UserMapper;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.User;
import com.br.stockpro.repository.UserRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public UserResponse createUser(UserCrreateRequest request) {

        Company company = getCurrentUserCompany();

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCompany(company);
        user.setActive(true);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers(Boolean active) {
        Company company = getCurrentUserCompany();

        List<User> users = (active != null)
                ? userRepository.findAllByCompanyIdAndActive(company.getId(), active)
                : userRepository.findAllByCompanyId(company.getId());

        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(Long id) {
        Company company = getCurrentUserCompany();
        return userMapper.toResponse(getUserOrThrow(id, company.getId()));
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        Company company = getCurrentUserCompany();
        User user = getUserOrThrow(id, company.getId());

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessException("Não é possível alterar um usuário inativo");
        }

        userMapper.updateEntity(request, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User currentUser = authenticatedUserService.getCurrentUser();

        if (!passwordEncoder.matches(request.currentPassword(), currentUser.getPassword())) {
            throw new BusinessException("Senha atual incorreta");
        }

        currentUser.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(currentUser);
    }

    @Transactional
    public UserResponse activate(Long id) {
        Company company = getCurrentUserCompany();
        User user = getUserOrThrow(id, company.getId());

        if (Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessException("Usuário já está ativo");
        }

        user.setActive(true);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse deactivate(Long id) {
        Company company = getCurrentUserCompany();
        User user = getUserOrThrow(id, company.getId());

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessException("Usuário já está inativo");
        }

        user.setActive(false);
        return userMapper.toResponse(userRepository.save(user));
    }

    private Company getCurrentUserCompany() {
        User currentUser = authenticatedUserService.getCurrentUser();
        if (currentUser.getCompany() == null) {
            throw new BusinessException("Usuário não possui empresa vinculada");
        }
        return currentUser.getCompany();
    }

    private User getUserOrThrow(Long id, Long companyId) {
        return userRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }
}
