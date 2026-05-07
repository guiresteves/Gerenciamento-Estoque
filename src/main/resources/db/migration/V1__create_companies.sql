-- V1__create_companies.sql
CREATE TABLE companies (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    legal_name              VARCHAR(250) NOT NULL,
    trade_name              VARCHAR(250) NOT NULL,
    cnpj                    VARCHAR(18)  NOT NULL UNIQUE,
    state_registration      VARCHAR(50),
    municipal_registration  VARCHAR(50),
    company_type            ENUM('MEI','EI','SLU','LTDA','SS','SA') NOT NULL,
    phone                   VARCHAR(20)  NOT NULL,
    email                   VARCHAR(150) NOT NULL,
    active                  BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at              DATETIME(6)  NOT NULL,
    updated_at              DATETIME(6)  NOT NULL,

    INDEX idx_company_cnpj  (cnpj),
    INDEX idx_company_email (email)
);