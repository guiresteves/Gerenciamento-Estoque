package com.br.stockpro.model;

import com.br.stockpro.enums.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(
        name = "products",
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

    @Column(nullable = false, length = 250)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UnitOfMeasure unitOfMeasure;

    @Builder.Default
    @Column(name = "track_expiration", nullable = false)
    private Boolean trackExpiration = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "cost_price", nullable = false)
    private BigDecimal costPrice;

    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;

    @Column(name = "min_stock")
    private Integer minStock;

    @Column(name = "max_stock")
    private Integer maxStock;

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
