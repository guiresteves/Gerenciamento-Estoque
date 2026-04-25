package com.br.stockpro.repository;

import com.br.stockpro.enums.AlertStatus;
import com.br.stockpro.enums.AlertType;
import com.br.stockpro.model.StockAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {

    Page<StockAlert> findByCompanyIdOrderByCreatedAtDesc(Long companyId, Pageable pageable);

    Page<StockAlert> findByCompanyIdAndAlertStatusOrderByCreatedAtDesc(
            Long companyId, AlertStatus alertStatus, Pageable pageable);

    Page<StockAlert> findByCompanyIdAndAlertTypeOrderByCreatedAtDesc(
            Long companyId, AlertType alertType, Pageable pageable);

    // verifica se já existe alerta ativo para evitar duplicatas
    boolean existsByProductIdAndCompanyIdAndAlertTypeAndAlertStatus(
            Long productId, Long companyId, AlertType alertType, AlertStatus alertStatus);

    // busca alerta ativo específico para resolver automaticamente
    Optional<StockAlert> findByProductIdAndCompanyIdAndAlertTypeAndAlertStatus(
            Long productId, Long companyId, AlertType alertType, AlertStatus alertStatus);

    // contagens para o summary
    long countByCompanyIdAndAlertStatus(Long companyId, AlertStatus alertStatus);

    long countByCompanyIdAndAlertTypeAndAlertStatus(
            Long companyId, AlertType alertType, AlertStatus alertStatus);

    // alertas ativos de um produto específico
    List<StockAlert> findByProductIdAndCompanyIdAndAlertStatus(
            Long productId, Long companyId, AlertStatus alertStatus);
}
