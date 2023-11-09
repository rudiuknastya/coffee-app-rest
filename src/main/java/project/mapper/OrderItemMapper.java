package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import project.entity.Additive;
import project.entity.OrderItem;
import project.model.orderItemModel.OrderItemResponse;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
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
}
