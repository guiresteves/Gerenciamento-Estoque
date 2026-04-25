package com.br.stockpro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@Table
@NoArgsConstructor
@AllArgsConstructor
public class ProductBatch extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_movement_id")
    private StockMovement stockMovement;

    @Column(name = "bath_code", nullable = false)
    private String batchCode;

    @Column(name = "expiation_date", nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "remaining_quantity", nullable = false)
    private Integer remainingQuantity;
}
