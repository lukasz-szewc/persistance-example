package org.luksze.cascade;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BiDirectionalCascadePersist {
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-pu");
    }

    @Test
    public void persistOnlyFirstPartOfRelationship() throws Exception {
        //given
        Bride bride = twoTransientEntitiesWithinBidirectionalRelation("Kate", "John");

        //when
        persistWithinTransaction(entityManager(), bride);

        //then
        bothObjectsAreStoredInDatabase(bride, bride.groom());
    }

    @Test
    public void persistOnlySecondPartOfRelationship() throws Exception {
        //given
        Bride bride = twoTransientEntitiesWithinBidirectionalRelation("Ann", "David");

        //when
        persistWithinTransaction(entityManager(), bride.groom());

        //then
        bothObjectsAreStoredInDatabase(bride, bride.groom());
    }

    private void bothObjectsAreStoredInDatabase(Bride bride, Groom groom) {
        Bride fetchedBride = entityManager().find(Bride.class, bride.id());
        fetchedBride.hasEqualIdentifier(bride);
        Groom fetchedGroom = entityManager().find(Groom.class, groom.id());
        fetchedGroom.hasEqualIdentifier(groom);
    }

    private Bride twoTransientEntitiesWithinBidirectionalRelation(String brideName, String groomName) {
        Bride kate = new Bride(brideName);
        Groom john = new Groom(groomName);
        john.takeBride(kate);
        kate.acceptGroom(john);
        return kate;
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
