package com.rushhour_app.domain.appointment.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record AppointmentClientCreateDTO(

        @NotNull(message = "Start time can't be null!")
        LocalDateTime startTime,

        @NotNull(message = "Provider id can't be null!")
        Long providerId,

        @NotNull(message = "Employee id can't be null! ")
        Long employeeId,

        @NotEmpty(message = "Activities list can't be empty!")
        List<Long> activities
) {
}
