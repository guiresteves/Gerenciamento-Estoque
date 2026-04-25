package com.br.stockpro.model;

import com.br.stockpro.enums.BatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private BatchStatus status;

    @Column(name = "days_to_expiration")
    private Integer daysToExpiration;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Override
    protected void prePersist() {
        if (status == null) status = BatchStatus.ACTIVE;
        if (remainingQuantity == null) remainingQuantity = quantity;
        if (active == null) active = true;
        updateDaysToExpiration();
    }

    @Override
    protected void preUpdate() {
        updateDaysToExpiration();
    }

    public void updateDaysToExpiration() {
        this.daysToExpiration = (int) java.time.temporal.ChronoUnit.DAYS
                .between(LocalDate.now(), expirationDate);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public boolean isExpiring(int days) {
        return !isExpired() && daysToExpiration <= days;
    }
}
