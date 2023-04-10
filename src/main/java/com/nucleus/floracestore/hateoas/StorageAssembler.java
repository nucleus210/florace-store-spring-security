package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.StorageController;
import com.nucleus.floracestore.model.view.SingleUploadResponseMessage;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StorageAssembler implements RepresentationModelAssembler<SingleUploadResponseMessage, EntityModel<SingleUploadResponseMessage>> {

    private ModelMapper modelMapper;

    @Override
    public EntityModel<SingleUploadResponseMessage> toModel(SingleUploadResponseMessage singleUploadResponseMessage) {

        return EntityModel.of(singleUploadResponseMessage,
                linkTo(methodOn(StorageController.class).uploadFileToStorages(singleUploadResponseMessage.getFile())).withRel("uploadFile"),
//                linkTo(methodOn(StorageController.class).multiUploadFileModel(singleUploadResponseMessage)).withRel("uploadFile"),

                linkTo(methodOn(StorageController.class).getListFiles()).withSelfRel());
    }
}