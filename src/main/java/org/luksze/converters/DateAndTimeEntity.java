package org.luksze.converters;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class DateAndTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate localDate;

    public DateAndTimeEntity() {
        this.localDate = LocalDate.now();
    }

    public LocalDate localDate() {
        return localDate;
    }

    public Long id() {
        return id;
    }
}
