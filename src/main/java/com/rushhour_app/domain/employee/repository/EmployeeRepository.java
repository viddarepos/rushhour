package com.rushhour_app.domain.employee.repository;

import com.rushhour_app.domain.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
            SELECT e FROM Employee e 
            WHERE e.account.id = :accountId
            """)
    Optional<Employee> findByAccountId(@Param("accountId") Long accountId);

    @Query("""
            SELECT e FROM Employee e WHERE 
            provider_id = :providerId 
            """)
    Page<Employee> getAllByProviderId(@Param("providerId") Long providerId, Pageable pageable);

    @Query("""
            SELECT e FROM Employee e 
            WHERE e.id IN :ids
            """)
    List<Employee> getAllByIds(@Param("ids") List<Long> employeeIds);

    Page<Employee> findAllByProviderId(Pageable pageable, Long id);

    @Query("""
            SELECT e FROM Employee e
            WHERE e.id IN (:employeeIds)
            AND e.provider.id = :providerId
            """)
    List<Employee> findByIdInAndProviderId(List<Long> employeeIds, Long providerId);


}

