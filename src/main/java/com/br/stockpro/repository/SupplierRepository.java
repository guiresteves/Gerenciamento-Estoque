package com.br.stockpro.repository;

import com.br.stockpro.model.Company;
import com.br.stockpro.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Optional<Supplier> findByIdAndCompanyId(Long id, Long companyId);

    List<Supplier> findAllByCompanyIdAndActive(Long companyId, Boolean active);
    List<Supplier> findAllByCompanyId(Long companyId);
    Optional<Supplier> findByCnpjAndCompanyId(String cnpj, Long companyId);

    boolean existsByCnpjAndCompanyId(String cnpj, Long companyId);
}
