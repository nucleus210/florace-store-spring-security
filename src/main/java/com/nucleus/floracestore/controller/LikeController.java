package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.LikeAssembler;
import com.nucleus.floracestore.model.service.LikeServiceModel;
import com.nucleus.floracestore.model.view.LikeViewModel;
import com.nucleus.floracestore.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
public class LikeController {
    private final ModelMapper modelMapper;
    private final LikeService likeService;
    private final LikeAssembler assembler;

    public LikeController(ModelMapper modelMapper, LikeService likeService, LikeAssembler assembler) {
        this.modelMapper = modelMapper;
        this.likeService = likeService;
        this.assembler = assembler;
    }

    @PostMapping("/likes/question/{questionId}")
    public ResponseEntity<EntityModel<LikeViewModel>> likeQuestion(@PathVariable Long questionId) {
        log.info("LikeController: like question with id " + questionId);
        return ResponseEntity
                .created(linkTo(methodOn(LikeController.class).likeQuestion(questionId)).toUri())
                .body(assembler.toModel(mapView(likeService.createLike(questionId, getCurrentLoggedUsername()))));
    }

    @GetMapping("/likes/question/{questionId}")
    public ResponseEntity<CollectionModel<EntityModel<LikeViewModel>>> getAllLikesByQuestionId(@PathVariable Long questionId) {
        log.info("LikeController: get all questions likes");
        List<EntityModel<LikeViewModel>> likesCount = likeService.getLikesByQuestionId(questionId).stream()
                .map(entity -> assembler.toModel(mapView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(likesCount, linkTo(methodOn(AnswerController.class).getAllAnswersByQuestionId(questionId)).withSelfRel()));
    }

    @DeleteMapping("/likes/{likeId}")
    public ResponseEntity<EntityModel<LikeViewModel>> deleteLikeById(@PathVariable Long likeId) {
        log.info("LikeController: delete like with id " + likeId);
        EntityModel<LikeViewModel> likeViewModel = assembler.toModel(mapView(likeService.deleteLikeById(likeId)));
        return ResponseEntity.status(HttpStatus.OK).body(likeViewModel);
    }

    private LikeViewModel mapView(LikeServiceModel model) {
        return modelMapper.map(model, LikeViewModel.class);
    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }
}
