package org.luksze;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class FirstTest {

    private static final Logger logger = LogManager.getLogger(FirstTest.class);
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("hsqldb-test-pu");
    }

    @Test
    public void testName() throws Exception {

        LocalTime startTest = LocalTime.now();


        Duration from = Duration.between(startTest, LocalTime.now());
        logger.info("Entity manager factory created in: " + from.toMillis() + " milliseconds");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Person person = new Person("john", "smith");
        entityManager.persist(person);

        entityManager.getTransaction().commit();

        EntityManager manager = entityManagerFactory.createEntityManager();

        manager.getTransaction().begin();
        Person person1 = manager.find(Person.class, 1l);

        Assert.assertEquals(person.firstName(), person1.firstName());
        Assert.assertEquals(person.surname(), person1.surname());
        manager.getTransaction().commit();

        Date date = new Date();
        date.setLocalDate(LocalDate.now());

        entityManager.getTransaction().begin();
        entityManager.persist(date);
        entityManager.getTransaction().commit();
    }

    @After
    public void cleanUp() throws Exception {
        entityManagerFactory.close();
    }
}
