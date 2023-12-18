package com.rushhour_app.domain.provider.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalTime;

public record ProviderUpdateDTO(

        @Size(min = 3)
        @NotBlank(message = "Name can't be blank!")
        String name,

        @Pattern(regexp = "[+0-9]{9,15}$", message = "Phone should be valid with minimum of 9 and maximum of 15 numbers and can be added the +")
        String phone,

        @NotNull(message = "Start time can't be null!")
        LocalTime startTime,

        @NotNull(message = "Start time can't be null!")
        LocalTime endTime
) {
}
