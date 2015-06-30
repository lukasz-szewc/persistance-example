package org.luksze.cascade;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

import static org.junit.Assert.assertNotNull;

public class WithoutPersistCascadeTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-pu");
    }

    @Test
    public void everyPersistedObjectMustBePassedToPersist() throws Exception {
        //given
        Employee employee = newEntitiesEmployeeWithAddress();

        //when
        RollbackException rollbackException = employeeIsPersistedAndAddressIsNot(employee);

        //then
        rollbackExceptionIsThrown(rollbackException);
    }

    private void rollbackExceptionIsThrown(RollbackException rollbackException) {
        assertNotNull(rollbackException);
    }

    private RollbackException employeeIsPersistedAndAddressIsNot(Employee employee) {
        try {
            persistWithinTransaction(entityManager(), employee);
        } catch (RollbackException e) {
            return e;
        }
        return null;
    }

    private void persistWithinTransaction(EntityManager entityManager, Object object) {
        entityManager.getTransaction().begin();
        entityManager.persist(object);
        entityManager.getTransaction().commit();
    }

    private EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private Employee newEntitiesEmployeeWithAddress() {
        Employee employee = new Employee();
        Address address = new Address("NY");
        employee.assignAddress(address);
        return employee;
    }

    @After
    public void cleanUp() throws Exception {
        entityManagerFactory.close();
    }
}
