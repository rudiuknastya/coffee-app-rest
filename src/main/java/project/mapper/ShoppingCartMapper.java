package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import project.entity.ShoppingCart;
import project.entity.ShoppingCartItem;
import project.model.shoppingCartItemModel.ShoppingCartItemResponse;
import project.model.shoppingCartModel.ShoppingCartPriceResponse;
import project.model.shoppingCartModel.ShoppingCartResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartMapper SHOPPING_CART_MAPPER = Mappers.getMapper(ShoppingCartMapper.class);
    @Mapping(target="id", source="id")
    @Mapping(target="price", source="price")
    ShoppingCartPriceResponse shoppingCartToShoppingCartPriceResponse(ShoppingCart shoppingCart);
    @Mapping(target="id", source="shoppingCart.id")
    @Mapping(target="price", source="shoppingCart.price")
    @Mapping(target="shoppingCartItems", expression="java(getShoppingCartItemResponses(shoppingCartItems))")
    ShoppingCartResponse shoppingCartItemsToShoppingCartResponse(List<ShoppingCartItem> shoppingCartItems, ShoppingCart shoppingCart);
    default List<ShoppingCartItemResponse> getShoppingCartItemResponses(List<ShoppingCartItem> shoppingCartItems){
        return ShoppingCartItemMapper.SHOPPING_CART_ITEM_MAPPER.shoppingCartItemListToShoppingCartItemResponseList(shoppingCartItems);
    }
}
