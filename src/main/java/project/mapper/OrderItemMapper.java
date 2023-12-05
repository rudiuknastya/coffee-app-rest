package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import project.entity.Additive;
import project.entity.OrderItem;
import project.entity.ShoppingCartItem;
import project.model.orderItemModel.OrderItemResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    static OrderItem orderItemToNewOrderItem(OrderItem orderItem){
        OrderItem newOrderItem = new OrderItem();
        //newOrderItem.setProduct(orderItem.getProduct());
        newOrderItem.setQuantity(orderItem.getQuantity());
        List<Additive> additives = new ArrayList<>(orderItem.getAdditives());
        newOrderItem.setAdditives(additives);
        newOrderItem.setDeleted(orderItem.getDeleted());
        return newOrderItem;
    }

    @Named("shoppingCartItemToOrderItem")
    static OrderItem shoppingCartItemToOrderItem(ShoppingCartItem shoppingCartItem){
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(shoppingCartItem.getProduct());
        orderItem.setQuantity(shoppingCartItem.getQuantity());
        orderItem.setDeleted(false);
        orderItem.setPrice(shoppingCartItem.getPrice());
        return orderItem;
    }
    @Named("orderItemListToOrderItemResponseList")
    static List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> orderItems){
        if(orderItems == null){
            return null;
        }
        List<OrderItemResponse> orderItemResponses = new ArrayList<>(orderItems.size());
        for(OrderItem orderItem: orderItems){
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setProductName(orderItem.getProduct().getName());
            orderItemResponse.setPrice(orderItem.getPrice());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            if(orderItem.getAdditives() != null) {
                List<String> additives = new ArrayList<>(orderItem.getAdditives().size());
                for (Additive additive : orderItem.getAdditives()) {
                    additives.add(additive.getName());
                }
                orderItemResponse.setAdditives(additives);
            }
            orderItemResponses.add(orderItemResponse);
        }
        return orderItemResponses;
    }
    @Named("oldOrderItemListToOrderItemResponseList")
    static List<OrderItemResponse> oldOrderItemListToOrderItemResponseList(List<OrderItem> orderItems){
        if(orderItems == null){
            return null;
        }
        List<OrderItemResponse> orderItemResponses = new ArrayList<>(orderItems.size());
        for(OrderItem orderItem: orderItems){
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            BigDecimal price = new BigDecimal(0);
            orderItemResponse.setProductName(orderItem.getProduct().getName());
            price = price.add(orderItem.getProduct().getPrice());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            if(orderItem.getAdditives() != null) {
                List<String> additives = new ArrayList<>(orderItem.getAdditives().size());
                for (Additive additive : orderItem.getAdditives()) {
                    additives.add(additive.getName());
                    price = price.add(additive.getPrice());
                }
                orderItemResponse.setAdditives(additives);
            }
            price = price.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            orderItemResponse.setPrice(price);
            orderItemResponses.add(orderItemResponse);
        }
        return orderItemResponses;
    }
}
