package project.serviceImpl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import project.entity.Delivery;
import project.entity.Order;
import project.entity.OrderStatus;
import project.entity.ShoppingCart;
import project.mapper.DeliveryMapper;
import project.mapper.OrderMapper;
import project.model.PageableDTO;
import project.model.deliveryModel.DeliveryRequest;
import project.model.orderItemModel.OrderItemResponse;
import project.model.orderModel.OrderResponse;
import project.model.orderModel.ReorderResponse;
import project.repository.DeliveryRepository;
import project.repository.OrderRepository;
import project.service.OrderService;
import static project.specification.OrderSpecification.*;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private Logger logger = LogManager.getLogger("serviceLogger");

    public OrderServiceImpl(OrderRepository orderRepository, DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public Order saveOrder(Order order) {
        logger.info("saveOrder() - Saving order");
        Order order1 = orderRepository.save(order);
        logger.info("saveOrder() - Order was saved");
        return order1;
    }

    @Override
    public Page<OrderResponse> getUserOrders(String email, PageableDTO pageableDTO) {
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
        else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
        logger.info("getUserOrders() - Finding orders for order responses for page "+pageable.getPageNumber());
        Page<Order> orders = orderRepository.findAll(byUserEmail(email), pageable);
        List<OrderResponse> orderResponses = OrderMapper.ORDER_MAPPER.orderListToOrderResponseList(orders.getContent());
        Page<OrderResponse> orderResponsePage = new PageImpl<>(orderResponses,pageable,orders.getTotalElements());
        logger.info("getUserOrders() - Orders for order responses were found");
        return orderResponsePage;
    }

    @Override
    public Order createOrder(ShoppingCart shoppingCart,OrderStatus status) {
        logger.info("createOrder() - Creating order");
        Order order = OrderMapper.ORDER_MAPPER.shoppingCartToOrder(shoppingCart,status);
        Order savedOrder = orderRepository.save(order);
        logger.info("createOrder() - Order was created");
        return savedOrder;
    }

    @Override
    public ReorderResponse createReorderResponse(Long id, List<OrderItemResponse> orderItemResponses) {
        logger.info("createReorderResponse() - Creating reorder response");
        Order order = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Order was not found by id "+id));
        ReorderResponse reorderResponse = OrderMapper.ORDER_MAPPER.orderToReorderResponse(order,orderItemResponses,orderRepository.findOrderSum(order.getId()));
        logger.info("createReorderResponse() - Reorder response was created");
        return reorderResponse;
    }

    @Override
    public Order reorder(Long id) {
        logger.info("reorder() - Reordering order");
        Order order = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Order wos not found by id "+id));
        Order newOrder = OrderMapper.ORDER_MAPPER.orderToNewOrder(order,order.getStatus());
        Order savedOrder = orderRepository.save(newOrder);
        logger.info("reorder() - Order was reordered");
        return savedOrder;
    }

    @Override
    public Order reorderWithDelivery(Long id, DeliveryRequest deliveryRequest) {
        logger.info("reorderWithDelivery() - Reordering order with delivery");
        Order order = orderRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Order wos not found by id "+id));
        Order newOrder;
        if(deliveryRequest.getCallBack()){
            newOrder = OrderMapper.ORDER_MAPPER.orderToNewOrder(order,OrderStatus.CALL);
        } else {
            newOrder = OrderMapper.ORDER_MAPPER.orderToNewOrder(order,OrderStatus.ORDERED);
        }
        Order savedOrder = orderRepository.save(newOrder);
        Delivery delivery = DeliveryMapper.DELIVERY_MAPPER.deliveryRequestToDelivery(deliveryRequest,savedOrder);
        deliveryRepository.save(delivery);
        logger.info("reorderWithDelivery() - Order was reordered with delivery");
        return savedOrder;
    }

    @Override
    public void setReorderedOrderPrice(Order order) {
        logger.info("setNewOrderPrice() - Setting price for reordered order");
        order.setPrice(orderRepository.findOrderSum(order.getId()));
        orderRepository.save(order);
        logger.info("setNewOrderPrice() - Price was set for reordered order");
    }
}
