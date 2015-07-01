package org.luksze.cascade;

import javax.persistence.*;

@Entity
public class Corporation {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;

    public void assignAddress(Address address) {
        this.address = address;
    }

    public boolean hasEqualContent(Corporation c) {
        return this == c || address.hasEqualContent(c.address);
    }

    public boolean hasEqualIdentifier(Corporation c) {
        return c.id.equals(id);
    }

    public Address address() {
        return address;
    }
}
