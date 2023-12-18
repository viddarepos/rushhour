package com.rushhour_app.domain.account.model;

import com.rushhour_app.domain.account.validation.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record AccountUpdateDTO(

        @NotBlank(message = "Full name can't be blank!")
        @Pattern(regexp = "[A-Za-z'-]*$", message = "This full name is not valid!")
        String fullName,

        @ValidPassword
        String password

) {
}
