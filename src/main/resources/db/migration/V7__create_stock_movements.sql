-- V7__create_stock_movements.sql
CREATE TABLE stock_movements (
                                 id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 stock_id          BIGINT       NOT NULL,
    product_id        BIGINT       NOT NULL,
    company_id        BIGINT       NOT NULL,
    performed_by_id   BIGINT,
    movement_type   ENUM('ENTRADA','SAIDA','AJUSTE_NEGATIVO','AJUSTE_POSITIVO','RESERVA','LIBERACAO','VENCIMENTO','DEVOLUCAO') NOT NULL,
    movement_origin ENUM('MANUAL','SYSTEM') NOT NULL,
    quantity          INT          NOT NULL,
    previous_quantity INT          NOT NULL,
    current_quantity  INT          NOT NULL,
    reason            VARCHAR(255),
    reference_id      BIGINT,
    reference_type    VARCHAR(50),
    created_at        DATETIME(6)  NOT NULL,

    INDEX idx_movement_company    (company_id),
    INDEX idx_movement_product    (product_id),
    INDEX idx_movement_stock      (stock_id),
    INDEX idx_movement_created_at (created_at),
    INDEX idx_movement_type       (movement_type),

    CONSTRAINT fk_movement_stock
     FOREIGN KEY (stock_id) REFERENCES stocks(id),

    CONSTRAINT fk_movement_product
     FOREIGN KEY (product_id) REFERENCES products(id),

    CONSTRAINT fk_movement_company
     FOREIGN KEY (company_id) REFERENCES companies(id),

    CONSTRAINT fk_movement_performed_by
     FOREIGN KEY (performed_by_id) REFERENCES users(id)
);