-- V4__create_products.sql
CREATE TABLE products (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(250)   NOT NULL,
    description      VARCHAR(500),
    barcode          VARCHAR(50)    NOT NULL,
    unit_of_measure  ENUM('UNIDADE','KG','G','L','ML','CX','PCT') NOT NULL,
    track_expiration BOOLEAN        NOT NULL DEFAULT FALSE,
    active           BOOLEAN        NOT NULL DEFAULT TRUE,
    cost_price       DECIMAL(12, 2) NOT NULL,
    sale_price       DECIMAL(12, 2) NOT NULL,
    min_stock        INT,
    max_stock        INT,
    company_id       BIGINT         NOT NULL,
    category_id      BIGINT         NOT NULL,
    created_at       DATETIME(6)    NOT NULL,
    updated_at       DATETIME(6)    NOT NULL,

    INDEX idx_product_company  (company_id),
    INDEX idx_product_category (category_id),

    CONSTRAINT uk_product_company_barcode
      UNIQUE (company_id, barcode),

    CONSTRAINT fk_product_company
      FOREIGN KEY (company_id) REFERENCES companies(id),

    CONSTRAINT fk_product_category
      FOREIGN KEY (category_id) REFERENCES categories(id)
);