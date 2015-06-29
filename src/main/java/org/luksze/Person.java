package org.luksze;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;

    protected Person() {}

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void changeFirstName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void changeFamilyName(String surname) {
        this.surname = surname;
    }

    public boolean hasEqualContent(Person p) {
        return this == p || Objects.equals(name, p.name) && Objects.equals(surname, p.surname);
    }

    public boolean hasEqualIdentifier(Person person) {
        return person.id.equals(id);
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
    }
}
