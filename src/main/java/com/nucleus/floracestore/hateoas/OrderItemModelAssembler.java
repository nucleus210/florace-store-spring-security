package com.nucleus.floracestore.hateoas;

import com.nucleus.floracestore.controller.OrderItemController;
import com.nucleus.floracestore.model.view.OrderItemViewModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderItemModelAssembler implements RepresentationModelAssembler<OrderItemViewModel, EntityModel<OrderItemViewModel>> {
    @Override
    public EntityModel<OrderItemViewModel> toModel(OrderItemViewModel orderItemModel) {

        return EntityModel.of(orderItemModel,
                linkTo(methodOn(OrderItemController.class).getCountOrderItems(orderItemModel.getOrder().getOrderId())).withSelfRel(),
                linkTo(methodOn(OrderItemController.class).getOrderItemsByOrderId(orderItemModel.getOrder().getOrderId())).withSelfRel(),
//                linkTo(methodOn(OrderItemController.class).getOrderItemByOrderIdAndProductId(orderItemModel.getOrder().getOrderId(),orderItemModel.getProduct().getProductId())).withSelfRel(),
                linkTo(methodOn(OrderItemController.class).deleteOrderItem(orderItemModel.getOrderItemId())).withSelfRel(),
                linkTo(methodOn(OrderItemController.class).getAllOrderItems()).withRel("order-items"));
    }
}