package com.br.stockpro.mapper;

import com.br.stockpro.dtos.productBatch.BatchAlertResponse;
import com.br.stockpro.dtos.productBatch.ProductBatchResponse;
import com.br.stockpro.model.ProductBatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductBatchMapper {

    @Mapping(target = "companyId", source = "company.Id")
    @Mapping(target = "productId", source = "product.Id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productBarCode", source = "product.barcode")
    @Mapping(target = "stockId", source = "stock.Id")
    @Mapping(target = "stockMovementId", source = "stockMovement.id")
    ProductBatchResponse toResponse(ProductBatch productBatch);


    @Mapping(target = "batchId", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productBarcode", source = "product.barcode")
    BatchAlertResponse toAlertResponse(ProductBatch productBatch);
}
