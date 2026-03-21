package com.br.stockpro.service;

import com.br.stockpro.dtos.auth.AuthResponse;
import com.br.stockpro.dtos.auth.LoginRequest;
import com.br.stockpro.dtos.auth.RegisterRequest;
import com.br.stockpro.enums.Role;
import com.br.stockpro.exceptions.BusinessException;
import com.br.stockpro.mapper.UserMapper;
import com.br.stockpro.model.User;
import com.br.stockpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(true);
        user.setRole(Role.ADMIN);

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        if (!user.getActive()) {
            throw new BusinessException("Usuário inativo");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}