package com.nycjv321.hibernateutils;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * This class should be the superclass of all classes that will be serialized with {@code}ObjectManager{@code}
 * Created by jvelasquez on 7/15/15.
 */
@MappedSuperclass
public abstract class HibernatingObject  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created;
    @UpdateTimestamp
    private Date modified;

    /**
     * @return the primary key id that identifies the hibernating object in its table
     */
    public int getId() {
        return id;
    }

}
