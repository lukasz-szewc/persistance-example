package org.luksze;

import org.junit.After;
import org.luksze.config.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
        ListIterator<EntityManager> entityManagerListIterator = registeredEntityManagers.listIterator(registeredEntityManagers.size());
        while (entityManagerListIterator.hasPrevious()) {
            EntityManager entityManager = entityManagerListIterator.previous();
            EntityTransaction transaction = entityManager.getTransaction();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    protected void persistWithinTransaction(EntityManager entityManager, Object object) {
        entityManager.getTransaction().begin();
        entityManager.persist(object);
        entityManager.getTransaction().commit();
    }

    protected void persistWithinTransaction(EntityManager entityManager, Object ... object) {
        entityManager.getTransaction().begin();
        for (Object o : object) {
            entityManager.persist(o);
        }
        entityManager.getTransaction().commit();
    }

    protected EntityManager entityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        registeredEntityManagers.add(entityManager);
        return entityManager;
    }
}
