-- V9__create_stock_alerts.sql
CREATE TABLE stock_alerts (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id           BIGINT      NOT NULL,
    product_id           BIGINT      NOT NULL,
    stock_id             BIGINT      NOT NULL,
    alert_type           ENUM('LOW_STOCK','OUT_OF_STOCK','LONG_OUT_OF_STOCK','ABOVE_MAXIMUM') NOT NULL,
    alert_status         ENUM('ACTIVE','RESOLVED','ACKNOWLEDGED') NOT NULL,
    quantity_at_alert    INT         NOT NULL,
    min_stock_at_alert   INT,
    max_stock_at_alert   INT,
    days_out_of_stock    INT,
    created_at           DATETIME(6) NOT NULL,
    resolved_at          DATETIME(6),
    acknowledged_at      DATETIME(6),

    INDEX idx_alert_company    (company_id),
    INDEX idx_alert_product    (product_id),
    INDEX idx_alert_status     (alert_status),
    INDEX idx_alert_type       (alert_type),
    INDEX idx_alert_created_at (created_at),

    CONSTRAINT fk_alert_company
      FOREIGN KEY (company_id) REFERENCES companies(id),

    CONSTRAINT fk_alert_product
      FOREIGN KEY (product_id) REFERENCES products(id),

    CONSTRAINT fk_alert_stock
      FOREIGN KEY (stock_id) REFERENCES stocks(id)
);