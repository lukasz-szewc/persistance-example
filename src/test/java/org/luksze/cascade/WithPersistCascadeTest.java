package org.luksze.cascade;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class WithPersistCascadeTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("cascade-pu");
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

    private void persistWithinTransaction(EntityManager entityManager, Object object) {
        entityManager.getTransaction().begin();
        entityManager.persist(object);
        entityManager.getTransaction().commit();
    }

    private EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @After
    public void cleanUp() throws Exception {
        entityManagerFactory.close();
    }
}
