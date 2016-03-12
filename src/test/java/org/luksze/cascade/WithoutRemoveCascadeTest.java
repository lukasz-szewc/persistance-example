package org.luksze.cascade;

import org.junit.Assert;
import org.junit.Test;
import org.luksze.CleanDatabaseTest;

import javax.persistence.EntityManager;

public class WithoutRemoveCascadeTest extends CleanDatabaseTest {

    public WithoutRemoveCascadeTest() {
        super("cascade-pu");
    }

    @Test
    public void onlyParentObjectWillBeRemovedWhenNoCascadeRemoveProvided() throws Exception {
        //given
        Employee employee = twoObjectsInDatabaseWithNoCascadeRemoveRelation();

        //when
        removeEmployee(employee);

        //then
        Assert.assertNull(employeeHasBeenRemoved(employee));
        Assert.assertNotNull(adressIsStillInDatabase(employee.getAddress()));
    }

    private Object adressIsStillInDatabase(Address address) {
        return entityManager().find(Address.class, address.id());
    }

    private Employee employeeHasBeenRemoved(Employee givenEmployee) {
        return entityManager().find(Employee.class, givenEmployee.id());
    }

    private void removeEmployee(Employee detachedEmployee) {
        EntityManager entityManager = entityManager();
        entityManager.getTransaction().begin();
        Employee employee = entityManager.find(Employee.class, detachedEmployee.id());
        entityManager.remove(employee);
        entityManager.getTransaction().commit();
    }

    private Employee twoObjectsInDatabaseWithNoCascadeRemoveRelation() {
        Employee employee = new Employee();
        Address london = new Address("London");
        employee.assignAddress(london);
        persistWithinTransaction(entityManager(), london, employee);
        return employee;
    }

}
