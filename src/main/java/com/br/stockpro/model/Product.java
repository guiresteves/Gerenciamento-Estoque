package com.br.stockpro.model;

import com.br.stockpro.enums.UnitOfMeasure;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(
        name = "product",

        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"company_id", "barcode"}
                )
        },
        indexes = {
                @Index(name = "idx_product_company", columnList = "company_id"),
                @Index(name = "idx_product_category", columnList = "category_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "track_expiration", nullable = false)
    private Boolean trackExpiration;

    @Column(nullable = false)
    private Boolean active;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "cost_price", nullable = false)
    private BigDecimal costPrice;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Override
    protected void prePersist() {
        if (trackExpiration == null) trackExpiration = false;
        if (active == null) active = true;
    }
}
