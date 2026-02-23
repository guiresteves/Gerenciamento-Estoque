package com.br.stockpro.repository;

import com.br.stockpro.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByCompanyIdAndBarcode(Long companyId, String barcode);

    Optional<Product> findByIdAndCompanyIdAndAtivoTrue(Long id, Long companyId);
}
