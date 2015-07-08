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
            EntityTransaction transaction = entityManager.getTransaction();
            if (transaction.isActive()) {
                System.out.println("explicitly rolling back");
                transaction.rollback();
            }
            if (entityManager.isOpen()) {
                System.out.println("closing entity manager");
//                entityManager.clear();
                entityManager.close();
            }
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
