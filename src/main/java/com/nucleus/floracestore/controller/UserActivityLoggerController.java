package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.UserActivityLoggerServiceAssembler;
import com.nucleus.floracestore.model.service.UserActivityLoggerServiceModel;
import com.nucleus.floracestore.model.view.UserActivityLoggerViewModel;
import com.nucleus.floracestore.service.UserActivityLoggerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserActivityLoggerController {
    private final ModelMapper modelMapper;
    private final UserActivityLoggerService userActivityLoggerService;
    private final UserActivityLoggerServiceAssembler assembler;
    @Autowired
    public UserActivityLoggerController(ModelMapper modelMapper,
                                        UserActivityLoggerService userActivityLoggerService,
                                        UserActivityLoggerServiceAssembler assembler) {
        this.modelMapper = modelMapper;
        this.userActivityLoggerService = userActivityLoggerService;
        this.assembler = assembler;
    }

    @GetMapping("/logs/{logId}")
    public ResponseEntity<EntityModel<UserActivityLoggerViewModel>> getActivityLogById(@PathVariable Long logId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(assembler.toModel(mapToView(userActivityLoggerService.getUserActivityLoggerById(logId))));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @DeleteMapping("/logs/{logId}")
    public ResponseEntity<EntityModel<UserActivityLoggerViewModel>> deleteActivityLogById(@PathVariable Long questionId) {
        EntityModel<UserActivityLoggerViewModel> activityLogModel =
                assembler.toModel(mapToView(userActivityLoggerService.deleteUserActivityLoggerById(questionId)));
        return ResponseEntity.status(HttpStatus.OK).body(activityLogModel);
    }

    @GetMapping("/logs")
    public ResponseEntity<CollectionModel<EntityModel<UserActivityLoggerViewModel>>> getAllActivityLogs() {
        List<EntityModel<UserActivityLoggerViewModel>> logs = userActivityLoggerService.getAllActivityLogs().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(logs, linkTo(methodOn(UserActivityLoggerController.class)
                        .getAllActivityLogs()).withSelfRel()));
    }

    private UserActivityLoggerViewModel mapToView(UserActivityLoggerServiceModel model) {
        return modelMapper.map(model, UserActivityLoggerViewModel.class);
    }

}
