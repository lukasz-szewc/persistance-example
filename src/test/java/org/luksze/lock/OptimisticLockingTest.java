package org.luksze.lock;

import org.junit.Assert;
import org.junit.Test;
import org.luksze.CleanDatabaseTest;
import org.luksze.VersionedPerson;
import org.luksze.VersionedPersonWrapper;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

public class OptimisticLockingTest extends CleanDatabaseTest {

    public OptimisticLockingTest() {
        super("test-pu");
    }

    @Test
    public void noExplicitLockWillAllowToPersistObjectWithStaleState() throws Exception {
        //given
        persistedPersonInDatabase();

        //and
        TransactionContext transactionContext = firstTransactionAttemptsToModifyLockedObject(LockModeType.NONE);

        //when
        VersionedPerson versionedPerson = secondTransactionTransactionChangeUpdatesObject();

        //and
        VersionedPerson person = firstTransactionCompletes(transactionContext);

        //then
        objectHasBeenSavedWithStaleState(versionedPerson, person);
    }

    @Test
    public void explicitLockWillNotAllowToPersistObjectWithStaleState() throws Exception {
        //given
        persistedPersonInDatabase();

        //and
        TransactionContext transactionContext = firstTransactionAttemptsToModifyLockedObject(LockModeType.OPTIMISTIC);

        //when
        secondTransactionTransactionChangeUpdatesObject();

        //then
        firstTransactionWillFail(transactionContext);
    }

    private void firstTransactionWillFail(TransactionContext context) {
        RollbackException ex = null;
        try {
            context.entityManager.persist(new VersionedPersonWrapper(context.versionedPerson));
            context.entityManager.getTransaction().commit();
        } catch (RollbackException e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
        Assert.assertTrue(ex.getCause() instanceof OptimisticLockException);
    }

    private void objectHasBeenSavedWithStaleState(VersionedPerson versionedPerson, VersionedPerson person) {
        Assert.assertFalse(versionedPerson.hasEqualContent(person));
    }

    private VersionedPerson firstTransactionCompletes(TransactionContext context) {
        VersionedPersonWrapper versionedPersonWrapper = new VersionedPersonWrapper(context.versionedPerson);
        context.entityManager.persist(versionedPersonWrapper);
        context.entityManager.getTransaction().commit();
        return context.versionedPerson;
    }

    private TransactionContext firstTransactionAttemptsToModifyLockedObject(LockModeType lockMode) {
        EntityManager entityManager = entityManager();
        entityManager.getTransaction().begin();
        VersionedPerson versionedPerson = entityManager.find(VersionedPerson.class, 1l, lockMode);
        return new TransactionContext(entityManager, versionedPerson);

    }

    private VersionedPerson secondTransactionTransactionChangeUpdatesObject() {
        EntityManager entityManager = entityManager();
        entityManager.getTransaction().begin();
        VersionedPerson versionedPerson = entityManager.find(VersionedPerson.class, 1l);
        versionedPerson.changeFamilyName("Barkley");
        versionedPerson.changeFirstName("Charles");
        entityManager.getTransaction().commit();
        return versionedPerson;
    }

    private void persistedPersonInDatabase() {
        EntityManager entityManager = entityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new VersionedPerson("John", "Smith"));
        entityManager.getTransaction().commit();
    }

    private static class TransactionContext {
        private final EntityManager entityManager;
        private final VersionedPerson versionedPerson;

        public TransactionContext(EntityManager entityManager, VersionedPerson versionedPerson) {
            this.entityManager = entityManager;
            this.versionedPerson = versionedPerson;
        }
    }
}
