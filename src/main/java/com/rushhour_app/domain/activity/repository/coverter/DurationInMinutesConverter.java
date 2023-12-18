package com.rushhour_app.domain.activity.repository.coverter;

import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;

@Component
@Converter(autoApply = true)
public class DurationInMinutesConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return duration.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long durationInMinutes) {
        return Duration.ofMinutes(durationInMinutes);
    }

}
