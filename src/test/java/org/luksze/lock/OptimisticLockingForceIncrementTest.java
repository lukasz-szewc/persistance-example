package org.luksze.lock;

import org.junit.Test;
import org.luksze.CleanDatabaseTest;
import org.luksze.VersionedPerson;
import org.luksze.VersionedPersonWrapper;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import static org.junit.Assert.assertTrue;

public class OptimisticLockingForceIncrementTest extends CleanDatabaseTest {
    public OptimisticLockingForceIncrementTest() {
        super("test-pu");
    }

    @Test
    public void lackOfLockingWillNotIncrementRootVersion() throws Exception {
        //given
        VersionedPersonWrapper versionedPersonWrapper = rootAndChildObjectInDatabase();

        //when
        VersionedPersonWrapper wrapperAfterUpdate = childIsModifiedWithLockMode(LockModeType.NONE);

        //then
        assertTrue(wrapperAfterUpdate.versionIsTheSame(versionedPersonWrapper));
        assertTrue(wrapperAfterUpdate.childObjectVersionHasBeenIncrementedByOne(versionedPersonWrapper));
    }

    @Test
    public void justOptimisticLockingWillNotIncrementRootVersion() throws Exception {
        //given
        VersionedPersonWrapper versionedPersonWrapper = rootAndChildObjectInDatabase();

        //when
        VersionedPersonWrapper wrapperAfterUpdate = childIsModifiedWithLockMode(LockModeType.OPTIMISTIC);

        //then
        assertTrue(wrapperAfterUpdate.versionIsTheSame(versionedPersonWrapper));
        assertTrue(wrapperAfterUpdate.childObjectVersionHasBeenIncrementedByOne(versionedPersonWrapper));
    }

    @Test
    public void optimisticLockForceWillUpdateRootObjectVersionAsWell() throws Exception {
        //given
        VersionedPersonWrapper versionedPersonWrapper = rootAndChildObjectInDatabase();

        //when
        VersionedPersonWrapper wrapperAfterUpdate = childIsModifiedWithLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        //then
        assertTrue(wrapperAfterUpdate.versionHasBeenIncrementedByOne(versionedPersonWrapper));
        assertTrue(wrapperAfterUpdate.childObjectVersionHasBeenIncrementedByOne(versionedPersonWrapper));
    }

    private VersionedPersonWrapper childIsModifiedWithLockMode(LockModeType lockMode) {
        EntityManager firstEntityManager = entityManager();
        firstEntityManager.getTransaction().begin();
        VersionedPersonWrapper wrapper = firstEntityManager.find(VersionedPersonWrapper.class, 1l, lockMode);
        wrapper.getVersionedPerson().changeFirstName("William");
        firstEntityManager.getTransaction().commit();
        return wrapper;
    }

    private VersionedPersonWrapper rootAndChildObjectInDatabase() {
        EntityManager entityManager = entityManager();
        entityManager.getTransaction().begin();
        VersionedPerson versionedPerson = new VersionedPerson("John", "Smith");
        VersionedPersonWrapper versionedPersonWrapper = new VersionedPersonWrapper(versionedPerson);
        entityManager.persist(versionedPersonWrapper);
        entityManager.getTransaction().commit();
        return versionedPersonWrapper;
    }
}
