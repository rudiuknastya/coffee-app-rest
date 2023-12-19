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
import project.mapper.DeliveryMapper;
import project.mapper.OrderItemMapper;
import project.mapper.OrderMapper;
import project.model.PageableDTO;
import project.model.deliveryModel.DeliveryRequest;
import project.model.orderItemModel.OrderItemResponse;
import project.model.orderModel.OrderResponse;
import project.model.orderModel.ReorderResponse;
import project.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Tag(name = "Order")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1")
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
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Failed validation")})
    @PostMapping("/orders/new/withDelivery")
    ResponseEntity<?> createOrderWithDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemService.getShoppingCartItemsWithAdditives(email);
        Order savedOrder;
        if(deliveryRequest.getCallBack()){
            savedOrder = orderService.createOrder(shoppingCartItems.get(0).getShoppingCart(),OrderStatus.CALL);
        } else {
            savedOrder = orderService.createOrder(shoppingCartItems.get(0).getShoppingCart(),OrderStatus.ORDERED);
        }
        deliveryService.createDelivery(deliveryRequest,savedOrder);
        orderItemService.createOrderItems(shoppingCartItems,savedOrder);
        shoppingCartItemService.deleteShoppingCartItems(shoppingCartItems);
        shoppingCartService.resetShoppingCart(email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Create order without delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/orders/new")
    ResponseEntity<?> createOrder(){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemService.getShoppingCartItemsWithAdditives(email);
        Order savedOrder = orderService.createOrder(shoppingCartItems.get(0).getShoppingCart(),OrderStatus.ORDERED);
        orderItemService.createOrderItems(shoppingCartItems,savedOrder);
        shoppingCartItemService.deleteShoppingCartItemsByUserEmail(email);
        shoppingCartService.resetShoppingCart(email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Operation(summary = "Get orders for user order history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Orders not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/orders/history")
    ResponseEntity<?> getOrdersForOrderHistory(PageableDTO pageableDTO){
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
        else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Page<OrderResponse> orderResponses = orderService.getUserOrders(email,pageable);
        if(orderResponses.getContent().size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderResponses,HttpStatus.OK);
    }
    @Operation(summary = "Get order items for order in order history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Orders not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/orders/history/{orderId}")
    ResponseEntity<?> getOrderItemsForOrderHistory(@PathVariable("orderId")Long id, PageableDTO pageableDTO){
        Pageable pageable;
        Sort sort;
        if(pageableDTO.getSortDirection().equals("DESC")){
            sort = Sort.by(pageableDTO.getSortField()).descending();
        }
        else{
            sort = Sort.by(pageableDTO.getSortField()).ascending();
        }
        pageable = PageRequest.of(pageableDTO.getPage(), pageableDTO.getSize(),sort);
        Page<OrderItemResponse> orderItemResponses = orderItemService.getOrderItemsByOrderId(id,pageable);
        if(orderItemResponses.getContent().size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderItemResponses,HttpStatus.OK);
    }
    @Operation(summary = "Get order with new prices for reordering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/orders/reorder/{orderId}")
    ResponseEntity<?> getOrderForReorder(@PathVariable("orderId")Long id){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderService.getOrderById(id);
        ReorderResponse reorderResponse = new ReorderResponse();
        reorderResponse.setOrderId(id);
        List<OrderItemResponse> orderItemResponses = orderItemService.getOrderItemsWithAdditivesByOrderId(id);
        reorderResponse.setOrderItemResponses(orderItemResponses);
        reorderResponse.setPrice(orderService.getOrderPrice(id));
        return new ResponseEntity<>(reorderResponse,HttpStatus.OK);
    }
    @Operation(summary = "Reorder order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/orders/reorder/{orderId}")
    ResponseEntity reorder(@PathVariable("orderId")Long id){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Order order = orderService.getOrderById(id);
        Order newOrder = OrderMapper.orderToNewOrder(order);
        newOrder.setPrice(orderService.getOrderPrice(id));
        Order savedOrder = orderService.saveOrder(newOrder);
        orderItemService.saveNewOrderItems(order.getId(), savedOrder);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Reorder order with delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/orders/reorder/withDelivery/{orderId}")
    ResponseEntity reorderWithDelivery(@PathVariable("orderId")Long id,@Valid @RequestBody DeliveryRequest deliveryRequest){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Delivery delivery = DeliveryMapper.DELIVERY_MAPPER.deliveryRequestToDelivery(deliveryRequest);
        Order order = orderService.getOrderById(id);
        Order newOrder = OrderMapper.orderToNewOrder(order);
        if(deliveryRequest.getCallBack()){
            newOrder.setStatus(OrderStatus.CALL);
        } else {
            newOrder.setStatus(OrderStatus.ORDERED);
        }
        newOrder.setPrice(orderService.getOrderPrice(id));
        Order savedOrder = orderService.saveOrder(newOrder);
        delivery.setOrder(savedOrder);
        deliveryService.saveDelivery(delivery);
        orderItemService.saveNewOrderItems(order.getId(), savedOrder);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
