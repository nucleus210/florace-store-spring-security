package com.nucleus.floracestore.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.security.Timestamp;

@Table(name = "persistent_logins")
public class PersistentLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "series", nullable = false)
    private String series;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "com/nucleus/floracestore/token", nullable = false)
    private String token;
    @Column(name = "last_used", nullable = false)
    private Timestamp lastUsed;
}
