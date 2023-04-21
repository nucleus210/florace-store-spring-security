package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "users_log_stats")
public class UserActivityLogger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "user_log_id", nullable = false)
    private Long userLogId;
    @Column(name = "remote_username", nullable = false)
    private String remoteUserName;
    @Column(name = "request_url")
    private String requestUrl;
    @Column(name = "protocol")
    private String protocol;

}
