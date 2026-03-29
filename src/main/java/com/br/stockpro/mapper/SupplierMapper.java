package com.br.stockpro.mapper;

import com.br.stockpro.dtos.supplier.SupplierCreateRequest;
import com.br.stockpro.dtos.supplier.SupplierResponse;
import com.br.stockpro.dtos.supplier.SupplierUpdateRequest;
import com.br.stockpro.model.Supplier;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "active", ignore = true)
    Supplier toEntity(SupplierCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    void updateEntityFromDTO(
            SupplierUpdateRequest request,
            @MappingTarget Supplier target
    );

    SupplierResponse toResponse(Supplier supplier);
}
