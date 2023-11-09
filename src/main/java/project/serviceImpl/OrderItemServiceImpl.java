package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.OrderItem;
import project.mapper.OrderItemMapper;
import project.model.orderItemModel.OrderItemResponse;
import project.repository.OrderItemRepository;
import project.service.OrderItemService;

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
}
