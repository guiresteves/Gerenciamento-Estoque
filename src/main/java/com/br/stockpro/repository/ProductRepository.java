package com.br.stockpro.repository;

import com.br.stockpro.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByBarcodeAndCompanyId(String barcode, Long companyId);

    boolean existsByBarcodeAndCompanyIdAndIdNot(String barcode, Long companyId, Long id);

    Optional<Product> findByBarcodeAndCompanyId(String barcode, Long companyId);
    Optional<Product> findByIdAndCompanyId(Long id, Long companyId);

    List<Product> findAllByCompanyId(Long companyId);
    List<Product> findAllByCompanyIdAndActive(Long companyId, Boolean active);
}
