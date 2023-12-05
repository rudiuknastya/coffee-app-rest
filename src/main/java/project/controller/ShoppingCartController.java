package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.entity.*;
import project.model.shoppingCartItemModel.ShoppingCartItemQuantityResponse;
import project.model.shoppingCartItemModel.ShoppingCartItemRequest;
import project.model.shoppingCartModel.ShoppingCartPriceResponse;
import project.model.shoppingCartModel.ShoppingCartResponse;
import project.service.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Shopping cart")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ProductService productService;
    private final AdditiveService additiveService;
    private final ShoppingCartItemService shoppingCartItemService;
    private final LocationService locationService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ProductService productService, AdditiveService additiveService, ShoppingCartItemService shoppingCartItemService, LocationService locationService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.productService = productService;
        this.additiveService = additiveService;
        this.shoppingCartItemService = shoppingCartItemService;
        this.locationService = locationService;
    }

    @Operation(summary = "Add product to shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/shoppingCart/add/{productId}")
    ResponseEntity<?> createShoppingCartItem(@PathVariable Long productId, @RequestBody ShoppingCartItemRequest shoppingCartItemRequest){
        if(productId < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shoppingCartItemService.createShoppingCartItem(productId,shoppingCartItemRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Operation(summary = "Get shopping cart price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/shoppingCart/price")
    ResponseEntity<ShoppingCartPriceResponse> getShoppingCartPrice(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        ShoppingCartPriceResponse shoppingCart = shoppingCartService.getShoppingCartPrice(email);
        return new ResponseEntity<>(shoppingCart,HttpStatus.OK);
    }
    @Operation(summary = "Get shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Shopping cart empty"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/shoppingCart")
    ResponseEntity<?> getShoppingCart(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        ShoppingCartResponse shoppingCartResponse = shoppingCartItemService.getShoppingCartResponse(email);
        if(shoppingCartResponse == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shoppingCartResponse, HttpStatus.OK);
    }
    @Operation(summary = "Delete shopping cart item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Shopping cart item not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @DeleteMapping("/shoppingCart/delete/{shoppingCartItemId}")
    ResponseEntity<?> deleteShoppingCartItem(@PathVariable("shoppingCartItemId")Long id){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ShoppingCartItem shoppingCartItem = shoppingCartItemService.getShoppingCartItemWithAdditivesById(id);
        shoppingCartItem.getShoppingCart().setPrice(shoppingCartItem.getShoppingCart().getPrice().subtract(shoppingCartItem.getPrice()) );
        shoppingCartItemService.deleteShoppingCartItem(shoppingCartItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Delete shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @DeleteMapping("/shoppingCart/delete")
    ResponseEntity<?> deleteShoppingCart(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        shoppingCartItemService.deleteShoppingCartItemsByUserEmail(email);
        shoppingCartService.resetShoppingCart(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Regulate quantity of shopping cart items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Shopping cart item not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PutMapping("/shoppingCart/edit/{shoppingCartItemId}")
    ResponseEntity<ShoppingCartItemQuantityResponse> updateShoppingCartItemQuantity(@PathVariable("shoppingCartItemId")Long id, @RequestParam Long quantity){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ShoppingCartItem shoppingCartItem = shoppingCartItemService.updateShoppingCartItem(id,quantity);
        ShoppingCartItemQuantityResponse response = new ShoppingCartItemQuantityResponse(id,quantity,shoppingCartItem.getPrice(),shoppingCartItem.getShoppingCart().getPrice());
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
    @Operation(summary = "Set location in shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PutMapping("/shoppingCart/setLocation/{locationId}")
    ResponseEntity<?> setLocation(@PathVariable Long locationId){
        if(locationId < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Location location = locationService.getLocationById(locationId);
        shoppingCartService.setShoppingCartLocation(location,email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
