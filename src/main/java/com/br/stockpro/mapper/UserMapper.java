package com.br.stockpro.mapper;

import com.br.stockpro.dtos.auth.AuthResponse;
import com.br.stockpro.dtos.auth.RegisterRequest;
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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget User user, RegisterRequest dto);
}
