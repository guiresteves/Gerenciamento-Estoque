package com.br.stockpro.repository;


import com.br.stockpro.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByIdAndCompanyId(Long id, Long companyId);

    Optional<Stock> findByProductIdAndCompanyId(Long productId, Long companyId);

    boolean existsByProductIdAndCompanyId(Long productId, Long companyId);

    List<Stock> findAllByCompanyId(Long companyId);

    List<Stock> findAllByCompanyIdAndActiveTrue(Long companyId);

    @Query("""
           SELECT s
           FROM Stock s
           WHERE s.company.id = :companyId
             AND s.active = true
             AND s.quantity <= s.minQuantity
           """)
    List<Stock> findLowStockByCompanyId(Long companyId);

    @Query("""
           SELECT s
           FROM Stock s
           WHERE s.company.id = :companyId
             AND s.active = true
             AND s.quantity = 0
           """)
    List<Stock> findOutOfStockByCompanyId(Long companyId);

    @Query("""
           SELECT s
           FROM Stock s
           WHERE s.company.id = :companyId
             AND s.active = true
             AND (s.quantity - s.reservedQuantity) > 0
           """)
    List<Stock> findAvailableStockByCompanyId(Long companyId);
}
