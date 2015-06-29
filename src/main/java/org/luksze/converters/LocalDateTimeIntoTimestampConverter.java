package org.luksze.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Converter(autoApply = false)
class LocalDateTimeIntoTimestampConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        Instant from = Instant.from(localDateTime);
        return Timestamp.from(from);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        return LocalDateTime.from(instant);
    }
}
