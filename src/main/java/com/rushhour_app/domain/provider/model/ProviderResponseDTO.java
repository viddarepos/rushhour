package com.rushhour_app.domain.provider.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record ProviderResponseDTO(

        Long id,
        String name,
        String website,
        String domain,
        String phone,
        LocalTime startTime,
        LocalTime endTime,
        List<DayOfWeek> workingDays

) {
}
