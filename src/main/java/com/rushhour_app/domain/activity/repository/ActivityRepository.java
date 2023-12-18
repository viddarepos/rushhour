package com.rushhour_app.domain.activity.repository;

import com.rushhour_app.domain.activity.entity.Activity;
import com.rushhour_app.domain.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("""
            SELECT a FROM Activity a
            WHERE a.provider.id = :providerId
            """)
    Page<Activity> findAllByProviderId(Long providerId, Pageable pageable);

    boolean existsByNameAndProviderId(String name, Long id);

    @Query("""
            SELECT a FROM Activity a
            WHERE a.id IN (:activitiesId)
            AND a.provider.id = :providerId
            """)
    List<Activity> findByIdInAndProviderId(List<Long> activitiesId, Long providerId);}
