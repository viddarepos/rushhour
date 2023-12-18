package com.rushhour_app.domain.role.service;

import com.rushhour_app.domain.role.entity.Role;
import java.util.Optional;

public interface RoleService {

    Role getRoleById(Long id);

    Optional<Role> findById(Long id);

    Role getRoleByName(String roleName);
}
