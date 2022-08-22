package com.nucleus.floracestore.model.entity;

import javax.persistence.*;
import java.security.Timestamp;

@Table(name = "persistent_logins")
public class PersistentLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "series", nullable = false)
    private String series;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "last_used", nullable = false)
    private Timestamp lastUsed;
}
