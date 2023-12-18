package com.rushhour_app.domain.account.repository;

import com.rushhour_app.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);
}
