package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users_log_stats")
public class UserActivityLogger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_log_id", nullable = false)
    private Long userLogId;
    @Column(name = "remote_username", nullable = false)
    private String remoteUserName;
    @Column(name = "request_url")
    private String requestUrl;
    @Column(name = "protocol")
    private String protocol;

}
