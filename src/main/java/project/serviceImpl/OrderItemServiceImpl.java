package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import project.entity.Additive;
import project.entity.Order;
import project.entity.OrderItem;
import project.entity.ShoppingCartItem;
import project.mapper.OrderItemMapper;
import project.model.PageableDTO;
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
    private Logger logger = LogManager.getLogger("serviceLogger");

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Page<OrderItemResponse> getOrderItemsByOrderId(Long orderId, PageableDTO pageableDTO) {
        logger.info("getOrderItemsByOrderId() - Finding order items for order item responses for page " + pageableDTO.getPage());
        Pageable pageable;
        Sort sort;
        if (pageableDTO.getSortDirection().equals("DESC")) {
            sort = Sort.by(pageableDTO.getSortField()).descending();
        } else {
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(), sort);
        Page<OrderItem> orderItems = orderItemRepository.findAll(byOrderId(orderId), pageable);
        List<OrderItemResponse> orderItemResponses = OrderItemMapper.ORDER_ITEM_MAPPER.orderItemListToOrderItemResponseList(orderItems.getContent());
        Page<OrderItemResponse> orderItemResponsePage = new PageImpl<>(orderItemResponses, pageable, orderItems.getTotalElements());
        logger.info("getOrderItemsByOrderId() - Order items for order item responses were found");
        return orderItemResponsePage;
    }

    @Override
    public List<OrderItemResponse> getOrderItemResponsesForReorder(Long orderId) {
        logger.info("getOrderItemResponsesForReorder() - Finding order items with additives by order id " + orderId + " and mapping them to order item response");
        List<OrderItem> orderItems = orderItemRepository.findWithAdditivesByOrderId(orderId);
        List<OrderItemResponse> orderItemResponses = new ArrayList<>(orderItems.size());
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getPrice().compareTo(BigDecimal.valueOf(0)) != 0) {
                BigDecimal newOrderItemPrice = new BigDecimal(0);
                newOrderItemPrice = newOrderItemPrice.add(orderItem.getProduct().getPrice());
                BigDecimal orderItemAdditivesSum = orderItemRepository.findOrderItemAdditivesSum(orderItem.getId());
                newOrderItemPrice = newOrderItemPrice.add(orderItemAdditivesSum);
                newOrderItemPrice = newOrderItemPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                OrderItemResponse orderItemResponse = OrderItemMapper.ORDER_ITEM_MAPPER.oldOrderItemToOrderItemResponse(orderItem, newOrderItemPrice);
                orderItemResponses.add(orderItemResponse);
            }
        }
        logger.info("getOrderItemResponsesForReorder() - Order items with additives for order item responses were found and mapped");
        return orderItemResponses;
    }

    @Override
    public void saveNewOrderItems(Order order, Long id) {
        logger.info("saveNewOrderItems() - Saving new order items");
        List<OrderItem> orderItems = orderItemRepository.findWithAdditivesByOrderId(id);
        List<OrderItem> newOrderItems = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getPrice().compareTo(BigDecimal.valueOf(0)) != 0) {
                BigDecimal newOrderItemPrice = new BigDecimal(0);
                newOrderItemPrice = newOrderItemPrice.add(orderItem.getProduct().getPrice());
                newOrderItemPrice = newOrderItemPrice.add(orderItemRepository.findOrderItemAdditivesSum(orderItem.getId()));
                newOrderItemPrice = newOrderItemPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                OrderItem newOrderItem = OrderItemMapper.ORDER_ITEM_MAPPER.orderItemToNewOrderItem(orderItem, newOrderItemPrice, order);
                newOrderItems.add(newOrderItem);
            }
        }
        orderItemRepository.saveAll(newOrderItems);
        logger.info("saveNewOrderItems() - New order items were saved");
    }

    @Override
    public void createOrderItems(List<ShoppingCartItem> shoppingCartItems, Order order) {
        logger.info("createOrderItems() - Creating order items");
        List<OrderItem> orderItems = OrderItemMapper.ORDER_ITEM_MAPPER.shoppingCartItemListToOrderItemList(shoppingCartItems, order);
        orderItemRepository.saveAll(orderItems);
        logger.info("createOrderItems() - Order items were created");
    }
}
