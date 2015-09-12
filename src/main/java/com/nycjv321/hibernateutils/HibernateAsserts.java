package com.nycjv321.hibernateutils;

import org.testng.Assert;

import java.util.Collection;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Contains asserts for hibernate comparision
 * Created by jvelasquez on 8/7/15.
 */
public class HibernateAsserts {

    /**
     * Assert that two Hibernating objects are equals. The comparisons are based on the object type and the object ids.
     * @param objectOne the first object to compare
     * @param objectTwo the second object to compare
     */
    public static void assertEquals(HibernatingObject objectOne, HibernatingObject objectTwo) {
        Assert.assertEquals(objectOne.getId(), objectTwo.getId());
        Assert.assertEquals(objectOne.getClass(), objectTwo.getClass());
    }

    /**
     * Assert that a Hibernating object has an id
     * @param object a hibernating object
     * @param description a description of the test
     */
    public static void assertHasId(HibernatingObject object, String description) {
        assertTrue(object.getId() > 0, description);
    }

    /**
     * Assert that the collection of {@code}T{@code} objects is not empty.  Assert that each object has an id
     * @param objects a collection of {@code}T{@code} Hibernating objects
     * @param description a description of the assertion
     * @param <T> a descdendent of {@code}HibernatingObject{@code}
     */
    public static <T extends HibernatingObject> void assertHasId(Collection<T> objects, String description) {
        assertFalse(objects.isEmpty(), description);

        for (HibernatingObject object : objects) {
            assertHasId(object, description);
        }
    }

    public static void assertNotEquals(HibernatingObject objectOne, HibernatingObject objectTwo) {
        Assert.assertTrue(objectOne.getId() != objectTwo.getId() || !objectOne.getClass().equals(objectTwo.getClass()));
    }
}
