package com.rushhour_app.domain.appointment.repository;

import com.rushhour_app.domain.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.DoubleStream;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {


    @Query("""
            SELECT a FROM Appointment a WHERE a.employee.id = :employeeId AND
            ((a.endDate > :startDate AND a.endDate <= :endDate)  OR
            (a.startDate >= :startDate AND a.startDate < :endDate))
            """)
    List<Appointment> checkIfEmployeeIsBusy(Long employeeId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
            SELECT a FROM Appointment a 
            JOIN Employee e ON a.employee.id = e.id 
            JOIN Provider p ON e.provider.id = p.id 
            WHERE p.id = :id            
            """)
    Page<Appointment> findAllByProviderId(Long id, Pageable pageable);

    Page<Appointment> findAllByEmployeeId(Long id, Pageable pageable);

    Page<Appointment> findAllByClientId(Long id, Pageable pageable);

    List<Appointment> findAllByActivityId(Long id);

    boolean existsByStartDate(LocalDateTime localDateTime);

}
