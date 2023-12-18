package com.rushhour_app.domain.appointment.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentDTO(

        Long id,
        @NotNull(message = "Start date can't be null!")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")
        LocalDateTime startDate,

        @NotNull(message = "Employee id can't be null!")
        Long employeeId,

        @NotNull(message = "Client id can't be null!")
        Long clientId,

        @NotNull(message = "Activity can't be null!")
        Long activityId

) {
}
