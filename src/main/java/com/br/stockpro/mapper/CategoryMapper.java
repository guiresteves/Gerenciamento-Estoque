package com.br.stockpro.mapper;

import com.br.stockpro.dtos.category.CategoryCreateRequest;
import com.br.stockpro.dtos.category.CategoryResponse;
import com.br.stockpro.dtos.category.CategoryUpadateRequest;
import com.br.stockpro.model.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(
            CategoryUpadateRequest request,
            @MappingTarget Category entity
    );

    CategoryResponse toResponse(Category entity);
}
