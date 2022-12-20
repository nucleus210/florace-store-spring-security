package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.BlogPostServiceModel;

import java.util.List;

public interface BlogPostService {
    BlogPostServiceModel createBlogPost(BlogPostServiceModel blogPostServiceModel, String username);
    BlogPostServiceModel updateBlogPostById(Long blogPostId, BlogPostServiceModel blogPostServiceModel, String username);
    BlogPostServiceModel deleteBlogPostById(Long blogPostId, String username);

    BlogPostServiceModel getBlogPostById(Long blogPostId);

    BlogPostServiceModel getBlogPostByTitle(String title);
    List<BlogPostServiceModel> getAllBlogPosts();
    List<BlogPostServiceModel> getAllBlogPostsByDatePeriod(String startDate, String endDate);
}
