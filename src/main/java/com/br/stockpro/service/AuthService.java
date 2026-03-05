package com.br.stockpro.service;

import com.br.stockpro.dtos.auth.AuthResponse;
import com.br.stockpro.dtos.auth.LoginRequest;
import com.br.stockpro.dtos.auth.RegisterRequest;
import com.br.stockpro.enums.Role;
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
            throw  new RuntimeException("Email já cadastrado");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(true);
        user.setRole(Role.ADMIN);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Credenciais Inválidas"));

        if (!user.getActive()){
            throw  new RuntimeException("Usuario Inatívo");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new RuntimeException("Crendicial Inválida");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
