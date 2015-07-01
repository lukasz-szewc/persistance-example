package org.luksze.cascade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void assignAddress(Address address) {
        this.address = address;
    }
}
