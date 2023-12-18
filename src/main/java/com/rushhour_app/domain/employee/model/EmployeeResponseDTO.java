package com.rushhour_app.domain.employee.model;

import com.rushhour_app.domain.account.model.AccountResponseDTO;

import java.time.LocalDate;

public record EmployeeResponseDTO(

        Long id,
        String title,
        String phone,
        Double rate,
        LocalDate date,
        Long providerId,
        AccountResponseDTO account

) {
}
