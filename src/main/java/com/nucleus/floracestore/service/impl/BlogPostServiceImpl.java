package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.BlogPostEntity;
import com.nucleus.floracestore.model.entity.StorageEntity;
import com.nucleus.floracestore.model.service.BlogPostServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.BlogPostRepository;
import com.nucleus.floracestore.service.BlogPostService;
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
public class BlogPostServiceImpl implements BlogPostService {
    private final ModelMapper modelMapper;
    private final BlogPostRepository blogPostRepository;
    private final UserService userService;

    @Autowired
    public BlogPostServiceImpl(ModelMapper modelMapper,
                               BlogPostRepository blogPostRepository,
                               UserService userService) {
        this.modelMapper = modelMapper;
        this.blogPostRepository = blogPostRepository;
        this.userService = userService;
    }

    @Override
    public BlogPostServiceModel createBlogPost(BlogPostServiceModel blogPostServiceModel, String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        blogPostServiceModel.setUser(userServiceModel);
        blogPostServiceModel.setCreatedAt(new Date());
        blogPostServiceModel.setPublishedAt(new Date());
        BlogPostEntity blogPostEntity = modelMapper.map(blogPostServiceModel, BlogPostEntity.class);
        return mapToService(blogPostRepository.save(blogPostEntity));
    }

    @Override
    public BlogPostServiceModel updateBlogPostById(Long blogPostId, BlogPostServiceModel blogPostServiceModel, String username) {
        BlogPostEntity blogPostEntity = blogPostRepository.findById(blogPostServiceModel.getBlogPostId())
                .orElseThrow(() -> new QueryRuntimeException("Could not find blog post with id " + blogPostServiceModel.getBlogPostId()));

        if (blogPostEntity.getUser().getUsername().equals(username)) {
            blogPostEntity = modelMapper.map(blogPostServiceModel, BlogPostEntity.class);

            return mapToService(blogPostRepository.save(blogPostEntity));
        } else {
            throw new QueryRuntimeException("Username have no permissions");
        }
    }

    @Override
    public BlogPostServiceModel deleteBlogPostById(Long blogPostId, String username) {
        BlogPostEntity blogPostEntity = blogPostRepository
                .findById(blogPostId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find blog post with id " + blogPostId));
        blogPostRepository.delete(blogPostEntity);
        return mapToService(blogPostEntity);
    }

    @Override
    public BlogPostServiceModel getBlogPostById(Long blogPostId) {
        BlogPostEntity blogPostEntity = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new QueryRuntimeException("Could not find blog post with id " + blogPostId));
        return mapToService(blogPostEntity);
    }

    @Override
    public BlogPostServiceModel getBlogPostByTitle(String title) {
        BlogPostEntity blogPostEntity = blogPostRepository.findByTitle(title)
                .orElseThrow(() -> new QueryRuntimeException("Could not find blog post with title " + title));
        return mapToService(blogPostEntity);
    }

    @Override
    public List<BlogPostServiceModel> getAllBlogPosts() {
        return blogPostRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostServiceModel> getAllBlogPostsByDatePeriod(String startDate, String endDate) {
        return blogPostRepository
                .findBlogPostsByDatePeriod(startDate, endDate)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    private BlogPostServiceModel mapToService(BlogPostEntity blogPostEntity) {
        return modelMapper.map(blogPostEntity, BlogPostServiceModel.class);
    }
}
