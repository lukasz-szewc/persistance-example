package org.luksze;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
public class VersionedPersonWrapper {

    @Id
    @GeneratedValue
    private Long id;
    @Version
    private long version;

    @OneToOne(cascade = CascadeType.ALL)
    VersionedPerson versionedPerson;

    protected VersionedPersonWrapper() {}

    public VersionedPersonWrapper(VersionedPerson versionedPerson) {
        this.versionedPerson = versionedPerson;
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

    public void changeFirstName(String name) {
        versionedPerson.changeFirstName(name);
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
