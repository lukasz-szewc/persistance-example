package org.luksze;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FirstTest {
    @Test
    public void testName() throws Exception {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hsqldb-test-pu");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Person person = new Person("john", "smith");
        entityManager.persist(person);

        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        Person person1 = entityManager.find(Person.class, 1l);

        entityManager.getTransaction().commit();
    }
}
