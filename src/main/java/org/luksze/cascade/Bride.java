package org.luksze.cascade;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Bride {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Groom groom;

    public Bride(String name) {
        this.name = name;
    }

    public Bride() {}

    public void acceptGroom(Groom groom) {
        this.groom = groom;
    }

    public Long id() {
        return id;
    }

    public boolean hasEqualIdentifier(Bride bride) {
        return bride.id.equals(id);
    }

    public Groom groom() {
        return groom;
    }
}
