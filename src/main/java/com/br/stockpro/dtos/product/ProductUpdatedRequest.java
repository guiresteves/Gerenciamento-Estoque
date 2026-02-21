package com.br.stockpro.dtos.product;

import com.br.stockpro.enums.UnitOfMeasure;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductUpdatedRequest(

        @NotBlank
        String name,

        String description,

        @NotBlank
        String barcode,

        @NotNull
        UnitOfMeasure unitOfMeasure,

        @NotNull
        Boolean active,

        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 10, fraction = 2)
        BigDecimal costPrice,

        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 10, fraction = 2)
        BigDecimal salePrice,

        @NotNull
        Long companyId
) {
}
