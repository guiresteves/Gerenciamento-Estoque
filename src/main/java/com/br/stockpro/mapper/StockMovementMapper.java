package com.br.stockpro.mapper;

import com.br.stockpro.dtos.stockMovement.StockMovementResponse;
import com.br.stockpro.model.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMovementMapper {

    @Mapping(target = "stockId", source = "stock.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productBarCode", source = "product.barcode")
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "performedByName", source = "performedBy.name")
    StockMovementResponse toResponse(StockMovement stockMovement);
}
