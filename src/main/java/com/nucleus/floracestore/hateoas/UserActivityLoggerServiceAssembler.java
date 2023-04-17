package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.UserActivityLoggerController;
import com.nucleus.floracestore.model.view.UserActivityLoggerViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserActivityLoggerServiceAssembler implements RepresentationModelAssembler<UserActivityLoggerViewModel,
        EntityModel<UserActivityLoggerViewModel>> {

    @Override
    public EntityModel<UserActivityLoggerViewModel> toModel(UserActivityLoggerViewModel model) {
        return EntityModel.of(model,
                linkTo(methodOn(UserActivityLoggerController.class).getActivityLogById(model.getUserLogId())).withRel("getLogActivityById"),
                linkTo(methodOn(UserActivityLoggerController.class).deleteActivityLogById(model.getUserLogId())).withRel("deleteLogActivityById"),
                linkTo(methodOn(UserActivityLoggerController.class).getAllActivityLogs()).withRel("getAllLogActivities"));
    }
}