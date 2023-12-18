package com.rushhour_app.domain.provider.repository;

import com.rushhour_app.domain.provider.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

    boolean existsByName(String name);

    boolean existsByDomain(String domain);

    @Query("""
            SELECT p FROM Provider p
            INNER JOIN Employee e
            ON e.provider.id = p.id
            WHERE e.account.id = :accountId
            """)
    Optional<Provider> findByEmployeeAccountId(@Param("accountId") Long accountId);

    @Query("""
            SELECT p FROM Provider p 
            JOIN Employee e ON e.provider.id = p.id 
            JOIN Account a ON e.account.id = a.id
            WHERE a.id = :accountId
            """)
    Provider findProviderByAccountId(Long accountId);
}
