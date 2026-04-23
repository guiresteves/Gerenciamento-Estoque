package com.br.stockpro.mapper;


import com.br.stockpro.dtos.alertStock.AlertStockResponse;
import com.br.stockpro.model.AlertStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlertSotckMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productBarcode", source = "product.barcode")
    @Mapping(target = "stockId", source = "stock.id")
    AlertStockResponse toResponse(AlertStock alertStock);
}
