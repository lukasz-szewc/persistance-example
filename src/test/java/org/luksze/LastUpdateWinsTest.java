package org.luksze;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.assertTrue;

public class LastUpdateWinsTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("hsqldb-test-pu");
        persistWithinTransaction(entityManager(), new Person("john", "smith"));
    }

    @Test
    public void lastUpdateWinsTest() throws Exception {
        //given
        FirstTransactionContext transactionContext = oneTransactionAttemptsToModifyPersistedObject();

        //when
        secondTransactionUpdatesSuccessfullyObjectTheSameTime();

        //then
        modificationFromFirstTransactionOverridesDatabaseState(transactionContext);
    }

    @Test
    public void removedObjectIsNotRecreatedAgainInDatabase() throws Exception {
        //given
        FirstTransactionContext transactionContext = oneTransactionAttemptsToModifyPersistedObject();

        //when
        secondTransactionRemovesThatObjectTheSameTime();

        //then
        objectHasBeenSuccessfullyRemovedFromDatabase(transactionContext);

        //when
        RollbackException rollbackException = firstTransactionAttemptsToPersistObject(transactionContext);

        //then
        exceptionIsThrownAndObjectIsNotSavedInDatabase(transactionContext, rollbackException);
    }

    @After
    public void cleanup() throws Exception {
        entityManagerFactory.close();
    }

    private void exceptionIsThrownAndObjectIsNotSavedInDatabase(FirstTransactionContext transactionContext, RollbackException rollbackException) {
        Person person = entityManager().find(Person.class, transactionContext.person.id());
        Assert.assertNull(person);
        Assert.assertNotNull(rollbackException);
        Assert.assertTrue(rollbackException.getCause() instanceof OptimisticLockException);
    }

    private RollbackException firstTransactionAttemptsToPersistObject(FirstTransactionContext transactionContext) {
        try {
            persistWithinTransaction(transactionContext.entityManager, transactionContext.person);
            return null;
        } catch (RollbackException e) {
            return e;
        }
    }

    private void objectHasBeenSuccessfullyRemovedFromDatabase(FirstTransactionContext transactionContext) {
        Person person = entityManager().find(Person.class, transactionContext.person.id());
        Assert.assertNull(person);
    }

    private void secondTransactionRemovesThatObjectTheSameTime() {
        EntityManager entityManager = entityManager();
        Person person = entityManager.find(Person.class, 1l);
        entityManager.getTransaction().begin();
        entityManager.remove(person);
        entityManager.getTransaction().commit();
    }

    private void modificationFromFirstTransactionOverridesDatabaseState(FirstTransactionContext context) {
        persistWithinTransaction(context.entityManager, context.person);
        Person person = entityManager().find(Person.class, 1l);
        assertTrue(person.hasEqualContent(context.person));
        assertTrue(person.hasEqualIdentifier(context.person));
    }

    private FirstTransactionContext oneTransactionAttemptsToModifyPersistedObject() {
        EntityManager entityManager = entityManager();
        Person person = entityManager.find(Person.class, 1l);
        person.changeFamilyName("Doe");
        return new FirstTransactionContext(entityManager, person);
    }

    private void secondTransactionUpdatesSuccessfullyObjectTheSameTime() {
        EntityManager entityManager = entityManager();
        Person person = entityManager.find(Person.class, 1l);
        person.changeFirstName("Thomas");
        person.changeFamilyName("Miller");
        persistWithinTransaction(entityManager, person);
    }

    private EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private void persistWithinTransaction(EntityManager entityManager, Person person) {
        entityManager.getTransaction().begin();
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }

    private static class FirstTransactionContext {
        private final EntityManager entityManager;
        private final Person person;

        public FirstTransactionContext(EntityManager entityManager, Person person) {
            this.entityManager = entityManager;
            this.person = person;
        }
    }
}
