package com.nucleus.floracestore.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "searches")
public class SearchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long searchId;
    @Column(name = "search_id", nullable = false)
    private String name;
    @Column(name = "alias", nullable = false)
    private String alias;
    @Column(name = "short_description", nullable = false)
    private String shortDescription;
    @Column(name = "full_description", nullable = false)
    private String fullDescription;
}
