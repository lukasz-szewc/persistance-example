package org.luksze;


import javax.persistence.*;

@Entity
public class VersionedPersonWrapper {

    @Id
    @GeneratedValue
    private Long id;
    @Version
    private long version;

    @OneToOne(cascade = CascadeType.ALL)
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

    public boolean versionHasBeenIncrementedByOne(VersionedPersonWrapper versionedPersonWrapper) {
        return version == versionedPersonWrapper.version + 1;
    }

    public boolean childObjectVersionHasBeenIncrementedByOne(VersionedPersonWrapper versionedPersonWrapper) {
        return versionedPerson.versionHasBeenIncrementedByOne(versionedPersonWrapper.versionedPerson);
    }

    public boolean versionIsTheSame(VersionedPersonWrapper versionedPersonWrapper) {
        return version == versionedPersonWrapper.version;
    }
}
