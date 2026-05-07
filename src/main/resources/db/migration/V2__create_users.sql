-- V2__create_users.sql
CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    cpf        VARCHAR(14),
    role       ENUM('ADMIN','MANEGE','CASHIER','STOCKER','FINANCIAL') NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    company_id BIGINT,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6)  NOT NULL,

    INDEX idx_user_email   (email),
    INDEX idx_user_company (company_id),

    CONSTRAINT fk_user_company
       FOREIGN KEY (company_id) REFERENCES companies(id)
);