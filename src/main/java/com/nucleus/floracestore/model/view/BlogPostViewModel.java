package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.service.StorageServiceModel;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class BlogPostViewModel {
    private Long blogPostId;
    private String title;
    private String metaTitle;
    private String slug;
    private String summary;
    private String content;
    private UserViewModel user;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedAt;
    private Set<StorageServiceModel> storages;
}
