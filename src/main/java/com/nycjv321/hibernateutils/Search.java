package com.nycjv321.hibernateutils;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jvelasquez on 9/10/15.
 */
public class Search {
    private final ObjectManager manager;

    Search(ObjectManager manager) {
        this.manager = manager;
    }

    public OptionalSearch optional() {
        return new OptionalSearch(manager);
    }

    public synchronized <R> R find(final Class type, Map<String, Object> constraints) {
        return find(type, constraints, new ArrayList<>());
    }

    /**
     * Find a record in the database
     *
     * @param type the class that represents the record
     * @param id   the records primary key
     * @param <R>  The record return type as a POJO class
     * @return a record that matches the specified ID represented by the specified type
     */
    @SuppressWarnings("unchecked")
    public <R> R find(final Class type, final int id) {
        return (R) manager.performSelect(session -> session.load(type, id));
    }

    @SuppressWarnings("unchecked")
    public synchronized <R> R find(final Class type, Map<String, Object> constraints, List<String> nulls) {
        Session session = manager.createSession();
        Criteria criteria = session.createCriteria(type);
        criteria.add(Restrictions.allEq(constraints));
        for (String key : nulls) {
            criteria.add(Restrictions.isNull(key));
        }
        Iterator iterator = criteria.list().iterator();
        R result;
        if (iterator.hasNext()) {
            result = (R) iterator.next();
        } else {
            result = null;
        }
        session.close();
        return result;
    }

    /**
     * Create a list representing all entries in a table
     *
     * @param tableName the table to search
     * @param <T>       the type of entry
     * @return a list of specified type
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(final String tableName) {
        return manager.performSelect(session -> session.createQuery(String.format("from %s", tableName)).list());
    }

}