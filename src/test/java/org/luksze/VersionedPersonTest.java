package org.luksze;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

public class VersionedPersonTest extends CleanDatabaseTest {

    public VersionedPersonTest() {
        super("test-pu");
    }

    @Before
    public void setUp() throws Exception {
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

    private static class FirstTransactionContext {
        private final VersionedPerson versionedPerson;
        private final EntityManager entityManager;

        public FirstTransactionContext(VersionedPerson versionedPerson, EntityManager entityManager) {
            this.versionedPerson = versionedPerson;
            this.entityManager = entityManager;
        }
    }
}
