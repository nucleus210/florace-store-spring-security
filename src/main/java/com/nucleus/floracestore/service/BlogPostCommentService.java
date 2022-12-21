package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.BlogPostCommentServiceModel;

import java.util.List;

public interface BlogPostCommentService {
    BlogPostCommentServiceModel createBlogPostComment(BlogPostCommentServiceModel blogPostComment, Long blogPostId, String username);

    BlogPostCommentServiceModel updateBlogPostComment(BlogPostCommentServiceModel blogPostComment, String username);

    BlogPostCommentServiceModel deleteBlogPostCommentById(Long blogPostCommentId, String username);

    BlogPostCommentServiceModel getBlogPostCommentById(Long blogPostCommentId);

    BlogPostCommentServiceModel getBlogPostCommentByBlogPostIdAndUsername(Long blogPostId, String username);

    List<BlogPostCommentServiceModel> getAllBlogPostCommentsByUsername(String username);

    List<BlogPostCommentServiceModel> getAllBlogPostComments();
}
