package org.luksze.cascade;

import org.junit.Test;
import org.luksze.CleanDatabaseTest;

import javax.persistence.RollbackException;

import static org.junit.Assert.assertNotNull;

public class WithoutPersistCascadeTest extends CleanDatabaseTest {

    public WithoutPersistCascadeTest() {
        super("cascade-pu");
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

    private Employee newEntitiesEmployeeWithAddress() {
        Employee employee = new Employee();
        Address address = new Address("NY");
        employee.assignAddress(address);
        return employee;
    }

}
