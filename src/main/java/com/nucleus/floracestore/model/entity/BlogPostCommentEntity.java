package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "blog_post_comments")
public class BlogPostCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "blog_posts_comment_id")
    private Long blogPostCommentId;
    @Column(name = "blog_post_comment_content")
    private String comment;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "blog_post__id", referencedColumnName = "blog_post_id")
    private BlogPostEntity blogPost;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
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
}
