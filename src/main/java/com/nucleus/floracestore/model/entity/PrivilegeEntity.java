package com.nucleus.floracestore.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "privileges")
public class PrivilegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Long id;

    @Column(name = "privileges_name")
    private String name;

    @JsonBackReference(value = "roles_privileges")
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.EAGER)
    private Set<RoleEntity> roles;
}