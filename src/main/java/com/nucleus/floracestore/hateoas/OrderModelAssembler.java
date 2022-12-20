package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.OrderController;
import com.nucleus.floracestore.model.view.OrderViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderViewModel, EntityModel<OrderViewModel>> {

    @Override
    public EntityModel<OrderViewModel> toModel(OrderViewModel orderModel) {

        return EntityModel.of(orderModel,
                linkTo(methodOn(OrderController.class).createOrder(orderModel)).withSelfRel(),
                linkTo(methodOn(OrderController.class).getOrderById(orderModel.getOrderId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getOrderByUsername(orderModel.getUsername())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrdersByUsername(orderModel.getUsername())).withRel("ordersByUsername"),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"));
    }
}