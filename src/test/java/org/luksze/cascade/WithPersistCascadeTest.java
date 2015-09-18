package org.luksze.cascade;

import org.junit.Assert;
import org.junit.Test;
import org.luksze.CleanDatabaseTest;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public class WithPersistCascadeTest extends CleanDatabaseTest {

    public WithPersistCascadeTest() {
        super("cascade-pu");
    }

    @Test
    public void onlyParentObjectMustBePassedToPersist() throws Exception {
        //given
        Corporation corporation = newTransientCorporationInstanceWithAddress();

        //when
        corporationIsPersistedAndAddressIsNot(corporation);

        //then
        bothObjectsAreStoredInDataBase(corporation);
    }

    private void bothObjectsAreStoredInDataBase(Corporation corporation) {
        corporationIsStored(corporation);
        addressIsStored(corporation.address());
    }

    private void addressIsStored(Address address) {
        EntityManager entityManager = entityManager();
        Address fetchedAddress = entityManager.find(Address.class, address.id(), LockModeType.NONE);
        Assert.assertTrue(fetchedAddress.hasEqualContent(address));
        Assert.assertTrue(fetchedAddress.hasEqualIdentifier(address));
//        entityManager.close();
    }

    private void corporationIsStored(Corporation corporation) {
        EntityManager entityManager = entityManager();
        Corporation fetched = entityManager.find(Corporation.class, 1l, LockModeType.NONE);
        Assert.assertTrue(fetched.hasEqualContent(corporation));
        Assert.assertTrue(fetched.hasEqualIdentifier(corporation));
//        entityManager.close();
    }

    private Corporation newTransientCorporationInstanceWithAddress() {
        Corporation corporation = new Corporation();
        corporation.assignAddress(new Address("Boston"));
        return corporation;
    }

    private void corporationIsPersistedAndAddressIsNot(Object object) {
        persistWithinTransaction(entityManager(), object);
    }

}
