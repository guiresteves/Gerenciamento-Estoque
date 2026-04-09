package com.br.stockpro.mapper;

import com.br.stockpro.dtos.stock.StockCreateRequest;
import com.br.stockpro.dtos.stock.StockResponse;
import com.br.stockpro.dtos.stock.StockUpdateRequest;
import com.br.stockpro.model.Category;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.Product;
import com.br.stockpro.model.Stock;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "product", source = "productId")
    @Mapping(target = "active", constant = "true")
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
    StockResponse toResponse(Stock stock);

    default Product mapProduct(Long productId) {
        if (productId == null) return null;

        return Product.builder()
                .id(productId)
                .build();
    }
}
