package com.rushhour_app.domain.role.repository;

import com.rushhour_app.domain.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetTime;
import java.util.Optional;
import java.util.OptionalLong;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
