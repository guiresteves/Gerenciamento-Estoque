package com.br.stockpro.mapper;

import com.br.stockpro.dtos.auth.AuthResponse;
import com.br.stockpro.dtos.auth.RegisterRequest;
import com.br.stockpro.dtos.user.UserCrreateRequest;
import com.br.stockpro.dtos.user.UserResponse;
import com.br.stockpro.dtos.user.UserUpdateRequest;
import com.br.stockpro.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "company", ignore = true)
    User toEntity(RegisterRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "company", ignore = true)
    User toEntity(UserCrreateRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntity(UserUpdateRequest dto, @MappingTarget User user);

    @Mapping(target = "companyId", source = "company.id")
    UserResponse toResponse(User user);
}
