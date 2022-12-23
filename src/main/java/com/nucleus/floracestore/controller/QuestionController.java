package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.QuestionAssembler;
import com.nucleus.floracestore.model.dto.QuestionDto;
import com.nucleus.floracestore.model.service.QuestionServiceModel;
import com.nucleus.floracestore.model.view.QuestionViewModel;
import com.nucleus.floracestore.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class QuestionController {

    private final ModelMapper modelMapper;
    private final QuestionService questionService;
    private final QuestionAssembler assembler;

    @Autowired
    public QuestionController(ModelMapper modelMapper,
                              QuestionService questionService,
                              QuestionAssembler assembler) {
        this.modelMapper = modelMapper;
        this.questionService = questionService;
        this.assembler = assembler;
    }

    @PostMapping("/questions")
    public ResponseEntity<EntityModel<QuestionViewModel>> writeQuestion(@RequestBody QuestionDto model) {

        QuestionServiceModel questionServiceModel = questionService.createQuestion(mapToService(model), model.getProduct().getProductId(), getCurrentLoggedUsername());
        return ResponseEntity
                .created(linkTo(methodOn(QuestionController.class).writeQuestion(model)).toUri())
                .body(assembler.toModel(mapView(questionServiceModel)));
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<EntityModel<QuestionViewModel>> getQuestionById(@PathVariable Long questionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapView(questionService.getQuestionById(questionId))));
    }

    @GetMapping("/questions/users/{username}")
    public ResponseEntity<CollectionModel<EntityModel<QuestionViewModel>>> getAllQuestionByUsername() {
        List<EntityModel<QuestionViewModel>> questions = questionService.getAllQuestionByUsername(getCurrentLoggedUsername()).stream()
                .map(entity -> assembler.toModel(mapView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(questions, linkTo(methodOn(QuestionController.class).getAllQuestionByUsername()).withSelfRel()));
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<EntityModel<QuestionViewModel>> updateQuestion(@RequestBody QuestionDto model, @PathVariable Long questionId) {

        QuestionServiceModel questionServiceModel = questionService.updateQuestion(mapToService(model), questionId);
        return ResponseEntity
                .created(linkTo(methodOn(QuestionController.class).updateQuestion(model, questionId)).toUri())
                .body(assembler.toModel(mapView(questionServiceModel)));
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<EntityModel<QuestionViewModel>> deleteQuestion(@PathVariable Long questionId) {
        EntityModel<QuestionViewModel> questionViewModel = assembler.toModel(mapView(questionService.deleteQuestionById(questionId)));
        return ResponseEntity.status(HttpStatus.OK).body(questionViewModel);
    }

    @GetMapping("/questions/products/{productId}")
    public ResponseEntity<CollectionModel<EntityModel<QuestionViewModel>>> getAllQuestionsByProductId(@PathVariable Long productId) {
        List<EntityModel<QuestionViewModel>> questions = questionService.getAllQuestionsByProductId(productId).stream()
                .map(entity -> assembler.toModel(mapView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(questions, linkTo(methodOn(QuestionController.class).getAllQuestionsByProductId(productId)).withSelfRel()));
    }

    private QuestionViewModel mapView(QuestionServiceModel model) {
        return modelMapper.map(model, QuestionViewModel.class);
    }

    private QuestionServiceModel mapToService(QuestionDto model) {
        return modelMapper.map(model, QuestionServiceModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}
