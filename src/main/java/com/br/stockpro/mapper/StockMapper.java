package com.br.stockpro.mapper;

import com.br.stockpro.dtos.stock.StockCreateRequest;
import com.br.stockpro.dtos.stock.StockResponse;
import com.br.stockpro.dtos.stock.StockUpdateRequest;
import com.br.stockpro.model.Product;
import com.br.stockpro.model.Stock;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "reservedQuantity", constant = "0")
    Stock toEntity(StockCreateRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "reservedQuantity", ignore = true)
    void updateEntityFromDTO(StockUpdateRequest dto, @MappingTarget Stock entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productBarcode", source = "product.barcode")
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "availableQuantity", expression = "java(stock.getAvailableQuantity())")
    @Mapping(target = "belowMinimum", expression = "java(stock.isBelowMinimum())")
    @Mapping(target = "aboveMaximum", expression = "java(stock.isAboveMaximum())")
    @Mapping(target = "unitOfMeasure", source = "product.unitOfMeasure")
    @Mapping(target = "minStock", source = "product.minStock")       // novo
    @Mapping(target = "maxStock", source = "product.maxStock")
    StockResponse toResponse(Stock stock);
}
