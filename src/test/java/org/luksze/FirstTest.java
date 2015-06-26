package org.luksze;

import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Duration;
import java.time.LocalTime;

public class FirstTest {
    @Test
    public void testName() throws Exception {

        LocalTime startTest = LocalTime.now();

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hsqldb-test-pu");

        Duration from = Duration.between(startTest, LocalTime.now());
        System.out.println("Seconds" + from.getSeconds() + " nanoseconds :" + from.toMillis() );

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Person person = new Person("john", "smith");
        entityManager.persist(person);

        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        Person person1 = entityManager.find(Person.class, 1l);

        Assert.assertEquals(person.getName(), person1.getName());
        Assert.assertEquals(person.getSurname(), person1.getSurname());
        entityManager.getTransaction().commit();
    }
}
