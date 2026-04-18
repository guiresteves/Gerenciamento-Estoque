package com.br.stockpro.repository;

import com.br.stockpro.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndCompanyId(Long id, Long companyId);

    List<Category> findAllByCompanyId(Long companyId);
    List<Category> findAllByCompanyIdAndActive(Long companyId, Boolean active);

    boolean existsByNameIgnoreCaseAndCompanyId(String name, Long companyId);
    boolean existsByNameIgnoreCaseAndCompanyIdAndIdNot(String name, Long companyId, Long id);
}
