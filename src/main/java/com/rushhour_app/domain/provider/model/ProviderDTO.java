package com.rushhour_app.domain.provider.model;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record ProviderDTO(

        @Size(min = 3)
        @NotBlank(message = "Name can't be blank!")
        String name,

        @NotBlank(message = "Website can't be blank!")
        @URL(message = "The url of website is not valid")
        String website,

        @Size(min = 2, message = "Domain should be at least 2 char long")
        @Pattern(regexp = "^[a-zA-Z]*$", message = "Domain should contain only letters")
        String domain,

        @NotBlank(message = "Name can't be blank!")
        @Pattern(regexp = "[+0-9]{9,15}$", message = "Phone should be valid with minimum of 9 and maximum of 15 numbers and can be added the +")
        String phone,

        @NotNull(message = "Start time can't be null!")
        LocalTime startTime,

        @NotNull(message = "End time can't be null!")
        LocalTime endTime,

        List<DayOfWeek> workingDays
) {
}
