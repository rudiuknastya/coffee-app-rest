package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Order;
import project.entity.OrderStatus;
import project.entity.ShoppingCart;
import project.model.PageableDTO;
import project.model.deliveryModel.DeliveryRequest;
import project.model.orderItemModel.OrderItemResponse;
import project.model.orderModel.OrderResponse;
import project.model.orderModel.ReorderResponse;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);
    Page<OrderResponse> getUserOrders(String email, PageableDTO pageableDTO);
    Order createOrder(ShoppingCart shoppingCart, OrderStatus status);
    ReorderResponse createReorderResponse(Long id, List<OrderItemResponse> orderItemResponses);
    Order reorder(Long id);
    Order reorderWithDelivery(Long id, DeliveryRequest deliveryRequest);
}
