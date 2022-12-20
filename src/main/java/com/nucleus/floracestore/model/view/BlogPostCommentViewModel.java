package com.nucleus.floracestore.model.view;

import lombok.Data;

import java.util.Date;

@Data
public class BlogPostCommentViewModel {
    private Long blogPostCommentId;
    private String comment;
    private BlogPostViewModel blogPost;
    private UserViewModel user;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedAt;
}
