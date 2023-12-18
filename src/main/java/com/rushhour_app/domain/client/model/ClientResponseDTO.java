package com.rushhour_app.domain.client.model;

import com.rushhour_app.domain.account.model.ClientAccountDTO;

public record ClientResponseDTO(

        Long id,
        String phone,
        String address,
        ClientAccountDTO account

) {
}
