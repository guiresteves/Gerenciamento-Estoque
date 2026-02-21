package com.br.stockpro.dtos.product;

import com.br.stockpro.enums.UnitOfMeasure;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(

    @NotBlank
    String name,

    String description,

    @NotBlank
    String barcode,

    @NotNull
    UnitOfMeasure unitOfMeasure,

    Boolean trackExpiration,

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    BigDecimal costPrice,

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    BigDecimal salePrice,

    @NotNull
    Long companyId,

    @NotNull
    Long categoryId
) {
}
