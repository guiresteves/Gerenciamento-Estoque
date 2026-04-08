package com.br.stockpro.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "stocks",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_stock_company_product", columnNames = {"company_id", "product_id"})
        },
        indexes = {
                @Index(name = "idx_stock_company", columnList = "company_id"),
                @Index(name = "idx_stock_product", columnList = "product_id"),
                @Index(name = "idx_stock_active", columnList = "active")
        }

)
public class Stock extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder.Default
    @Column(nullable = false)
    private Integer quantity = 0;

    @Builder.Default
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    @Builder.Default
    @Column(name = "min_quantity", nullable = false)
    private Integer minQuantity = 0;

    @Column(name = "average_cost", precision = 19, scale = 2)
    private BigDecimal averageCost;

    @Column(length = 100)
    private String location;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Override
    protected void prePersist() {
        if (quantity == null) quantity = 0;
        if (reservedQuantity == null) reservedQuantity = 0;
        if (minQuantity == null) minQuantity = 0;
        if (active == null) active = true;
    }

    public Integer getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public boolean hasAvailableStock(Integer requestedQuantity) {
        if (requestedQuantity == null || requestedQuantity <= 0) {
            return false;
        }
        return getAvailableQuantity() >= requestedQuantity;
    }

    public boolean isBelowMinimum() {
        return quantity < minQuantity;
    }
}
