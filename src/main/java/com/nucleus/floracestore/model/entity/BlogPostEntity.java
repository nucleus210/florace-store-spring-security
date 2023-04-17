package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "blog_posts")
public class BlogPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "blog_post_id")
    private Long blogPostId;
    @Column(name = "title", nullable = false, unique = true)
    private String title;
    @Column(name = "meta_title", nullable = false, unique = true)
    private String metaTitle;
    @Column(name = "slug", nullable = false)
    private String slug;
    @Column(name = "summary", columnDefinition = "TEXT", nullable = false)
    private String summary;
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "user_id")
    private UserEntity user;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "updated_at")
    private Date updatedAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "published_at")
    private Date publishedAt;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<StorageEntity> storages;
}
