package project.service;

import project.entity.ShoppingCart;
import project.model.shoppingCartModel.ShoppingCartPriceResponse;
import project.model.shoppingCartModel.ShoppingCartResponse;

public interface ShoppingCartService {
    ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);
    ShoppingCartPriceResponse getShoppingCartPrice(String email);
    void deleteShoppingCartByUserEmail(String email);
    void deleteShoppingCart(ShoppingCart shoppingCart);
}
