package org.luksze.criteria;

import org.junit.Before;
import org.junit.Test;
import org.luksze.CleanDatabaseTest;
import org.luksze.Person;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class SimpleCriteriaTest extends CleanDatabaseTest {

    public SimpleCriteriaTest() {
        super("test-pu");
    }

    @Before
    public void setUp() throws Exception {
        persistWithinTransaction(entityManager(), new Person("John", "Smith"));
        persistWithinTransaction(entityManager(), new Person("Ann", "Snow"));
        persistWithinTransaction(entityManager(), new Person("Emilia", "Sand"));
    }

    @Test
    public void test() throws Exception {
        EntityManager entityManager = entityManager();
        entityManager.getTransaction().begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        criteriaQuery.select(root);
        TypedQuery<Person> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Person> resultList = typedQuery.getResultList();
        System.out.println(resultList);
        entityManager.getTransaction().rollback();
    }

}
