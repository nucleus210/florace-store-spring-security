package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.OrderStatusCodesController;
import com.nucleus.floracestore.model.dto.OrderStatusCodesDto;
import com.nucleus.floracestore.model.view.OrderStatusCodesViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderStatusCodesAssembler implements RepresentationModelAssembler<OrderStatusCodesViewModel, EntityModel<OrderStatusCodesViewModel>> {
    private final ModelMapper modelMapper;

    public OrderStatusCodesAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<OrderStatusCodesViewModel> toModel(OrderStatusCodesViewModel orderStatusCodesModel) {
        return EntityModel.of(orderStatusCodesModel,
                linkTo(methodOn(OrderStatusCodesController.class).createOrderStatusCodes(modelMapper.map(orderStatusCodesModel, OrderStatusCodesDto.class))).withSelfRel(),
                linkTo(methodOn(OrderStatusCodesController.class).getOrderStatusCodeById(orderStatusCodesModel.getOrderStatusCodeId())).withSelfRel(),
                linkTo(methodOn(OrderStatusCodesController.class).getOrderStatusCodeByCodeName(orderStatusCodesModel.getStatusCode())).withSelfRel(),
                linkTo(methodOn(OrderStatusCodesController.class).getAllOrderStatusCodes()).withRel("orderStatusCodes"));
    }
}
