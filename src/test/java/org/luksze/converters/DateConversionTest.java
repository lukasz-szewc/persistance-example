package org.luksze.converters;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.luksze.config.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DateConversionTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-pu", new Configuration());
    }

    @Test
    public void testName() throws Exception {
        //given
        DateAndTimeEntity persisted = persistedDateAndTimeObject();

        //when
        DateAndTimeEntity fetched = objectIsFetchedAgainFromDatabase(persisted);

        //when
        Assert.assertEquals(fetched.id(), persisted.id());
        Assert.assertEquals(fetched.firstLocalDateTime(), persisted.firstLocalDateTime());
        Assert.assertEquals(fetched.secondLocalDateTime(), persisted.secondLocalDateTime());
        Assert.assertEquals(fetched.thirdLocalDateTime(), persisted.thirdLocalDateTime());
    }

    @After
    public void cleanUp() throws Exception {
        entityManagerFactory.close();
    }

    private DateAndTimeEntity objectIsFetchedAgainFromDatabase(DateAndTimeEntity dateAndTimeEntity) {
        return entityManagerFactory.createEntityManager().
                find(DateAndTimeEntity.class, dateAndTimeEntity.id());

    }

    private DateAndTimeEntity persistedDateAndTimeObject() {
        DateAndTimeEntity dateAndTimeEntity = new DateAndTimeEntity();
        persist(dateAndTimeEntity, entityManagerFactory.createEntityManager());
        return dateAndTimeEntity;
    }

    private void persist(DateAndTimeEntity dateAndTimeEntity, EntityManager entityManager) {
        entityManager.getTransaction().begin();
        entityManager.persist(dateAndTimeEntity);
        entityManager.getTransaction().commit();
    }
}
