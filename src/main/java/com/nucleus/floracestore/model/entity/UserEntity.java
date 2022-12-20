package com.nucleus.floracestore.model.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_name", nullable = false, unique = true)
    private String username;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "salt_key")
    private String salt;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email_valid")
    private int emailValidation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created", nullable = false)
    private Date accountCreatedDate;
    @Column(name = "lock_date")
    private long lockDate;
    @Column(name = "active")
    private boolean setActive;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user__id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role__id", referencedColumnName = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    public void setActive(boolean b) {
    }
}