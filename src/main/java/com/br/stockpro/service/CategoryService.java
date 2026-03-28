package com.br.stockpro.service;

import com.br.stockpro.mapper.CategoryMapper;
import com.br.stockpro.repository.CategoryRepository;
import com.br.stockpro.repository.UserRepository;
import com.br.stockpro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository userRepository;
}
