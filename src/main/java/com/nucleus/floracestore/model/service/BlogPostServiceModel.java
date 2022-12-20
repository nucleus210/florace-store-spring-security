package com.nucleus.floracestore.model.service;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BlogPostServiceModel {
    private Long blogPostId;
    private String title;
    private String metaTitle;
    private String slug;
    private String summary;
    private String content;
    private UserServiceModel user;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedAt;
    private List<StorageServiceModel> storages;
}
