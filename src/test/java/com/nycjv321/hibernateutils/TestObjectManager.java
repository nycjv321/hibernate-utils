package com.nycjv321.hibernateutils;

import com.nycjv321.reflectiveutilities.ReflectiveUtilities;
import org.hibernate.Session;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * Created by jvelasquez on 8/10/15.
 */
public class TestObjectManager {

    private ObjectManager manager;

    @Test
    public void testInitializeConfigNoEnvironment() {
        manager = new ObjectManager(HibernatingObject.class);
        Session session = getSession(manager);
        assertNotNull(session);
        session.close();
    }

    @Test
    public void testInitializeConfigEnvironnmentEnvironment() {
        manager = new ObjectManager(ObjectManager.ENV.STAGE, HibernatingObject.class);
        Session session = getSession(manager);
        assertNotNull(session);
        session.close();
    }

    private Session getSession(ObjectManager manager) {
        return (Session) ReflectiveUtilities.invokeMethod(manager, "createSession");
    }

    @AfterMethod
    public void afterMethod() {
        manager.terminate();
    }

}
