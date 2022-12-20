package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.LikeController;
import com.nucleus.floracestore.model.view.LikeViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LikeAssembler implements RepresentationModelAssembler<LikeViewModel, EntityModel<LikeViewModel>> {

    @Override
    public EntityModel<LikeViewModel> toModel(LikeViewModel likeViewModel) {

        return EntityModel.of(likeViewModel,
                linkTo(methodOn(LikeController.class).likeQuestion(likeViewModel.getLikeId())).withRel("likeQuestion"),
                linkTo(methodOn(LikeController.class).deleteLikeById(likeViewModel.getLikeId())).withRel("deleteLikeById"),
                linkTo(methodOn(LikeController.class).getAllLikesByQuestionId(likeViewModel.getQuestion().getQuestionId())).withRel("getAllLikesByQuestionId"));
    }
}