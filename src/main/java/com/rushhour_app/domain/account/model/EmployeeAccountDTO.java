package com.rushhour_app.domain.account.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rushhour_app.domain.account.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record EmployeeAccountDTO(

        Long id,

        @Email(message = "Not valid email, email should be email@example.com")
        @NotBlank(message = "Email can't be blank")
        String email,

        @NotBlank(message = "Full name can't be blank")
        @Pattern(regexp = "[A-Za-z'-]*$", message = "This full name is not valid!")
        String fullName,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @ValidPassword
        String password,

        @NotNull(message = "Role id can't be blank")
        Long roleId

) {
}
