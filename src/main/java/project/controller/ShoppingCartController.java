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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.entity.*;
import project.model.shoppingCartItemModel.ShoppingCartItemRequest;
import project.service.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Shopping cart")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final LocationService locationService;
    private final UserService userService;
    private final ProductService productService;
    private final AdditiveService additiveService;
    private final ShoppingCartItemService shoppingCartItemService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, LocationService locationService, UserService userService, ProductService productService, AdditiveService additiveService, ShoppingCartItemService shoppingCartItemService) {
        this.shoppingCartService = shoppingCartService;
        this.locationService = locationService;
        this.userService = userService;
        this.productService = productService;
        this.additiveService = additiveService;
        this.shoppingCartItemService = shoppingCartItemService;
    }

    @Operation(summary = "Create shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/shoppingCart/create")
    ResponseEntity<?> createShoppingCart(@RequestParam Long locationId){
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setPrice(BigDecimal.valueOf(0));
        shoppingCart.setLocation(locationService.getLocationById(locationId));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        shoppingCart.setUser(userService.getUserByEmail(email));
        shoppingCartService.saveShoppingCart(shoppingCart);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Add product to shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @PostMapping("/shoppingCart/add/{productId}")
    ResponseEntity<?> createShoppingCartItem(@PathVariable Long productId, @RequestBody ShoppingCartItemRequest shoppingCartItemRequest){
        BigDecimal price = new BigDecimal(0);
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setQuantity(shoppingCartItemRequest.getQuantity());
        Product product = productService.getProductById(productId);
        price = price.add(product.getPrice());
        shoppingCartItem.setProduct(product);
        List<Additive> additives = additiveService.getAdditivesById(shoppingCartItemRequest.getAdditiveIds());
        for(Additive additive: additives){
            price = price.add(additive.getPrice());
        }
        price = price.multiply(BigDecimal.valueOf(shoppingCartItemRequest.getQuantity()));
        shoppingCartItem.setPrice(price);
        shoppingCartItem.setAdditives(additives);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        User user = userService.getUserWithShoppingCartByEmail(email);
        user.getShoppingCart().setPrice(user.getShoppingCart().getPrice().add(price));
        shoppingCartItem.setShoppingCart(user.getShoppingCart());
        shoppingCartItemService.saveShoppingCartItem(shoppingCartItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
