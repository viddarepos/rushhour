package com.rushhour_app.domain.activity.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.time.Duration;
import java.util.List;

public record ActivityDTO(

        @NotBlank(message = "Name can't be blank!")
        @Pattern(regexp = "[A-Za-z]{2,}" , message = "Minimum 2 letters for the name and only letters")
        String name,

        @NotNull(message = "Account can't be null!")
        @Min(value = 1, message = "Price must be positive number")
        Double price,

        @NotNull(message = "Duration can't be null!")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MINUTES")
        @Schema(example = "15")
        Duration duration,

        @NotNull(message = "Account can't be null!")
        Long providerId,

        @NotEmpty(message = "Employees can't be empty!")
        List<Long> employeesId

) {
}
