package com.br.stockpro.repository;

import com.br.stockpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndCompanyId(Long id, Long companyId);

    boolean existsByEmail(String email);

    List<User> findAllByCompanyId(Long companyId);
    List<User> findAllByCompanyIdAndActive(Long companyId, Boolean active);
}
