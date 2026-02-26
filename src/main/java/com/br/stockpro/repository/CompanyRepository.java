package com.br.stockpro.repository;

import com.br.stockpro.enums.CompanyStatus;
import com.br.stockpro.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByCnpj(String cnpj);
    boolean existsByCnpjAndIdNot(String cnpj, Long id);

    Optional<Company> findByCnpj(String cnpj);

    List<Company> findAllByStatus(CompanyStatus status);

    boolean existsByStatus(CompanyStatus status);
}
