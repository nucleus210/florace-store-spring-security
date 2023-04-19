package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "resources")
public class StorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "size")
    private Long size;

}
