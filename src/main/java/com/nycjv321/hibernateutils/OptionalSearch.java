package com.nycjv321.hibernateutils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Created by jvelasquez on 9/10/15.
 */
public class OptionalSearch {

    private final Search search;

    OptionalSearch(ObjectManager manager) {
        this.search = new Search(manager);
    }

    public synchronized <T> Optional<T> find(final Class<T> type, Map<String, Object> constraints) {
        return Optional.ofNullable(search.find(type, constraints, Collections.<String>emptyList()));
    }

}
