package project.serviceImpl;

import jakarta.persistence.EntityExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Order;
import project.mapper.OrderMapper;
import project.model.orderModel.OrderResponse;
import project.repository.OrderRepository;
import project.service.OrderService;
import static project.specification.OrderSpecification.*;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Order saveOrder(Order order) {
        logger.info("saveOrder() - Saving order");
        Order order1 = orderRepository.save(order);
        logger.info("saveOrder() - Order was saved");
        return order1;
    }

    @Override
    public Page<OrderResponse> getUserOrders(String email, Pageable pageable) {
        logger.info("getUserOrders() - Finding orders for order responses for page "+pageable.getPageNumber());
        Page<Order> orders = orderRepository.findAll(byUserEmail(email), pageable);
        List<OrderResponse> orderResponses = OrderMapper.orderListToOrderResponseList(orders.getContent());
        Page<OrderResponse> orderResponsePage = new PageImpl<>(orderResponses,pageable,orders.getTotalElements());
        logger.info("getUserOrders() - Orders for order responses were found");
        return orderResponsePage;
    }

    @Override
    public Order getOrderById(Long id) {
        logger.info("getOrderById() - Finding order by id "+id);
        Order order = orderRepository.findById(id).orElseThrow(EntityExistsException::new);
        logger.info("getOrderById() - Order was found");
        return order;
    }

    @Override
    public BigDecimal getOrderPrice(Long orderId) {
        logger.info("getOrderPrice() - Finding order price by id "+orderId);
        BigDecimal price = orderRepository.findOrderSum(orderId);
        logger.info("getOrderPrice() - Order price was found");
        return price;
    }
}
