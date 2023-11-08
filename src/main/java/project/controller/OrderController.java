package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.entity.*;
import project.model.deliveryModel.DeliveryRequest;
import project.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Tag(name = "Order")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartItemService shoppingCartItemService;
    private final DeliveryService deliveryService;

    public OrderController(OrderService orderService, OrderItemService orderItemService, ShoppingCartService shoppingCartService, ShoppingCartItemService shoppingCartItemService, DeliveryService deliveryService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.shoppingCartService = shoppingCartService;
        this.shoppingCartItemService = shoppingCartItemService;
        this.deliveryService = deliveryService;
    }

    @Operation(summary = "Create order with delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Failed validation")})
    @PostMapping("/order/new/withDelivery")
    ResponseEntity<?> createOrderWithDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest){
        Delivery delivery = new Delivery(
                deliveryRequest.getName(), deliveryRequest.getPhoneNumber(),
                deliveryRequest.getCity(), deliveryRequest.getBuilding(),
                deliveryRequest.getStreet(), deliveryRequest.getEntrance(),
                deliveryRequest.getApartment(), deliveryRequest.getPayment(),
                deliveryRequest.getRemainderFrom(), deliveryRequest.getDeliveryDate(),
                deliveryRequest.getDeliveryTime());
        Order order = new Order();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemService.getShoppingCartItemsWithAdditives(email);
        ShoppingCart shoppingCart = shoppingCartItems.get(0).getShoppingCart();
        order.setPrice(shoppingCart.getPrice());
        order.setOrderDate(LocalDate.now());
        order.setOrderTime(LocalTime.now());
        order.setLocation(shoppingCart.getLocation());
        order.setUser(shoppingCart.getUser());
        if(deliveryRequest.getCallBack()){
          order.setStatus(OrderStatus.CALL);
        } else {
            order.setStatus(OrderStatus.ORDERED);
        }
        Order savedOrder = orderService.saveOrder(order);
        delivery.setOrder(savedOrder);
        deliveryService.saveDelivery(delivery);
        for(ShoppingCartItem shoppingCartItem: shoppingCartItems){
            OrderItem orderItem = new OrderItem();
            List<Additive> additives = new ArrayList<>(shoppingCartItem.getAdditives());
            orderItem.setAdditives(additives);
            orderItem.setProduct(shoppingCartItem.getProduct());
            orderItem.setQuantity(shoppingCartItem.getQuantity());
            orderItem.setDeleted(false);
            orderItem.setOrder(savedOrder);
            orderItem.setPrice(shoppingCartItem.getPrice());
            orderItemService.saveOrderItem(orderItem);
        }
        shoppingCartItemService.deleteShoppingCartItems(shoppingCartItems);
        shoppingCartService.deleteShoppingCart(shoppingCart);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
