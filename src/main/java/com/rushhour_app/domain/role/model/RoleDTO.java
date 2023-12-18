package com.rushhour_app.domain.role.model;

import javax.validation.constraints.NotNull;

public record RoleDTO(

        @NotNull
        Long id,

        @NotNull(message = "Name of role should not be null")
        String name
) {
}
