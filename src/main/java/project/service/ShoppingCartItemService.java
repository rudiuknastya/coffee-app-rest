package project.service;

import project.entity.ShoppingCartItem;
import project.model.shoppingCartModel.ShoppingCartResponse;

public interface ShoppingCartItemService {
    ShoppingCartItem saveShoppingCartItem(ShoppingCartItem shoppingCartItem);
    ShoppingCartResponse getShoppingCartResponse(String email);
    ShoppingCartItem getShoppingCartItemById(Long id);
    void deleteShoppingCartItem(ShoppingCartItem shoppingCartItem);
}
