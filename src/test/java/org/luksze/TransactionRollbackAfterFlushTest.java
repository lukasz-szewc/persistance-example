package org.luksze;

import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TransactionRollbackAfterFlushTest extends CleanDatabaseTest {
    private EntityManager entityManager;

    public TransactionRollbackAfterFlushTest() {
        super("test-pu");
        entityManager = entityManager();
    }

    @Test
    public void transactionCanRollBackDespiteManualFlushes() throws Exception {
        //given active transaction with manual flush into database
        activeTransactionAlreadyFlushedIntoDatabase();

        //when transaction rolls back
        transactionRollsBack();

        //then
        thereIsNoPersistedObjectInDatabase();
    }

    private void transactionRollsBack() {
        entityManager.getTransaction().rollback();
    }

    private void activeTransactionAlreadyFlushedIntoDatabase() {
        entityManager.getTransaction().begin();
        entityManager.persist(new VersionedPerson("John", "Smith"));
        entityManager.flush();
        entityManager.persist(new VersionedPerson("David", "Connor"));
        entityManager.flush();
    }

    private void thereIsNoPersistedObjectInDatabase() {
        assertTrue(result().isEmpty());
    }

    private List<VersionedPerson> result() {
        return entityManager().createQuery(query(), VersionedPerson.class).getResultList();
    }

    private String query() {
        return "select p from VersionedPerson p";
    }

}
