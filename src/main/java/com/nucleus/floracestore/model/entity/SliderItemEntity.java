package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "slider_items")
public class SliderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "slider_item_Id", nullable = false)
    private Long sliderItemId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "_resource_id", referencedColumnName = "resource_id")
    private StorageEntity storage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;
    @Column(name = "slide_item_title")
    private String sliderItemTitle;
    @Column(name = "slider_item_content")
    private String sliderItemContent;
}
