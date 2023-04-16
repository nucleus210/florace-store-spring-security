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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class AnswerController {
    private final ModelMapper modelMapper;
    private final AnswerService answerService;
    private final AnswerAssembler assembler;

    @Autowired
    public AnswerController(ModelMapper modelMapper, AnswerService answerService, AnswerAssembler assembler) {
        this.modelMapper = modelMapper;
        this.answerService = answerService;
        this.assembler = assembler;
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping("/answers")
    public ResponseEntity<EntityModel<AnswerViewModel>> writeAnswer(@RequestBody AnswerDto model) {
        AnswerServiceModel answerServiceModel = answerService.createAnswer(mapToService(model), model.getQuestionId(), getCurrentLoggedUsername());
        return ResponseEntity
                .created(linkTo(methodOn(AnswerController.class).writeAnswer(model)).toUri())
                .body(assembler.toModel(mapToView(answerServiceModel)));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PutMapping("/answers/{answerId}")
    public ResponseEntity<EntityModel<AnswerViewModel>> updateAnswer(@RequestBody AnswerDto model, @PathVariable Long answerId) {
        AnswerServiceModel answerServiceModel = answerService.updateAnswer(mapToService(model), answerId);
        return ResponseEntity
                .created(linkTo(methodOn(AnswerController.class).updateAnswer(model, answerId)).toUri())
                .body(assembler.toModel(mapToView(answerServiceModel)));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<EntityModel<AnswerViewModel>> deleteAnswer(@PathVariable Long answerId) {
        EntityModel<AnswerViewModel> answerViewModel = assembler.toModel(mapToView(answerService.deleteAnswerById(answerId)));
        return ResponseEntity.status(HttpStatus.OK).body(answerViewModel);
    }

    @GetMapping("/answers/{answerId}")
    public ResponseEntity<EntityModel<AnswerViewModel>> getAnswerById(@PathVariable Long answerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(answerService.getAnswerById(answerId))));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/answers/search/users/{username}")
    public ResponseEntity<CollectionModel<EntityModel<AnswerViewModel>>> getAllAnswersByUsername() {
        List<EntityModel<AnswerViewModel>> questions = answerService.getAllAnswersByUsername(getCurrentLoggedUsername()).stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(questions, linkTo(methodOn(AnswerController.class).getAllAnswersByUsername()).withSelfRel()));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/answers/search/questions/{questionId}")
    public ResponseEntity<CollectionModel<EntityModel<AnswerViewModel>>> getAllAnswersByQuestionId(@PathVariable Long questionId) {
        List<EntityModel<AnswerViewModel>> answers = answerService.getAllAnswersByQuestionId(questionId).stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(answers, linkTo(methodOn(AnswerController.class).getAllAnswersByQuestionId(questionId)).withSelfRel()));
    }

    private AnswerViewModel mapToView(AnswerServiceModel model) {
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


