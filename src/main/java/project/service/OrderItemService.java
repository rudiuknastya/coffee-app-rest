package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Order;
import project.entity.OrderItem;
import project.entity.ShoppingCartItem;
import project.model.orderItemModel.OrderItemResponse;

import java.util.List;

public interface OrderItemService {
    OrderItem saveOrderItem(OrderItem orderItem);
    Page<OrderItemResponse> getOrderItemsByOrderId(Long orderId, Pageable pageable);
    List<OrderItemResponse> getOrderItemsWithAdditivesByOrderId(Long orderId);
    void saveNewOrderItems(Long orderId, Order order);
    void createOrderItems(List<ShoppingCartItem> shoppingCartItems, Order order);
}
