package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Additive;
import project.entity.Order;
import project.entity.OrderItem;
import project.entity.ShoppingCartItem;
import project.mapper.OrderItemMapper;
import project.model.orderItemModel.OrderItemResponse;
import project.repository.OrderItemRepository;
import project.service.OrderItemService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static project.specification.OrderItemSpecification.*;
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        logger.info("saveOrderItem() - Saving order item");
        OrderItem orderItem1 = orderItemRepository.save(orderItem);
        logger.info("saveOrderItem() - Order item was saved");
        return orderItem1;
    }

    @Override
    public Page<OrderItemResponse> getOrderItemsByOrderId(Long orderId, Pageable pageable) {
        logger.info("getOrderItemsByOrderId() - Finding order items for order item responses for page "+pageable.getPageNumber());
        Page<OrderItem> orderItems = orderItemRepository.findAll(byOrderId(orderId), pageable);
        List<OrderItemResponse> orderItemResponses = OrderItemMapper.orderItemListToOrderItemResponseList(orderItems.getContent());
        Page<OrderItemResponse> orderItemResponsePage = new PageImpl<>(orderItemResponses,pageable,orderItems.getTotalElements());
        logger.info("getOrderItemsByOrderId() - Order items for order item responses were found");
        return orderItemResponsePage;
    }

    @Override
    public List<OrderItemResponse> getOrderItemsWithAdditivesByOrderId(Long orderId) {
        logger.info("getOrderItemsWithAdditivesByOrderId() - Finding order items with additives for order item responses by order id "+orderId);
        List<OrderItemResponse> orderItemResponses = OrderItemMapper.oldOrderItemListToOrderItemResponseList(orderItemRepository.findWithAdditivesByOrderId(orderId));
        logger.info("getOrderItemsWithAdditivesByOrderId() - Order items with additives for order item responses were found");
        return orderItemResponses;
    }

    @Override
    public void saveNewOrderItems(Long orderId, Order order) {
        logger.info("saveNewOrderItems() - Saving new order items");
        List<OrderItem> orderItems = orderItemRepository.findWithAdditivesByOrderId(orderId);
        List<OrderItem> newOrderItems = new ArrayList<>(orderItems.size());
        for(OrderItem orderItem: orderItems){
            OrderItem newOrderItem = OrderItemMapper.orderItemToNewOrderItem(orderItem);
            BigDecimal price = new BigDecimal(0);
            price = price.add(orderItem.getProduct().getPrice());
            price = price.add(orderItemRepository.findOrderItemAdditivesSum(orderItem.getId()));
            price = price.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            newOrderItem.setPrice(price);
            newOrderItem.setOrder(order);
            newOrderItems.add(newOrderItem);
        }
        orderItemRepository.saveAll(newOrderItems);
        logger.info("saveNewOrderItems() - New order items were saved");
    }

    @Override
    public void createOrderItems(List<ShoppingCartItem> shoppingCartItems, Order order) {
        logger.info("createOrderItems() - Creating order items");
        for(ShoppingCartItem shoppingCartItem: shoppingCartItems){
            List<Additive> additives = new ArrayList<>(shoppingCartItem.getAdditives());
            OrderItem orderItem = OrderItemMapper.shoppingCartItemToOrderItem(shoppingCartItem);
            orderItem.setAdditives(additives);
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }
        logger.info("createOrderItems() - Order items were created");
    }
}
