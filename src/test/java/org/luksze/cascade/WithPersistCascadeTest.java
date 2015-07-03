package org.luksze.cascade;

import org.junit.Assert;
import org.junit.Test;
import org.luksze.CleanDatabaseTest;

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
        Address fetchedAddress = entityManager().find(Address.class, address.id());
        Assert.assertTrue(fetchedAddress.hasEqualContent(address));
        Assert.assertTrue(fetchedAddress.hasEqualIdentifier(address));
    }

    private void corporationIsStored(Corporation corporation) {
        Corporation fetched = entityManager().find(Corporation.class, 1l);
        Assert.assertTrue(fetched.hasEqualContent(corporation));
        Assert.assertTrue(fetched.hasEqualIdentifier(corporation));
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
