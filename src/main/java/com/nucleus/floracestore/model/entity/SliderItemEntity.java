package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "slider_items")
public class SliderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "slider_item_Id", nullable = false)
    private Long sliderItemId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "resources__id", referencedColumnName = "resources_id")
    private StorageEntity storage;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user__id", referencedColumnName = "user_id")
    private UserEntity user;
    @Column(name = "slide_item_title")
    private String sliderItemTitle;
    @Column(name = "slider_item_content")
    private String sliderItemContent;
}
