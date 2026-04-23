package com.br.stockpro.model;

import com.br.stockpro.enums.AlertStatus;
import com.br.stockpro.enums.AlertType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "stock_alerts",
        indexes = {
                @Index(name = "idx_alert_company", columnList = "company_id"),
                @Index(name = "idx_alert_product", columnList = "product_id"),
                @Index(name = "idx_alert_status", columnList = "status"),
                @Index(name = "idx_alert_type", columnList = "alert_type"),
                @Index(name = "idx_alert_created_at", columnList = "created_at")
        }
)
public class AlertStock {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false, length = 20)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private AlertStatus alertStatus;

    @Column(name = "quantity_at_alert", nullable = false)
    private Integer quantityAtAlert;

    @Column(name = "min_quantity_at_alert", nullable = false)
    private Integer minStockAtAlert;

    @Column(name = "max_quantity_at_alert", nullable = false)
    private Integer maxStockAtAlert;

    @Column(name = "days_out_of_stock")
    private Integer daysOutOfStock;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant resolvedAt;

    @Column
    private Instant acknowledgedAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = Instant.now();
        if (this.alertStatus == null) this.alertStatus = AlertStatus.ACTIVE;
    }
}
