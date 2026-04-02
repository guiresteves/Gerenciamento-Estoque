package com.br.stockpro.mapper;

import com.br.stockpro.dtos.product.ProductCreateRequest;
import com.br.stockpro.dtos.product.ProductResponse;
import com.br.stockpro.dtos.product.ProductUpdateRequest;
import com.br.stockpro.model.Category;
import com.br.stockpro.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "active", constant = "true")
    Product toEntity(ProductCreateRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    void updateEntityFromDTO(ProductUpdateRequest dto, @MappingTarget Product entity);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyLegalName", source = "company.legalName")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toResponse(Product product);

    default Category mapCategory(Long categoryId) {
        if (categoryId == null) return null;
        return Category.builder()
                .id(categoryId)
                .build();
    }
}