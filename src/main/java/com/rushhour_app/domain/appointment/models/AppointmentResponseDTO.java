package com.rushhour_app.domain.appointment.models;

import com.rushhour_app.domain.activity.models.ActivityResponseDTO;
import com.rushhour_app.domain.client.model.ClientResponseDTO;
import com.rushhour_app.domain.employee.model.EmployeeResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AppointmentResponseDTO(

        Long id,
        LocalDateTime startDate,
        LocalDateTime endDate,
        EmployeeResponseDTO employee,
        ClientResponseDTO client,
        ActivityResponseDTO activity

) {
}
