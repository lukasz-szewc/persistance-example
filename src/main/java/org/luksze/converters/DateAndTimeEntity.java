package org.luksze.converters;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class DateAndTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime firstLocalDateTime;
    @Convert(converter = LocalDateTimeIntoTimestampConverter.class, attributeName = "secondLocalDateTime")
    private LocalDateTime secondLocalDateTime;
    @Convert(converter = LocalDateTimeIntoLongConverter.class, attributeName = "thirdLocalDateTime")
    private LocalDateTime thirdLocalDateTime;

    public DateAndTimeEntity() {
        this.firstLocalDateTime = LocalDateTime.now();
        this.secondLocalDateTime = LocalDateTime.now();
        this.thirdLocalDateTime = LocalDateTime.now();
    }

    public LocalDateTime firstLocalDateTime() {
        return firstLocalDateTime;
    }

    public LocalDateTime secondLocalDateTime() {
        return secondLocalDateTime;
    }

    public LocalDateTime thirdLocalDateTime() {
        return thirdLocalDateTime;
    }

    public Long id() {
        return id;
    }
}
