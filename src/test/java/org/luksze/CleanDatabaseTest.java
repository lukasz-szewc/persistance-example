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
            System.out.println("before cleanup, entity manager is open: " + entityManager.isOpen() + " is joined to transaction:");
            EntityTransaction transaction = entityManager.getTransaction();
            System.out.println("transaction activity state: " + transaction.isActive());
            if (transaction.isActive()) {
                System.out.println("explicitly rolling back");
                transaction.rollback();
            }
            if (entityManager.isOpen()) {
                System.out.println("entuty manager is joined with active transaction: " + entityManager.isJoinedToTransaction());
                System.out.println("closing entity manager");
                entityManager.close();
            }
            System.out.println("after cleanup. entity manager open flag: " + entityManager.isOpen());
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
