package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.BlogPostCommentController;
import com.nucleus.floracestore.model.dto.BlogPostCommentDto;
import com.nucleus.floracestore.model.view.BlogPostCommentViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BlogPostCommentAssembler implements RepresentationModelAssembler<BlogPostCommentViewModel, EntityModel<BlogPostCommentViewModel>> {

    private ModelMapper modelMapper;

    @Override
    public EntityModel<BlogPostCommentViewModel> toModel(BlogPostCommentViewModel blogPostCommentViewModel) {

        return EntityModel.of(blogPostCommentViewModel,
                linkTo(methodOn(BlogPostCommentController.class)
                        .createBlogPostComment(blogPostCommentViewModel.getBlogPost().getBlogPostId(),
                                modelMapper.map(blogPostCommentViewModel, BlogPostCommentDto.class)))
                        .withRel("createBlogPostComment"),
                linkTo(methodOn(BlogPostCommentController.class)
                        .updateBlogPostComment(modelMapper.map(blogPostCommentViewModel, BlogPostCommentDto.class)))
                        .withRel("updateBlogPostComment"),
                linkTo(methodOn(BlogPostCommentController.class)
                        .deleteBlogPost(blogPostCommentViewModel.getBlogPostCommentId()))
                        .withRel("deleteBlogPostComment"),
                linkTo(methodOn(BlogPostCommentController.class).getAllBlogPostComments())
                        .withRel("getAllBlogPostsComment"));
    }
}