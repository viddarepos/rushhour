package com.rushhour_app.infrastructure.security.model;

import com.rushhour_app.domain.account.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "Email can't be blank")
        String email,

        @ValidPassword
        String password

) {
}
