package com.rushhour_app.domain.activity.models;

import java.time.Duration;
import java.util.List;

public record ActivityResponseDTO(

        Long id,
        String name,
        Double price,
        Duration duration,
        Long providerId,
        List<Long> employeesIds

) {
}
