package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Order;
import project.model.orderModel.OrderResponse;

public interface OrderService {
    Order saveOrder(Order order);
    Page<OrderResponse> getUserOrders(String email, Pageable pageable);
}
