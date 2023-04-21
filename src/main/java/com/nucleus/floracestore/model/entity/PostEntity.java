package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "post_reviews")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_target_id", referencedColumnName = "product_id")
    private ProductEntity product;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "meta_title")
    private String metaTitle;

    @Column(name = "slug")
    private String slug;

    @Column(name = "summary")
    private String summary;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "published_at", nullable = false)
    private Date publishedAt;

    @Column(name = "content", nullable = false)
    private String content;


}
