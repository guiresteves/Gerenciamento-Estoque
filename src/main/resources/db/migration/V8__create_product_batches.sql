-- V8__create_product_batches.sql
CREATE TABLE product_batches (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id          BIGINT       NOT NULL,
    product_id          BIGINT       NOT NULL,
    stock_id            BIGINT       NOT NULL,
    stock_movement_id   BIGINT,
    batch_code          VARCHAR(100) NOT NULL,
    expiration_date     DATE         NOT NULL,
    quantity            INT          NOT NULL,
    remaining_quantity  INT          NOT NULL,
    days_to_expiration  INT,
    status              ENUM('ACTIVE','EXPIRING','EXPIRED') NOT NULL,
    active              BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at          DATETIME(6)  NOT NULL,
    updated_at          DATETIME(6)  NOT NULL,

    INDEX idx_batch_company    (company_id),
    INDEX idx_batch_product    (product_id),
    INDEX idx_batch_stock      (stock_id),
    INDEX idx_batch_expiration (expiration_date),
    INDEX idx_batch_status     (status),

    CONSTRAINT fk_batch_company
     FOREIGN KEY (company_id) REFERENCES companies(id),

    CONSTRAINT fk_batch_product
     FOREIGN KEY (product_id) REFERENCES products(id),

    CONSTRAINT fk_batch_stock
     FOREIGN KEY (stock_id) REFERENCES stocks(id),

    CONSTRAINT fk_batch_movement
     FOREIGN KEY (stock_movement_id) REFERENCES stock_movements(id)
);