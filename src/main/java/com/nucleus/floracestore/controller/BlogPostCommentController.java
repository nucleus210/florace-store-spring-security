package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.BlogPostCommentAssembler;
import com.nucleus.floracestore.model.dto.BlogPostCommentDto;
import com.nucleus.floracestore.model.service.BlogPostCommentServiceModel;
import com.nucleus.floracestore.model.view.BlogPostCommentViewModel;
import com.nucleus.floracestore.service.BlogPostCommentService;
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
public class  BlogPostCommentController {
    private final ModelMapper modelMapper;
    private final BlogPostCommentService blogPostCommentService;
    private final BlogPostCommentAssembler assembler;

    @Autowired
    public BlogPostCommentController(ModelMapper modelMapper,
                                     BlogPostCommentService blogPostCommentService,
                                     BlogPostCommentAssembler assembler) {
        this.modelMapper = modelMapper;
        this.blogPostCommentService = blogPostCommentService;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping("/blog-posts/{blogPostId}/comments")
    public ResponseEntity<EntityModel<BlogPostCommentViewModel>> createBlogPostComment(@PathVariable Long blogPostId,
                                                                                       @RequestBody BlogPostCommentDto model) {
        BlogPostCommentServiceModel blogPostCommentServiceModel =
                blogPostCommentService.createBlogPostComment(modelMapper.map(model, BlogPostCommentServiceModel.class),
                        blogPostId,
                        getCurrentLoggedUsername());
        log.info("BlogPostCommentController: created blog post comment with id: " +
                blogPostCommentServiceModel.getBlogPostCommentId());
        return ResponseEntity
                .created(linkTo(methodOn(BlogPostCommentController.class).createBlogPostComment(blogPostId, model)).toUri())
                .body(assembler.toModel(mapToView(blogPostCommentServiceModel)));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PutMapping("/blog-posts/comments/{id}")
    public ResponseEntity<EntityModel<BlogPostCommentViewModel>> updateBlogPostComment(@RequestBody BlogPostCommentDto model) {
        BlogPostCommentServiceModel blogPostCommentServiceModel = blogPostCommentService.updateBlogPostComment(
                modelMapper.map(model, BlogPostCommentServiceModel.class),
                getCurrentLoggedUsername());
        log.info("BlogPostCommentController: update blog post comment with id: "
                + blogPostCommentServiceModel.getBlogPostCommentId());
        return ResponseEntity
                .created(linkTo(methodOn(BlogPostCommentController.class).updateBlogPostComment(model)).toUri())
                .body(assembler.toModel(mapToView(blogPostCommentServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/blog-posts/comments/{id}")
    public ResponseEntity<EntityModel<BlogPostCommentViewModel>> deleteBlogPost(@PathVariable Long blogPostId) {
        BlogPostCommentServiceModel blogPostCommentServiceModel =
                blogPostCommentService.deleteBlogPostCommentById(blogPostId, getCurrentLoggedUsername());
        return ResponseEntity
                .created(linkTo(methodOn(BlogPostCommentController.class).deleteBlogPost(blogPostId)).toUri())
                .body(assembler.toModel(mapToView(blogPostCommentServiceModel)));
    }

    @GetMapping("/blog-posts/comments")
    public ResponseEntity<CollectionModel<EntityModel<BlogPostCommentViewModel>>> getAllBlogPostComments() {
        List<EntityModel<BlogPostCommentViewModel>> sliderItems = blogPostCommentService.getAllBlogPostComments().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(sliderItems, linkTo(methodOn(BlogPostCommentController.class)
                        .getAllBlogPostComments())
                        .withSelfRel()));
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }

    private BlogPostCommentViewModel mapToView(BlogPostCommentServiceModel model) {
        return modelMapper.map(model, BlogPostCommentViewModel.class);
    }
}
