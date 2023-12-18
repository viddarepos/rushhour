package com.rushhour_app.domain.account.model;

import com.rushhour_app.domain.role.model.RoleDTO;

public record AccountResponseDTO(

        Long id,
        String email,
        String fullName,
        RoleDTO role

) {
}
