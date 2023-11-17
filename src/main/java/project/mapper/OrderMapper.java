package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.Order;
import project.entity.OrderStatus;
import project.entity.ShoppingCart;
import project.model.orderModel.OrderResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    //OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class);
    @Named("orderToNewOrder")
    static Order orderToNewOrder(Order order){
        Order order1 = new Order();
        order1.setPrice(order.getPrice());
        order1.setStatus(OrderStatus.ORDERED);
        order1.setOrderTime(LocalTime.now());
        order1.setOrderDate(LocalDate.now());
        order1.setLocation(order.getLocation());
        order1.setUser(order.getUser());
        order1.setPrice(BigDecimal.valueOf(0));
        return order1;
    }
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
    @Named("shoppingCartToOrder")
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
