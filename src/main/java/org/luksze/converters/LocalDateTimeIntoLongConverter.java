package org.luksze.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Converter(autoApply = false)
class LocalDateTimeIntoLongConverter implements AttributeConverter<LocalDateTime, Long> {

    @Override
    public Long convertToDatabaseColumn(LocalDateTime localDateTime) {
        Instant from = Instant.from(localDateTime);
        return from.toEpochMilli();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long milis) {
        return LocalDateTime.from(Instant.ofEpochMilli(milis));
    }
}
