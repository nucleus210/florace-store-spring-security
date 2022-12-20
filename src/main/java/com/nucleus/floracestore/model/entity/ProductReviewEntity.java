package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "product_reviews")
public class ProductReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_review_id")
    private Long productReviewId;
    @ManyToOne(targetEntity=ProductEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private ProductEntity product;

    @ManyToOne(optional = false, fetch=FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user__id", referencedColumnName = "user_id")
    private UserEntity user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "published_at")
    private Date publishedAt;

}
