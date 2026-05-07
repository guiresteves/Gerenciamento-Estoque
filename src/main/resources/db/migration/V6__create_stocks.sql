-- V6__create_stocks.sql
CREATE TABLE stocks (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id        BIGINT   NOT NULL,
    product_id        BIGINT   NOT NULL,
    quantity          INT      NOT NULL DEFAULT 0,
    reserved_quantity INT      NOT NULL DEFAULT 0,
    location          VARCHAR(100),
    active            BOOLEAN  NOT NULL DEFAULT TRUE,
    created_at        DATETIME(6) NOT NULL,
    updated_at        DATETIME(6) NOT NULL,

    INDEX idx_stock_company (company_id),
    INDEX idx_stock_product (product_id),
    INDEX idx_stock_active  (active),

    CONSTRAINT uk_stock_company_product
        UNIQUE (company_id, product_id),

    CONSTRAINT fk_stock_company
        FOREIGN KEY (company_id) REFERENCES companies(id),

    CONSTRAINT fk_stock_product
        FOREIGN KEY (product_id) REFERENCES products(id)
);