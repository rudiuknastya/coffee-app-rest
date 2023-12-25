package project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/v1")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartItemService shoppingCartItemService;
    private final LocationService locationService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, ShoppingCartItemService shoppingCartItemService, LocationService locationService) {
        this.shoppingCartService = shoppingCartService;
        this.shoppingCartItemService = shoppingCartItemService;
        this.locationService = locationService;
    }

    @Operation(summary = "Add product to shopping cart",description = "Adding product to shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Product not found",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PostMapping("/shoppingCart/add/{productId}")
    ResponseEntity<?> createShoppingCartItem(@PathVariable
                                             @Parameter(name = "productId", description = "Product id", example = "1")
                                             Long productId, @RequestBody ShoppingCartItemRequest shoppingCartItemRequest){
        if(productId < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shoppingCartItemService.createShoppingCartItem(productId,shoppingCartItemRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Get shopping cart price",description = "Getting shopping cart price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = ShoppingCartPriceResponse.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/shoppingCart/price")
    ResponseEntity<ShoppingCartPriceResponse> getShoppingCartPrice(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        ShoppingCartPriceResponse shoppingCart = shoppingCartService.getShoppingCartPrice(email);
        return new ResponseEntity<>(shoppingCart,HttpStatus.OK);
    }
    @Operation(summary = "Get shopping cart",description = "Getting shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = ShoppingCartResponse.class))}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @GetMapping("/shoppingCart")
    ResponseEntity<?> getShoppingCart(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        ShoppingCartResponse shoppingCartResponse = shoppingCartService.getShoppingCartResponse(email);
        return new ResponseEntity<>(shoppingCartResponse, HttpStatus.OK);
    }
    @Operation(summary = "Delete shopping cart item by id",description = "Deleting shopping cart item by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Shopping cart item not found",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @DeleteMapping("/shoppingCart/delete/{shoppingCartItemId}")
    ResponseEntity<?> deleteShoppingCartItem(@PathVariable("shoppingCartItemId")
                                             @Parameter(name = "shoppingCartItemId", description = "Shopping cart item id", example = "1")
                                             Long id){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ShoppingCartItem shoppingCartItem = shoppingCartItemService.getShoppingCartItemWithAdditivesById(id);
        shoppingCartItem.getShoppingCart().setPrice(shoppingCartItem.getShoppingCart().getPrice().subtract(shoppingCartItem.getPrice()) );
        shoppingCartItemService.deleteShoppingCartItem(shoppingCartItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Delete shopping cart", description = "Deleting all products in shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @DeleteMapping("/shoppingCart/delete")
    ResponseEntity<?> deleteShoppingCart(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        shoppingCartItemService.deleteShoppingCartItemsByUserEmail(email);
        shoppingCartService.resetShoppingCart(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Operation(summary = "Regulate quantity of shopping cart items",description = "Update quantity of shopping cart items in shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Shopping cart item not found",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PutMapping("/shoppingCart/edit/{shoppingCartItemId}")
    ResponseEntity<ShoppingCartItemQuantityResponse> updateShoppingCartItemQuantity(@PathVariable("shoppingCartItemId")
                                                                                    @Parameter(name = "shoppingCartItemId", description = "Shopping cart item id", example = "1")
                                                                                    Long id,
                                                                                    @Parameter(name = "quantity", description = "Shopping cart item quantity", example = "3")
                                                                                    @RequestParam Long quantity){
        if(id < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ShoppingCartItem shoppingCartItem = shoppingCartItemService.updateShoppingCartItem(id,quantity);
        ShoppingCartItemQuantityResponse response = new ShoppingCartItemQuantityResponse(id,quantity,shoppingCartItem.getPrice(),shoppingCartItem.getShoppingCart().getPrice());
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
    @Operation(summary = "Set location in shopping cart", description = "Setting location in shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "401", description = "User unauthorized",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Location not found",content = {@Content(mediaType = "application/json",schema = @Schema())}),
            @ApiResponse(responseCode = "400", description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema())})})
    @PutMapping("/shoppingCart/setLocation/{locationId}")
    ResponseEntity<?> setLocation(@PathVariable
                                  @Parameter(name = "locationId", description = "Location id", example = "1")
                                  Long locationId){
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
