package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.Additive;
import project.entity.ShoppingCart;
import project.entity.ShoppingCartItem;
import project.model.shoppingCartItemModel.ShoppingCartItemResponse;
import project.model.shoppingCartModel.ShoppingCartPriceResponse;
import project.model.shoppingCartModel.ShoppingCartResponse;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartMapper SHOPPING_CART_MAPPER = Mappers.getMapper(ShoppingCartMapper.class);
    @Mapping(target="id", source="id")
    @Mapping(target="price", source="price")
    ShoppingCartPriceResponse shoppingCartToShoppingCartPriceResponse(ShoppingCart shoppingCart);
    @Named("shoppingCartItemsToShoppingCartResponse")
    static ShoppingCartResponse shoppingCartItemsToShoppingCartResponse(List<ShoppingCartItem> shoppingCartItems){
        if (shoppingCartItems == null || shoppingCartItems.size() == 0){
            return null;
        }
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse();
        shoppingCartResponse.setId(shoppingCartItems.get(0).getShoppingCart().getId());
        shoppingCartResponse.setPrice(shoppingCartItems.get(0).getShoppingCart().getPrice());
        List<ShoppingCartItemResponse> shoppingCartItemResponses = new ArrayList<>(shoppingCartItems.size());
        for (ShoppingCartItem shoppingCartItem: shoppingCartItems){
            ShoppingCartItemResponse shoppingCartItemResponse = new ShoppingCartItemResponse();
            shoppingCartItemResponse.setId(shoppingCartItem.getId());
            shoppingCartItemResponse.setPrice(shoppingCartItem.getPrice());
            shoppingCartItemResponse.setQuantity(shoppingCartItem.getQuantity());
            shoppingCartItemResponse.setProductName(shoppingCartItem.getProduct().getName());
            if(shoppingCartItem.getAdditives() != null) {
                List<String> additives = new ArrayList<>(shoppingCartItem.getAdditives().size());
                for (Additive additive : shoppingCartItem.getAdditives()) {
                    additives.add(additive.getName());
                }
                shoppingCartItemResponse.setAdditives(additives);
            }
            shoppingCartItemResponses.add(shoppingCartItemResponse);
        }
        shoppingCartResponse.setShoppingCartItems(shoppingCartItemResponses);
        return shoppingCartResponse;
    }
}
