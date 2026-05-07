-- V5__create_suppliers.sql
CREATE TABLE suppliers (
   id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
   legal_name             VARCHAR(250) NOT NULL,
   trade_name             VARCHAR(250) NOT NULL,
   cnpj                   VARCHAR(18)  NOT NULL,
   state_registration     VARCHAR(50),
   municipal_registration VARCHAR(50),
   phone                  VARCHAR(20)  NOT NULL,
   email                  VARCHAR(150) NOT NULL,
   contact_name           VARCHAR(150),
   active                 BOOLEAN      NOT NULL DEFAULT TRUE,
   company_id             BIGINT       NOT NULL,
   created_at             DATETIME(6)  NOT NULL,
   updated_at             DATETIME(6)  NOT NULL,

   INDEX idx_supplier_company (company_id),
   INDEX idx_supplier_cnpj    (cnpj),

   CONSTRAINT uk_supplier_company_cnpj
       UNIQUE (company_id, cnpj),

   CONSTRAINT fk_supplier_company
       FOREIGN KEY (company_id) REFERENCES companies(id)
);