package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.AnswerController;
import com.nucleus.floracestore.model.view.AnswerViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnswerAssembler implements RepresentationModelAssembler<AnswerViewModel, EntityModel<AnswerViewModel>> {

    @Override
    public EntityModel<AnswerViewModel> toModel(AnswerViewModel answerViewModel) {

        return EntityModel.of(answerViewModel,
                linkTo(methodOn(AnswerController.class).getAnswerById(answerViewModel.getAnswerId())).withRel("getAnswerById"),
                linkTo(methodOn(AnswerController.class).deleteAnswer(answerViewModel.getAnswerId())).withRel("deleteQuestion"),
                linkTo(methodOn(AnswerController.class).getAllAnswersByUsername()).withRel("getAllQuestionByUsername"),
                linkTo(methodOn(AnswerController.class).getAllAnswersByQuestionId(answerViewModel.getQuestion().getQuestionId())).withRel("getQuestions"));
    }
}