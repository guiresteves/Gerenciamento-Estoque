package com.br.stockpro.repository;

import com.br.stockpro.enums.MovementType;
import com.br.stockpro.model.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;


public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    Page<StockMovement> findByCompanyIdOrderByCreatedAtDesc(Long companyId, Pageable pageable);

    Page<StockMovement> findByStockIdAndCompanyIdOrderByCreatedAtDesc(Long stockId, Long companyId, Pageable pageable);

    Page<StockMovement> findByProductIdAndCompanyIdOrderByCreatedAtDesc(Long productId, Long companyId, Pageable pageable);

    Page<StockMovement> findByCompanyIdAndMovementTypeOrderByCreatedAtDesc(Long companyId, MovementType movementType, Pageable pageable);

    @Query("""
           SELECT sm
           FROM StockMovement sm
           WHERE sm.company.id = :companyId
             AND sm.createdAt BETWEEN :start AND :end
           ORDER BY sm.createdAt DESC
           """)
    Page<StockMovement> findByCompanyIdAndPeriod(Long companyId, Instant start, Instant end, Pageable pageable);

    @Query("""
           SELECT sm
           FROM StockMovement sm
           WHERE sm.referenceId = :referenceId
             AND sm.referenceType = :referenceType
           ORDER BY sm.createdAt DESC
           """)
    List<StockMovement> findByReference(Long referenceId, String referenceType);
}
