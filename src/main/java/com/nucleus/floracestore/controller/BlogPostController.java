package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.BlogPostAssembler;
import com.nucleus.floracestore.model.dto.BlogPostDto;
import com.nucleus.floracestore.model.service.BlogPostServiceModel;
import com.nucleus.floracestore.model.view.BlogPostViewModel;
import com.nucleus.floracestore.service.BlogPostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class BlogPostController {
    private final ModelMapper modelMapper;
    private final BlogPostService blogPostService;
    private final BlogPostAssembler assembler;

    @Autowired
    public BlogPostController(ModelMapper modelMapper,
                              BlogPostService blogPostService,
                              BlogPostAssembler assembler) {
        this.modelMapper = modelMapper;
        this.blogPostService = blogPostService;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PostMapping("/blog-posts")
    public ResponseEntity<EntityModel<BlogPostViewModel>> createBlogPost(@RequestBody BlogPostDto model) {
        BlogPostServiceModel blogPostServiceModel =
                blogPostService.createBlogPost(modelMapper.map(model, BlogPostServiceModel.class), getCurrentLoggedUsername());
        log.info("BlogPostController: created blog post with id: " + blogPostServiceModel.getBlogPostId());
        return ResponseEntity
                .created(linkTo(methodOn(BlogPostController.class).createBlogPost(model)).toUri())
                .body(assembler.toModel(mapToView(blogPostServiceModel)));

    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @PutMapping("/blog-posts/{blogPostId}")
    public ResponseEntity<EntityModel<BlogPostViewModel>> updateBlogPost(@RequestBody BlogPostDto model, @PathVariable Long blogPostId) {
        BlogPostServiceModel blogPostServiceModel = blogPostService.updateBlogPostById(model.getBlogPostId(),
                modelMapper.map(model, BlogPostServiceModel.class),
                getCurrentLoggedUsername());
        log.info("BlogPostController: update blog post with id: " + blogPostServiceModel.getBlogPostId());
        return ResponseEntity
                .created(linkTo(methodOn(BlogPostController.class).updateBlogPost(model, blogPostId)).toUri())
                .body(assembler.toModel(mapToView(blogPostServiceModel)));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/blog-posts/{blogPostId}")
    public ResponseEntity<EntityModel<BlogPostViewModel>> deleteBlogPost(@PathVariable Long blogPostId) {
        BlogPostServiceModel blogPostServiceModel = blogPostService.deleteBlogPostById(blogPostId, getCurrentLoggedUsername());
        return ResponseEntity
                .created(linkTo(methodOn(BlogPostController.class).deleteBlogPost(blogPostId)).toUri())
                .body(assembler.toModel(mapToView(blogPostServiceModel)));
    }

    @GetMapping("/blog-posts")
    public ResponseEntity<CollectionModel<EntityModel<BlogPostViewModel>>> getAllBlogPosts() {
        List<EntityModel<BlogPostViewModel>> blogPosts = blogPostService.getAllBlogPosts().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(blogPosts, linkTo(methodOn(BlogPostController.class).getAllBlogPosts()).withSelfRel()));
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }

    private BlogPostViewModel mapToView(BlogPostServiceModel model) {
        return modelMapper.map(model, BlogPostViewModel.class);
    }
}