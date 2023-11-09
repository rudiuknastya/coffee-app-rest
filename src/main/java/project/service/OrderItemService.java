package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.OrderItem;
import project.model.orderItemModel.OrderItemResponse;

public interface OrderItemService {
    OrderItem saveOrderItem(OrderItem orderItem);
    Page<OrderItemResponse> getOrderItemsByOrderId(Long orderId, Pageable pageable);
}
