package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
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
                    name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "_role_id", referencedColumnName = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

//    @OneToMany(mappedBy = "user")
//    private List<Token> tokens;


    public void setActive(boolean b) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<RoleEntity> roles) {
        List<String> privileges = new ArrayList<>();
        List<PrivilegeEntity> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            privileges.add("ROLE_" + role.getRoleName());
            collection.addAll(role.getPrivileges());
        }
//        for (PrivilegeEntity item : collection) {
//            privileges.add(item.getName());
//        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}