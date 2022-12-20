package com.nucleus.floracestore.model.service;

import lombok.Data;

import java.util.Date;

@Data
public class BlogPostCommentServiceModel {
    private Long blogPostCommentId;
    private String comment;
    private BlogPostServiceModel blogPost;
    private UserServiceModel user;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedAt;
}
