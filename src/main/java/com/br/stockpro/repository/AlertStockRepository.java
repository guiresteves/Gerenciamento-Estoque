package com.br.stockpro.repository;

import com.br.stockpro.enums.AlertStatus;
import com.br.stockpro.enums.AlertType;
import com.br.stockpro.model.AlertStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlertStockRepository extends JpaRepository<AlertStock, Long> {

    Page<AlertStock> findByCompanyIdOrderByCreatedAtDesc(Long companyId, Pageable pageable);

    Page<AlertStock> findByCompanyIdAndStatusOrderByCreatedAtDesc(
            Long companyId, AlertStatus status, Pageable pageable);

    Page<AlertStock> findByCompanyIdAndAlertTypeOrderByCreatedAtDesc(
            Long companyId, AlertType type, Pageable pageable);

    // verifica se já existe alerta ativo para evitar duplicatas
    boolean existsByProductIdAndCompanyIdAndAlertTypeAndStatus(
            Long productId, Long companyId, AlertType type, AlertStatus status);

    // busca alerta ativo específico para resolver automaticamente
    Optional<AlertStock> findByProductIdAndCompanyIdAndAlertTypeAndStatus(
            Long productId, Long companyId, AlertType type, AlertStatus status);

    // contagens para o summary
    long countByCompanyIdAndStatus(Long companyId, AlertStatus status);

    long countByCompanyIdAndAlertTypeAndStatus(
            Long companyId, AlertType type, AlertStatus status);

    // alertas ativos de um produto específico
    List<AlertStock> findByProductIdAndCompanyIdAndStatus(
            Long productId, Long companyId, AlertStatus status);
}
