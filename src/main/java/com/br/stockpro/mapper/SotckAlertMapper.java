package com.br.stockpro.mapper;


import com.br.stockpro.dtos.stockAlert.StockAlertResponse;
import com.br.stockpro.model.StockAlert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SotckAlertMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productBarcode", source = "product.barcode")
    @Mapping(target = "stockId", source = "stock.id")
    StockAlertResponse toResponse(StockAlert stockAlert);
}
