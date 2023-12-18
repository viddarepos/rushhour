package com.rushhour_app.domain.provider.repository.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Component
@Converter(autoApply = true)
public class DayOfWeekConverter implements AttributeConverter<List<DayOfWeek>, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DayOfWeekConverter.class);

    private final ObjectMapper mapper;

    public DayOfWeekConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String convertToDatabaseColumn(List<DayOfWeek> dayOfWeeks) {
        try {
            return mapper.writeValueAsString(dayOfWeeks);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Error when converting to DB column:", e);
            return "[]";
        }
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(String column) {
        try {
            return mapper.readValue(column, new TypeReference<>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (JsonProcessingException e) {
            LOGGER.warn("Error when converting from DB column:", e);
            return new ArrayList<>(0);
        }
    }
}

