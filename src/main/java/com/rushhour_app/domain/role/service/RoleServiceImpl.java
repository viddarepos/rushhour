package com.rushhour_app.domain.role.service;

import com.rushhour_app.domain.role.entity.Role;
import com.rushhour_app.domain.role.repository.RoleRepository;
import com.rushhour_app.infrastructure.exception.customException.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isEmpty()) {
            throw new NotFoundException("Role with id %s not found".formatted(id));
        }
        return roleOptional.get();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role with name %s not found".formatted(roleName)));
    }
}
