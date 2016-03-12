package org.luksze;


import javax.persistence.*;

@Entity
public class VersionedPersonWrapper {

    @Id
    @GeneratedValue
    private Long id;
    @Version
    private long version;

    @OneToOne
    VersionedPerson versionedPerson;

    public VersionedPersonWrapper() {
    }

    public VersionedPersonWrapper(VersionedPerson versionedPerson) {
        this.versionedPerson = versionedPerson;
    }

    public VersionedPerson getVersionedPerson() {
        return versionedPerson;
    }

    @Override
    public String toString() {
        return "VersionedPersonWrapper{" +
                "id=" + id +
                ", version=" + version +
                ", versionedPerson=" + versionedPerson +
                '}';
    }
}
