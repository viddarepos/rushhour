package com.rushhour_app.domain.appointment.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentUpdateDTO(

        @NotNull(message = "Start date can't be null!")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")
        LocalDateTime startDate

) {
}
