package com.br.stockpro.repository;

import com.br.stockpro.enums.BatchStatus;
import com.br.stockpro.model.ProductBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {

    Page<ProductBatch> findByCompanyIdOrderByExpirationDateAsc(Long companyId, Pageable pageable);

    Page<ProductBatch> findByProductIdAndCompanyIdOrderByExpirationDateAsc(Long productId, Long companyId, Pageable pageable);

    Page<ProductBatch> findByCompanyIdAndStatusOrderByExpirationDateAsc(Long companyId, BatchStatus status, Pageable pageable);

    Optional<ProductBatch> findByIdAndCompanyId(Long id, Long companyId);

    boolean existsByBatchCodeAndProductIdAndCompanyId(String batchCode, Long productId, Long companyId);

    // Lotes próximos ao vencimento
    @Query("""
        SELECT b FROM ProductBatch b
        WHERE b.company.id = :companyId
          AND b.active = true
          AND b.status != 'EXPIRED'
          AND b.expirationDate BETWEEN :today AND :limitDate
        ORDER BY b.expirationDate ASC
        """)
    List<ProductBatch> findExpiringBatches(
            Long companyId, LocalDate today, LocalDate limitDate);

    // Lotes vencidos ainda não baixados
    @Query("""
        SELECT b FROM ProductBatch b
        WHERE b.active = true
          AND b.status != 'EXPIRED'
          AND b.expirationDate < :today
        """)
    List<ProductBatch> findExpiredNotProcessed(LocalDate today);

    // todos os lotes ativos de uma empresa para o scheduler
    @Query("""
        SELECT b FROM ProductBatch b
        WHERE b.company.id = :companyId
          AND b.active = true
          AND b.status != 'EXPIRED'
        """)
    List<ProductBatch> findAllActiveBatchesByCompany(Long companyId);

    // Lotes de um produto ordenados por validade - FEFO
    // First Expired First Out
    @Query("""
        SELECT b FROM ProductBatch b
        WHERE b.product.id = :productId
          AND b.company.id = :companyId
          AND b.active = true
          AND b.status != 'EXPIRED'
          AND b.remainingQuantity > 0
        ORDER BY b.expirationDate ASC
        """)
    List<ProductBatch> findActiveBatchesByProductFEFO(
            Long productId, Long companyId);
}