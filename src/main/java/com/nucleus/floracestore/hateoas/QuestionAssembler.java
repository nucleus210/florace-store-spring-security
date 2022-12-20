package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.QuestionController;
import com.nucleus.floracestore.model.view.QuestionViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class QuestionAssembler implements RepresentationModelAssembler<QuestionViewModel, EntityModel<QuestionViewModel>> {

    @Override
    public EntityModel<QuestionViewModel> toModel(QuestionViewModel questionViewModel) {

        return EntityModel.of(questionViewModel,
                linkTo(methodOn(QuestionController.class).getQuestionById(questionViewModel.getQuestionId())).withRel("getQuestionById"),
                linkTo(methodOn(QuestionController.class).deleteQuestion(questionViewModel.getQuestionId())).withRel("deleteQuestion"),
                linkTo(methodOn(QuestionController.class).getAllQuestionByUsername()).withRel("getAllQuestionByUsername"),
                linkTo(methodOn(QuestionController.class).getAllQuestionsByProductId(questionViewModel.getProduct().getProductId())).withRel("getQuestions"));
    }
}