package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.StorageServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BlogPostDto {
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
