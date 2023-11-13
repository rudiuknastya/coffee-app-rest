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

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ProductService productService, AdditiveService additiveService, ShoppingCartItemService shoppingCartItemService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.productService = productService;
        this.additiveService = additiveService;
        this.shoppingCartItemService = shoppingCartItemService;
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
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("/shoppingCart")
    ResponseEntity<ShoppingCartResponse> getShoppingCart(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        return new ResponseEntity<>(shoppingCartItemService.getShoppingCartResponse(email), HttpStatus.OK);
    }
    @Operation(summary = "Delete shopping cart item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @DeleteMapping("/shoppingCart/delete/{shoppingCartItemId}")
    ResponseEntity<?> deleteShoppingCartItem(@PathVariable("shoppingCartItemId")Long id){
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
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Regulate quantity of shopping cart items")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "User unauthorized"),
        @ApiResponse(responseCode = "400", description = "Bad request")})
    @PutMapping("/shoppingCart/edit/{shoppingCartItemId}")
    ResponseEntity<ShoppingCartItemQuantityResponse> updateShoppingCartItemQuantity(@PathVariable("shoppingCartItemId")Long id, @RequestParam Long quantity){
        ShoppingCartItem shoppingCartItem = shoppingCartItemService.getShoppingCartItemById(id);
        BigDecimal p = shoppingCartItem.getPrice();
        p = p.divide(BigDecimal.valueOf(shoppingCartItem.getQuantity()));
        p = p.multiply(BigDecimal.valueOf(quantity));

        BigDecimal op = shoppingCartItem.getShoppingCart().getPrice();
        op = op.subtract(shoppingCartItem.getPrice());
        shoppingCartItem.setPrice(p);
        op = op.add(shoppingCartItem.getPrice());
        shoppingCartItem.getShoppingCart().setPrice(op);
        shoppingCartItem.setQuantity(quantity);
        shoppingCartItemService.saveShoppingCartItem(shoppingCartItem);
        ShoppingCartItemQuantityResponse response = new ShoppingCartItemQuantityResponse(id,quantity,shoppingCartItem.getPrice(),shoppingCartItem.getShoppingCart().getPrice());
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

}
