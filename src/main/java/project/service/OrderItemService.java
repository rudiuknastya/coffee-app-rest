package project.service;

import org.springframework.data.domain.Page;
import project.entity.Order;
import project.entity.OrderItem;
import project.entity.ShoppingCartItem;
import project.model.PageableDTO;
import project.model.orderItemModel.OrderItemResponse;

import java.util.List;

public interface OrderItemService {
    Page<OrderItemResponse> getOrderItemsByOrderId(Long orderId, PageableDTO pageableDTO);
    List<OrderItemResponse> getOrderItemResponsesForReorder(Long orderId);
    void saveNewOrderItems(Order order,Long id);
    void createOrderItems(List<ShoppingCartItem> shoppingCartItems, Order order);
}
