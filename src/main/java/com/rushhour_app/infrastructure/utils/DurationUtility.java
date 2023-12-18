package com.rushhour_app.infrastructure.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class DurationUtility {

    public static LocalDateTime calculateTime(Duration duration, LocalDateTime time) {
        return time.plusNanos(duration.toNanos());
    }
}
