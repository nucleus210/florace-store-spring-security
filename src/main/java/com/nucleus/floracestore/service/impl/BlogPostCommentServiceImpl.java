package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.BlogPostCommentEntity;
import com.nucleus.floracestore.model.entity.BlogPostEntity;
import com.nucleus.floracestore.model.entity.UserEntity;
import com.nucleus.floracestore.model.service.BlogPostCommentServiceModel;
import com.nucleus.floracestore.repository.BlogPostCommentRepository;
import com.nucleus.floracestore.service.BlogPostCommentService;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlogPostCommentServiceImpl implements BlogPostCommentService {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final BlogPostCommentRepository blogPostCommentRepository;

    @Autowired
    public BlogPostCommentServiceImpl(ModelMapper modelMapper, UserService userService, BlogPostCommentRepository blogPostCommentRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.blogPostCommentRepository = blogPostCommentRepository;
    }

    @Override
    public BlogPostCommentServiceModel createBlogPostComment(BlogPostCommentServiceModel blogPostComment,
                                                             Long blogPostId,
                                                             String username) {
        BlogPostCommentEntity blogPostCommentEntity = modelMapper.map(blogPostComment, BlogPostCommentEntity.class);
        return mapToService(blogPostCommentRepository.save(blogPostCommentEntity));

    }

    @Override
    public BlogPostCommentServiceModel updateBlogPostComment(BlogPostCommentServiceModel blogPostComment,
                                                             String username) {
        BlogPostCommentEntity blogPostCommentEntity = blogPostCommentRepository.findById(blogPostComment.getBlogPostCommentId())
                .orElseThrow(() -> new QueryRuntimeException("Could not find blog post comment with id " + blogPostComment.getBlogPostCommentId()));;
        blogPostCommentEntity.setComment(blogPostComment.getComment());
        blogPostCommentEntity.setBlogPost(modelMapper.map(blogPostComment.getBlogPost(), BlogPostEntity.class));
        blogPostCommentEntity.setUser(modelMapper.map(blogPostComment.getUser(), UserEntity.class));
        blogPostCommentEntity.setCreatedAt(blogPostComment.getCreatedAt());
        blogPostCommentEntity.setUpdatedAt(new Date());
        blogPostCommentEntity.setPublishedAt(blogPostComment.getPublishedAt());
        return mapToService(blogPostCommentRepository.save(blogPostCommentEntity));
    }

    @Override
    public BlogPostCommentServiceModel deleteBlogPostCommentById(Long blogPostCommentId, String username) {
        BlogPostCommentEntity blogPostCommentEntity = blogPostCommentRepository
                .findById(blogPostCommentId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find blog post comment with id " + blogPostCommentId));
        blogPostCommentRepository.delete(blogPostCommentEntity);
        return mapToService(blogPostCommentEntity);


    }

    @Override
    public BlogPostCommentServiceModel getBlogPostCommentById(Long blogPostCommentId) {
        BlogPostCommentEntity blogPostCommentEntity = blogPostCommentRepository
                .findById(blogPostCommentId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find blog post comment with id " + blogPostCommentId));
        return mapToService(blogPostCommentEntity);
    }

    @Override
    public BlogPostCommentServiceModel getBlogPostCommentByBlogPostIdAndUsername(Long blogPostId, String username) {
        BlogPostCommentEntity blogPostCommentEntity = blogPostCommentRepository
                .findBlogPostCommentByBlogPostIdAndUsername(blogPostId, username)
                .orElseThrow(() -> new QueryRuntimeException(
                        String.format("Could not find blog post with id %d and username %s", blogPostId, username)));
        return mapToService(blogPostCommentEntity);
    }

    @Override
    public List<BlogPostCommentServiceModel> getAllBlogPostCommentsByUsername(String username) {
        return blogPostCommentRepository
                .findAllBlogPostCommentsByUsername(username)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostCommentServiceModel> getAllBlogPostComments() {
        return blogPostCommentRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }
    private BlogPostCommentServiceModel mapToService(BlogPostCommentEntity blogPostCommentEntity) {
        return modelMapper.map(blogPostCommentEntity, BlogPostCommentServiceModel.class);
    }
}
