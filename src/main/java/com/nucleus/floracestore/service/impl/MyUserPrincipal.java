package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.model.entity.PrivilegeEntity;
import com.nucleus.floracestore.model.entity.RoleEntity;
import com.nucleus.floracestore.model.service.UserServiceModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserPrincipal implements UserDetails {
    private final UserServiceModel userServiceModel;

    public MyUserPrincipal(UserServiceModel userServiceModel) {
        this.userServiceModel = userServiceModel;
    }

    public Long getId() {
        return userServiceModel.getUserId();
    }

    @Override
    public String getUsername() {
        return userServiceModel.getUsername();
    }

    @Override
    public String getPassword() {
        return userServiceModel.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthorities(userServiceModel.getRoles());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<RoleEntity> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<RoleEntity> roles) {
        List<String> privileges = new ArrayList<>();
        List<PrivilegeEntity> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            privileges.add("ROLE_" + role.getRoleName());
            collection.addAll(role.getPrivileges());
        }
        for (PrivilegeEntity item : collection) {
            privileges.add(item.getName());
        }
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

    public UserServiceModel getAppUser() {
        return userServiceModel;
    }

    public String getUserIdentifier() {
        return getUsername();
    }
}
