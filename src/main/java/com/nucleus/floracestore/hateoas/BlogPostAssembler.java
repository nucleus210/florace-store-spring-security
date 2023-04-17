package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.BlogPostController;
import com.nucleus.floracestore.model.dto.BlogPostDto;
import com.nucleus.floracestore.model.view.BlogPostViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BlogPostAssembler implements RepresentationModelAssembler<BlogPostViewModel, EntityModel<BlogPostViewModel>> {

    @Override
    public EntityModel<BlogPostViewModel> toModel(BlogPostViewModel blogPostViewModel) {

        return EntityModel.of(blogPostViewModel,
                linkTo(methodOn(BlogPostController.class).deleteBlogPost(blogPostViewModel.getBlogPostId())).withRel("deleteBlogPost"),
                linkTo(methodOn(BlogPostController.class).getAllBlogPosts()).withRel("getAllBlogPosts"));
    }
}