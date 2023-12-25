package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.Order;
import project.entity.OrderStatus;
import project.entity.ShoppingCart;
import project.model.orderItemModel.OrderItemResponse;
import project.model.orderModel.OrderResponse;
import project.model.orderModel.ReorderResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class);
    @Mapping(target = "status", source = "orderStatus")
    @Mapping(target = "orderTime", expression = "java(getTimeNow())")
    @Mapping(target = "orderDate", expression = "java(getDateNow())")
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "price")
    Order orderToNewOrder(Order order, OrderStatus orderStatus);

    List<OrderResponse> orderListToOrderResponseList(List<Order> orders);
    @Mapping(target = "orderItemResponses", source = "orderItemResponseList")
    @Mapping(target = "price", source = "orderPrice")
    @Mapping(target = "orderId", source = "order.id")
    ReorderResponse orderToReorderResponse(Order order, List<OrderItemResponse> orderItemResponseList, BigDecimal orderPrice);
    @Mapping(target = "locationCity", source = "location.city")
    @Mapping(target = "locationAddress", source = "location.address")
    OrderResponse orderToOrderResponse(Order order);
    @Mapping(target = "orderTime", expression = "java(getTimeNow())")
    @Mapping(target = "orderDate", expression = "java(getDateNow())")
    @Mapping(target = "status", source = "orderStatus")
    @Mapping(ignore = true, target = "id")
    Order shoppingCartToOrder(ShoppingCart shoppingCart, OrderStatus orderStatus);
    default LocalTime getTimeNow(){
        return LocalTime.now();
    }
    default LocalDate getDateNow(){
        return LocalDate.now();
    }
}
