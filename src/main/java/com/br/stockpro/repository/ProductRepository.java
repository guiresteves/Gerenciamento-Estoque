package com.br.stockpro.repository;

import com.br.stockpro.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByCompanyIdAndBarcode(Long companyId, String barcode);
}
