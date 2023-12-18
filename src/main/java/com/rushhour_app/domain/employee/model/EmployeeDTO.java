package com.rushhour_app.domain.employee.model;

import com.rushhour_app.domain.account.model.EmployeeAccountDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public record EmployeeDTO(

        Long id,

        @NotNull(message = "Title cant be null!")
        @Pattern(regexp = "[A-Za-z0-9]{2,}", message = "Title should be minimum 2 char long and only alphanumeric")
        String title,

        @NotNull(message = "Phone can't be null!")
        @Pattern(regexp = "^[+0-9]*$", message = "Phone should be only numeric")
        String phone,

        @NotNull(message = "Rate can't be null!")
        @Min(value = 1)
        Double rate,

        @NotNull(message = "Local date should not be null")
        @DateTimeFormat(pattern = "yyyy/mm/dd")
        LocalDate date,

        @NotNull(message = "Provider is not set")
        @Min(value = 1)
        Long providerId,

        @Valid
        @NotNull(message = "Account can't be null!")
        EmployeeAccountDTO account

) {
}
