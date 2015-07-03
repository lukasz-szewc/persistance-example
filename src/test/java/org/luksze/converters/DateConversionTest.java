package org.luksze.converters;

import org.junit.Assert;
import org.junit.Test;
import org.luksze.CleanDatabaseTest;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public class DateConversionTest extends CleanDatabaseTest {

    public DateConversionTest() {
        super("test-pu");
    }

    @Test
    public void objectWithDateShouldBePersisted() throws Exception {
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

    private DateAndTimeEntity objectIsFetchedAgainFromDatabase(DateAndTimeEntity dateAndTimeEntity) {
        EntityManager entityManager = entityManager();
        entityManager.getTransaction().begin();
        DateAndTimeEntity dateAndTimeEntity1 = entityManager.find(DateAndTimeEntity.class, dateAndTimeEntity.id());
        entityManager.getTransaction().commit();
        return dateAndTimeEntity1;

    }

    private DateAndTimeEntity persistedDateAndTimeObject() {
        DateAndTimeEntity dateAndTimeEntity = new DateAndTimeEntity();
        persistWithinTransaction(entityManager(), dateAndTimeEntity);
        return dateAndTimeEntity;
    }

    private void persist(DateAndTimeEntity dateAndTimeEntity, EntityManager entityManager) {
        entityManager.getTransaction().begin();
        entityManager.persist(dateAndTimeEntity);
        entityManager.getTransaction().commit();
    }
}
