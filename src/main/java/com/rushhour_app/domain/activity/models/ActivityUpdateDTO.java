package com.rushhour_app.domain.activity.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

public record ActivityUpdateDTO(

        @NotNull(message = "Name can't be null")
        @Pattern(regexp = "[A-Za-z]{2,}", message = "Minimum 2 letters for the name and only letters")
        String name,

        @NotNull(message = "Price can't be null")
        Double price,

        @NotEmpty(message = "Employees can't be empty!")
        List<Long> employeesId

) {
}
