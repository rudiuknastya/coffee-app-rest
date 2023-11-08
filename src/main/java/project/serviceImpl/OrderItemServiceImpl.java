package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.OrderItem;
import project.repository.OrderItemRepository;
import project.service.OrderItemService;
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
}
