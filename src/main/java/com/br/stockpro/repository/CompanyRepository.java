package com.br.stockpro.repository;

import com.br.stockpro.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByCnpj(String cnpj);
    boolean existsByCnpjAndIdNot(String cnpj, Long id);

    Optional<Company> findByCnpj(String cnpj);


    @Query("SELECT c FROM Company c WHERE c.active = true")
    List<Company> findAllActiveCompanies();
}
