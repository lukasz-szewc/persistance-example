package org.luksze.cascade;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Groom {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Bride bride;

    public Groom() {}

    public Groom(String name) {
        this.name = name;
    }

    public void takeBride(Bride bride) {
        this.bride = bride;
    }

    public boolean hasEqualIdentifier(Groom groom) {
        return groom.id.equals(id);
    }

    public Long id() {
        return id;
    }
}
