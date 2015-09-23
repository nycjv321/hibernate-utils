package com.nycjv321.hibernateutils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ObjectManager {
    /**
     * Specify where we are storing the hibernate config xml
     */
    private final static String DEFAULT_CONFIGURATION_FILE = "hibernate%s.cfg.xml";
    private SessionFactory sessionFactory;

    public ObjectManager(Class<?>... clazzes) {
        initSessionFactory(null, clazzes);
    }

    public ObjectManager(@NotNull ENV env, Class<?>... clazzes) {
        initSessionFactory(env, clazzes);
    }

    /**
     * Create a session factory to allow us to interact
     * with a table within a db that should reflect a POJO
     *
     * @param clazzes
     */
    private void initSessionFactory(ENV env, Class<?>... clazzes) {
        final Configuration configuration = createConfiguration(env);

        for (Class<?> clazz : clazzes) {
            configuration.addAnnotatedClass(clazz);
        }
        final ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().
                applySettings(
                        configuration.getProperties()
                ).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private Configuration createConfiguration(ENV env) {
        if (Objects.isNull(env)) {
            return new Configuration().configure(String.format(DEFAULT_CONFIGURATION_FILE, ""));
        } else {
            return new Configuration().configure(String.format(DEFAULT_CONFIGURATION_FILE, "-" + env));
        }
    }

    /**
     * Save a record to the database
     *
     * @param object an object that should be serialized to the database
     */
    public synchronized void saveOrUpdate(final Object object) {
        performUpdate(session -> session.saveOrUpdate(object));
    }

    /**
     * Create a new session with the database
     *
     * @return a brand new session
     */
    Session createSession() {
        return sessionFactory.openSession();
    }

    /**
     * Delete a record from the database
     *
     * @param object a record to delete
     */
    public void delete(final Object object) {
        performUpdate(session -> session.delete(object));
    }

    /**
     * See {@code}SessionFactory#close{@code}
     */
    public void terminate() {
        this.sessionFactory.close();
    }

    /**
     * Perform an update into the db
     *
     * @param consumer a database operation that does not return anything such as an update or an insert
     */
    public void performUpdate(final Consumer<Session> consumer) {
        final Session session = createSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            consumer.accept(session);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Perform a select in a table
     *
     * @param operation     The select operation
     * @param <OUTPUT_TYPE> the output type (ex:. A collection, or a single POJO)
     * @return the output specified by output type and found based on the inputted operation
     */
    public <OUTPUT_TYPE> OUTPUT_TYPE performSelect(final Function<Session, OUTPUT_TYPE> operation) {
        final Session session = createSession();
        session.beginTransaction();
        final OUTPUT_TYPE outputTYPE = operation.apply(session);
        session.getTransaction().commit();
        session.close();
        return outputTYPE;
    }

    public Search search() {
        return new Search(this);
    }

    public List<Object> createQuery(String string) {
        return performSelect(session -> session.createQuery(string).list());
    }

    public List<Object> createSQLQuery(String string) {
        return performSelect(session -> session.createSQLQuery(string).list());
    }

    public enum ENV {
        DEV("development"), STAGE("staging"), PROD("production");

        private String name;

        ENV(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}