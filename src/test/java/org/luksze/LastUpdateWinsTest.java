package org.luksze;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LastUpdateWinsTest extends CleanDatabaseTest {

    public LastUpdateWinsTest() {
        super("test-pu");
    }

    @Before
    public void setUp() throws Exception {
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

    private void exceptionIsThrownAndObjectIsNotSavedInDatabase(FirstTransactionContext transactionContext, RollbackException rollbackException) {
        EntityManager entityManager = entityManager();
        Person person = entityManager.find(Person.class, transactionContext.person.id());
        assertNull(person);
        Assert.assertNotNull(rollbackException);
        Assert.assertTrue(rollbackException.getCause() instanceof OptimisticLockException);
        entityManager.close();
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
        assertNull(entityManager().find(Person.class, transactionContext.person.id()));
    }

    private void secondTransactionRemovesThatObjectTheSameTime() {
        EntityManager entityManager = entityManager();
        Person person = entityManager.find(Person.class, 1l);
        entityManager.getTransaction().begin();
        entityManager.remove(person);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private void modificationFromFirstTransactionOverridesDatabaseState(FirstTransactionContext context) {
        persistWithinTransaction(context.entityManager, context.person);
        EntityManager entityManager = entityManager();
        Person person = entityManager.find(Person.class, 1l);
        assertTrue(person.hasEqualContent(context.person));
        assertTrue(person.hasEqualIdentifier(context.person));
        entityManager.close();
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

    private static class FirstTransactionContext {
        private final EntityManager entityManager;
        private final Person person;

        public FirstTransactionContext(EntityManager entityManager, Person person) {
            this.entityManager = entityManager;
            this.person = person;
        }
    }
}
