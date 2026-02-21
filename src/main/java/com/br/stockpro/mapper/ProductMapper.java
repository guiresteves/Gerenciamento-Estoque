package com.br.stockpro.mapper;

import com.br.stockpro.dtos.product.ProductCreateRequest;
import com.br.stockpro.dtos.product.ProductResponse;
import com.br.stockpro.dtos.product.ProductUpdatedRequest;
import com.br.stockpro.model.Category;
import com.br.stockpro.model.Company;
import com.br.stockpro.model.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", source = "companyId")
    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "active", constant = "true")
    Product toEntity(ProductCreateRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "category", source = "categoryId")
    void updateEntityFromDTO(ProductUpdatedRequest dto, @MappingTarget Product entity);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toResponseDTO(Product product);

    default Company mapCompany(Long companyId) {
        if (companyId == null) return null;
        return Company.builder()
                .id(companyId)
                .build();
    }

    default Category mapCategory(Long categoryId) {
        if (categoryId == null) return null;
        return Category.builder()
                .id(categoryId)
                .build();
    }
}
