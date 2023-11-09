package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.entity.*;
import project.model.PageableDTO;
import project.model.deliveryModel.DeliveryRequest;
import project.model.orderItemModel.OrderItemResponse;
import project.model.orderModel.OrderResponse;
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
    @PostMapping("/orders/new/withDelivery")
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

    @Operation(summary = "Create order without delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/orders/new")
    ResponseEntity<?> createOrder(){
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
        order.setStatus(OrderStatus.ORDERED);
        Order savedOrder = orderService.saveOrder(order);
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
    @Operation(summary = "Get orders for user order history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/orders/history")
    Page<OrderResponse> getOrdersForOrderHistory(PageableDTO pageableDTO){
        int page = 0;
        int size = 5;
        if(pageableDTO.getPage() >= 0){
            page = pageableDTO.getPage();
        }
        if(pageableDTO.getSize() > 0){
            size = pageableDTO.getSize();
        }
        Pageable pageable;
        if(pageableDTO.getSortField() != null && !pageableDTO.getSortField().equals("")){
            Sort sort = Sort.by("id").ascending();
            if(pageableDTO.getSortDirection() != null && !pageableDTO.getSortDirection().equals("")){
                if(pageableDTO.getSortDirection().equals("ASC")){
                    sort = Sort.by(pageableDTO.getSortField()).ascending();
                }
                if(pageableDTO.getSortDirection().equals("DESC")){
                    sort = Sort.by(pageableDTO.getSortField()).descending();
                }

            }
            pageable = PageRequest.of(page,size,sort);
        } else {
            pageable = PageRequest.of(page,size,Sort.by("id").ascending());
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        return orderService.getUserOrders(email,pageable);
    }
    @Operation(summary = "Get order items for order in order history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/orders/history/{orderId}")
    Page<OrderItemResponse> getOrderItemsForOrderHistory(@PathVariable("orderId")Long id, PageableDTO pageableDTO){
        int page = 0;
        int size = 5;
        if(pageableDTO.getPage() >= 0){
            page = pageableDTO.getPage();
        }
        if(pageableDTO.getSize() > 0){
            size = pageableDTO.getSize();
        }
        Pageable pageable;
        if(pageableDTO.getSortField() != null && !pageableDTO.getSortField().equals("")){
            Sort sort = Sort.by("id").ascending();
            if(pageableDTO.getSortDirection() != null && !pageableDTO.getSortDirection().equals("")){
                if(pageableDTO.getSortDirection().equals("ASC")){
                    sort = Sort.by(pageableDTO.getSortField()).ascending();
                }
                if(pageableDTO.getSortDirection().equals("DESC")){
                    sort = Sort.by(pageableDTO.getSortField()).descending();
                }

            }
            pageable = PageRequest.of(page,size,sort);
        } else {
            pageable = PageRequest.of(page,size,Sort.by("id").ascending());
        }
        return orderItemService.getOrderItemsByOrderId(id,pageable);
    }
}
