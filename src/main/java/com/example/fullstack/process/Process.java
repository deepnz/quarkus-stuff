package com.example.fullstack.process;

import com.example.fullstack.user.User;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "processes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "version","user_id","project_id"})
        })
public class Process extends PanacheEntity {
    @Column(nullable = false)
    public String name;
    @ManyToOne(optional = false)
    public User user;
    @ManyToOne(optional = false)
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    public String version;
    @Column(nullable = false, length = 100)
    public String description;
    @Column(nullable = false)
    public String status;
    @Column(nullable = false)
    public String type;
    @Column(nullable = false)
    public String project;
    @Column(nullable = false)
    public String updated;
    @Column(nullable = false)
    public ZonedDateTime created;
}
