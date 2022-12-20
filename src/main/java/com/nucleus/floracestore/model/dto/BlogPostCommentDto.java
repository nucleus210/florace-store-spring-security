package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.BlogPostServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import lombok.Data;

import java.util.Date;

@Data
public class BlogPostCommentDto {
    private Long blogPostCommentId;
    private String comment;
    private BlogPostServiceModel blogPost;
    private UserServiceModel user;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedAt;
}
