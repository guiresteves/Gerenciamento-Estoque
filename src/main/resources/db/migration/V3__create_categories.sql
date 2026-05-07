-- V3__create_categories.sql
CREATE TABLE categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    description TEXT,
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    company_id  BIGINT       NOT NULL,
    created_at  DATETIME(6)  NOT NULL,
    updated_at  DATETIME(6)  NOT NULL,

    INDEX idx_category_company (company_id),

    CONSTRAINT uk_category_company_name
        UNIQUE (company_id, name),

    CONSTRAINT fk_category_company
        FOREIGN KEY (company_id) REFERENCES companies(id)
);