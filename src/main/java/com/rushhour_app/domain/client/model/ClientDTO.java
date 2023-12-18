package com.rushhour_app.domain.client.model;

import com.rushhour_app.domain.account.model.ClientAccountDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record ClientDTO(

        Long id,

        @NotNull(message = "Phone can't be null!")
        @Pattern(regexp = "[+0-9]{9,15}$", message = "Phone should be valid with minimum of 9 numbers and maximum of 15 and can be added the +")
        String phone,

        @Pattern(regexp = "[A-Za-z0-9]{3,}", message = "Address can have letters and number but should be minimum 3 char long")
        String address,

        @Valid
        ClientAccountDTO account
) {
}
