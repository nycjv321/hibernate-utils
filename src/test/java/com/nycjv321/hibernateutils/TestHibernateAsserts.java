package com.nycjv321.hibernateutils;

import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.nycjv321.hibernateutils.HibernateAsserts.assertEquals;
import static com.nycjv321.hibernateutils.HibernateAsserts.assertHasId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

/**
 * Created by jvelasquez on 8/10/15.
 */
public class TestHibernateAsserts {

    @Test(dependsOnMethods = "testAssertHasId")
    public void testAssertEquals() {
        assertEquals(createHibernatingObject(5), createHibernatingObject(5));
    }

    @Test(dependsOnMethods = "testAssertHasId")
    public void testAssertIdEqualsZero() {
        try {
            assertHasId(createHibernatingObject(0), "Expected instance of Object A to have an ID of at least 1");
        } catch (AssertionError a) {
            // expected
        }
    }

    @Test(dependsOnMethods = "testAssertEquals")
    public void testAssertEqualsFail() {
        try {
            assertEquals(createHibernatingObject(5), createHibernatingObject(1));
            fail("Expected for objects to be considered not equal");
        } catch (AssertionError a) {
            // expected
        }
    }

    @Test
    public void testAssertHasId() {
        assertHasId(createHibernatingObject(), "Expected instance of Object A to have an ID");
    }


    @Test(dependsOnMethods = "testAssertEquals")
    public void testAssertHaveId() {
        assertHasId(
                Arrays.asList(createHibernatingObject(), createHibernatingObject(), createHibernatingObject(), createHibernatingObject()),
                "Expected instance all instances to have id"
        );
    }

    @Test
    public void testNotEquals() {
        HibernateAsserts.assertNotEquals(createHibernatingObject(5), createHibernatingObject(1));
    }

    @Test
    public void testNotEqualsFails() {
        try {
            HibernateAsserts.assertNotEquals(createHibernatingObject(5), createHibernatingObject(5));
            fail("Expected for objects to be considered equal");
        } catch (AssertionError a) {
            // Expected
        }
    }

    private HibernatingObject createHibernatingObject() {
        return createHibernatingObject(RandomUtils.nextInt(1, 500));
    }

    private HibernatingObject createHibernatingObject(int id) {
        HibernatingObject hibernatingObject = mock(HibernatingObject.class);
        when(hibernatingObject.getId()).thenReturn(id);
        return hibernatingObject;
    }

}
