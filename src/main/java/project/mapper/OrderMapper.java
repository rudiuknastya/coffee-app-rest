package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.Order;
import project.entity.ShoppingCart;
import project.model.orderModel.OrderResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Named("orderListToOrderResponseList")
    static List<OrderResponse> orderListToOrderResponseList(List<Order> orders){
        if(orders ==  null){
            return null;
        }
        List<OrderResponse> orderResponses = new ArrayList<>(orders.size());
        for(Order order: orders){
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.getId());
            orderResponse.setPrice(order.getPrice());
            orderResponse.setOrderDate(order.getOrderDate());
            orderResponse.setLocationCity(order.getLocation().getCity());
            orderResponse.setLocationAddress(order.getLocation().getAddress());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
    static Order shoppingCartToOrder(ShoppingCart shoppingCart){
        Order order = new Order();
        order.setPrice(shoppingCart.getPrice());
        order.setOrderDate(LocalDate.now());
        order.setOrderTime(LocalTime.now());
        order.setLocation(shoppingCart.getLocation());
        order.setUser(shoppingCart.getUser());
        return order;
    }
}
