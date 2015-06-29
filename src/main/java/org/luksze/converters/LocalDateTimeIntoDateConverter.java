package org.luksze.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@SuppressWarnings("unused")
@Converter(autoApply = true)
class LocalDateTimeIntoDateConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
        Instant from = Instant.from(localDateTime);
        return Date.from(from);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.from(instant);
    }
}
