package org.luksze.cascade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Address {

    @Id
    @GeneratedValue
    private Long id;
    private String city;

    public Address() {}

    public Address(String city) {
        this.city = city;
    }

    public boolean hasEqualContent(Address a) {
        return this == a || Objects.equals(city, a.city);
    }

    public boolean hasEqualIdentifier(Address address) {
        return address.id.equals(id);
    }

    public Long id() {
        return id;
    }
}
