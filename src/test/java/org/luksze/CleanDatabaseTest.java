package org.luksze;

import org.junit.After;
import org.luksze.config.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class CleanDatabaseTest {

    private EntityManagerFactory entityManagerFactory;
    private List<EntityManager> registeredEntityManagers = new ArrayList<>();

    public CleanDatabaseTest(String persistenceUnitName) {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, new Configuration());
    }

    @After
    public void cleanupSuperClass() throws Exception {
        closeEntityManagers();
        entityManagerFactory.close();
    }

    private void closeEntityManagers() {
        for (EntityManager entityManager : registeredEntityManagers) {
            System.out.println("before is open: " + entityManager.isOpen() + " is joined to transaction:" + entityManager.isJoinedToTransaction());
            EntityTransaction transaction = entityManager.getTransaction();
            System.out.println("transaction state: " + transaction.isActive());
            if (transaction.isActive()) {
                transaction.rollback();
            }
            if (entityManager.isOpen()) {
                entityManager.close();
            }
            System.out.println("after is open: " + entityManager.isOpen());
        }
    }

    protected void persistWithinTransaction(EntityManager entityManager, Object object) {
        entityManager.getTransaction().begin();
        entityManager.persist(object);
        entityManager.getTransaction().commit();
    }

    protected EntityManager entityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        registeredEntityManagers.add(entityManager);
        return entityManager;
    }
}
