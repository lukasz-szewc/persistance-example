package org.luksze;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
        whenSecondTransactionUpdatesSuccessfullyObjectTheSameTime();

        //then
        modificationFromFirstTransactionOverridesDatabaseState(transactionContext);

    }

    @After
    public void cleanup() throws Exception {
        entityManagerFactory.close();
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
        person.setSurname("Doe");
        return new FirstTransactionContext(entityManager, person);
    }

    private void whenSecondTransactionUpdatesSuccessfullyObjectTheSameTime() {
        EntityManager entityManager = entityManager();
        Person person = entityManager.find(Person.class, 1l);
        person.setName("Thomas");
        person.setSurname("Miller");
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
