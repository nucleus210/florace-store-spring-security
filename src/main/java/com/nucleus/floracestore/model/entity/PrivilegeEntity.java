package com.nucleus.floracestore.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "privileges")
public class PrivilegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "privileges_name")
    private String name;

    @JsonBackReference(value = "roles_privileges")
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
    private Set<RoleEntity> roles;
}