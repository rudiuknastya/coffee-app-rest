package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Order;
import project.entity.OrderStatus;
import project.entity.ShoppingCart;
import project.model.orderModel.OrderResponse;

import java.math.BigDecimal;

public interface OrderService {
    Order saveOrder(Order order);
    Page<OrderResponse> getUserOrders(String email, Pageable pageable);
    Order getOrderById(Long id);
    BigDecimal getOrderPrice(Long orderId);
    Order createOrder(ShoppingCart shoppingCart, OrderStatus status);
}
