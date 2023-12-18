package com.rushhour_app.domain.client.repository;

import com.rushhour_app.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("""
            SELECT c FROM Client c 
            WHERE c.account.id = :accountId
            """)
    Optional<Client> findByAccountId(@Param("accountId") Long accountId);

}
