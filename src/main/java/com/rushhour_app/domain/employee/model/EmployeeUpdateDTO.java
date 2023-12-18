package com.rushhour_app.domain.employee.model;

import com.rushhour_app.domain.account.model.AccountUpdateDTO;

import javax.validation.constraints.NotNull;

public record EmployeeUpdateDTO(

        @NotNull(message = "Title can't be null!")
        String title,

        @NotNull(message = "Account can't be null!")
        AccountUpdateDTO account

) {
}
