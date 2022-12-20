package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.AnswerAssembler;
import com.nucleus.floracestore.model.dto.AnswerDto;
import com.nucleus.floracestore.model.service.AnswerServiceModel;
import com.nucleus.floracestore.model.view.AnswerViewModel;
import com.nucleus.floracestore.service.AnswerService;
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
public class AnswerController {
    private  final ModelMapper modelMapper;
    private final AnswerService answerService;
    private final AnswerAssembler assembler;
    @Autowired
    public AnswerController(ModelMapper modelMapper, AnswerService answerService, AnswerAssembler assembler) {
        this.modelMapper = modelMapper;
        this.answerService = answerService;
        this.assembler = assembler;
    }

    @PostMapping("/answers/questions/{questionId}")
    public ResponseEntity<EntityModel<AnswerViewModel>> writeAnswer(@RequestBody AnswerDto model, @PathVariable Long questionId) {

        AnswerServiceModel answerServiceModel = answerService.createAnswer(mapToService(model), questionId, getCurrentLoggedUsername());
        return ResponseEntity
                .created(linkTo(methodOn(AnswerController.class).writeAnswer(model, questionId)).toUri())
                .body(assembler.toModel(mapView(answerServiceModel)));
    }
    @GetMapping("/answers/{answerId}")
    public ResponseEntity<EntityModel<AnswerViewModel>> getAnswerById(@PathVariable Long answerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapView(answerService.getAnswerById(answerId))));
    }
    @GetMapping("/answers/users/{username}")
    public ResponseEntity<CollectionModel<EntityModel<AnswerViewModel>>> getAllAnswersByUsername() {
        List<EntityModel<AnswerViewModel>> questions = answerService.getAllAnswersByUsername(getCurrentLoggedUsername()).stream()
                .map(entity -> assembler.toModel(mapView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(questions, linkTo(methodOn(AnswerController.class).getAllAnswersByUsername()).withSelfRel()));
    }
    @PutMapping("/answers/{answerId}")
    public ResponseEntity<EntityModel<AnswerViewModel>> updateAnswer(@RequestBody AnswerDto model, @PathVariable Long answerId) {

        AnswerServiceModel answerServiceModel = answerService.updateAnswer(mapToService(model), answerId);
        return ResponseEntity
                .created(linkTo(methodOn(AnswerController.class).updateAnswer(model, answerId)).toUri())
                .body(assembler.toModel(mapView(answerServiceModel)));
    }
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<EntityModel<AnswerViewModel>> deleteAnswer(@PathVariable Long answerId) {
        EntityModel<AnswerViewModel> answerViewModel = assembler.toModel(mapView(answerService.deleteAnswerById(answerId)));
        return ResponseEntity.status(HttpStatus.OK).body(answerViewModel);
    }

    @GetMapping("/answers/questions/{questionId}")
    public ResponseEntity<CollectionModel<EntityModel<AnswerViewModel>>> getAllAnswersByQuestionId(@PathVariable Long questionId) {
        List<EntityModel<AnswerViewModel>> answers = answerService.getAllAnswersByQuestionId(questionId).stream()
                .map(entity -> assembler.toModel(mapView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(answers, linkTo(methodOn(AnswerController.class).getAllAnswersByQuestionId(questionId)).withSelfRel()));
    }

    private AnswerViewModel mapView(AnswerServiceModel model) {
        return modelMapper.map(model, AnswerViewModel.class);
    }

    private AnswerServiceModel mapToService(AnswerDto model) {
        return modelMapper.map(model, AnswerServiceModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}


