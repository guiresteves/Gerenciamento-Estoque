package com.br.stockpro.mapper;

import com.br.stockpro.dtos.company.CompanyCreateRequest;
import com.br.stockpro.dtos.company.CompanyResponse;
import com.br.stockpro.dtos.company.CompanyUpdateRequest;
import com.br.stockpro.model.Company;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Company toEntity(CompanyCreateRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "cnpj", ignore = true)
    void updateEntityFromDTO(
            CompanyUpdateRequest dto,
            @MappingTarget Company entity
    );

    CompanyResponse toResponse(Company company);
}
