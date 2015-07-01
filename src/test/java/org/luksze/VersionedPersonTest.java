package org.luksze;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.luksze.config.Configuration;

import javax.persistence.*;

public class VersionedPersonTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-pu", new Configuration());
        persistWithinTransaction(entityManager(), new VersionedPerson("john", "smith"));
    }

    @Test
    public void optimisticLockingTest() throws Exception {
        //given
        FirstTransactionContext context = firstTransactionAttemptsToModifyPerson();

        //when
        secondTransactionPersists();

        //then
        firstTransactionFailsBecauseOfOptimisticLockingException(context);
    }

    private void firstTransactionFailsBecauseOfOptimisticLockingException(FirstTransactionContext bla) {
        try {
            persistWithinTransaction(bla.entityManager, bla.versionedPerson);
            Assert.fail();
        } catch (RollbackException e) {
            Assert.assertTrue(e.getCause() instanceof OptimisticLockException);
        }
    }

    private FirstTransactionContext firstTransactionAttemptsToModifyPerson() {
        EntityManager entityManager = entityManager();
        VersionedPerson versionedPerson = entityManager.find(VersionedPerson.class, 1l);
        versionedPerson.changeFirstName("Thomas");
        return new FirstTransactionContext(versionedPerson, entityManager);
    }

    private void secondTransactionPersists() {
        EntityManager entityManager = entityManager();
        VersionedPerson versionedPerson = entityManager.find(VersionedPerson.class, 1l);
        versionedPerson.changeFamilyName("Doe");
        versionedPerson.changeFirstName("David");
        persistWithinTransaction(entityManager, versionedPerson);
    }

    @After
    public void cleanup() throws Exception {
        entityManagerFactory.close();
    }

    private EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private void persistWithinTransaction(EntityManager entityManager, VersionedPerson person) {
        entityManager.getTransaction().begin();
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }

    private static class FirstTransactionContext {
        private final VersionedPerson versionedPerson;
        private final EntityManager entityManager;

        public FirstTransactionContext(VersionedPerson versionedPerson, EntityManager entityManager) {
            this.versionedPerson = versionedPerson;
            this.entityManager = entityManager;
        }
    }
}
