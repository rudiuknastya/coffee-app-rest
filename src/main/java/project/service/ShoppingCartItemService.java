package project.service;

import project.entity.ShoppingCartItem;
import project.model.shoppingCartModel.ShoppingCartResponse;

import java.util.List;

public interface ShoppingCartItemService {
    ShoppingCartItem saveShoppingCartItem(ShoppingCartItem shoppingCartItem);
    ShoppingCartResponse getShoppingCartResponse(String email);
    ShoppingCartItem getShoppingCartItemWithAdditivesById(Long id);
    ShoppingCartItem getShoppingCartItemById(Long id);
    void deleteShoppingCartItem(ShoppingCartItem shoppingCartItem);
    void deleteShoppingCartItemsByUserEmail(String email);
    List<ShoppingCartItem> getShoppingCartItemsWithAdditives(String email);
    void deleteShoppingCartItems(List<ShoppingCartItem> shoppingCartItems);
}
