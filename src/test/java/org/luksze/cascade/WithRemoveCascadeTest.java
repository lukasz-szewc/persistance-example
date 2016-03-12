package org.luksze.cascade;

import org.junit.Assert;
import org.junit.Test;
import org.luksze.CleanDatabaseTest;

import javax.persistence.EntityManager;

public class WithRemoveCascadeTest extends CleanDatabaseTest {

    public WithRemoveCascadeTest() {
        super("cascade-pu");
    }

    @Test
    public void bothObjectsWillBeRemovedWhenParentObjectPassedToRemove() throws Exception {
        //given
        Corporation corporation = twoObjectsInDatabaseWithCascadeRemoveRelation();

        //when
        removeParentObject(corporation);

        //then
        Assert.assertNull(corporationHasBeenRemoved(corporation));
        Assert.assertNull(addressHasBeenRemoved(corporation.address()));
    }

    private Object addressHasBeenRemoved(Address address) {
        return entityManager().find(Address.class, address.id());
    }

    private Object corporationHasBeenRemoved(Corporation corporation) {
        return entityManager().find(Corporation.class, corporation.id());
    }

    private void removeParentObject(Corporation corporation) {
        EntityManager entityManager = entityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Corporation.class, corporation.id()));
        entityManager.getTransaction().commit();
    }

    private Corporation twoObjectsInDatabaseWithCascadeRemoveRelation() {
        Corporation corporation = new Corporation();
        Address london = new Address("London");
        corporation.assignAddress(london);
        persistWithinTransaction(entityManager(), london, corporation);
        return corporation;
    }

}
