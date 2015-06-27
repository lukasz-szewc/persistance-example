package org.luksze;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, java.util.Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        Instant from = Instant.from(localDate);
        return Date.from(from);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        Instant instant = date.toInstant();
        return LocalDate.from(instant);
    }
}
