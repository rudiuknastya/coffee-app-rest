package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import project.entity.Additive;
import project.entity.Product;
import project.entity.ShoppingCart;
import project.entity.ShoppingCartItem;
import project.model.shoppingCartItemModel.ShoppingCartItemResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Mapper(componentModel = "spring")
public interface ShoppingCartItemMapper {
    ShoppingCartItemMapper SHOPPING_CART_ITEM_MAPPER = Mappers.getMapper(ShoppingCartItemMapper.class);
    @Mapping(target = "additives", expression = "java(createAdditives(shoppingCartItem))")
    List<ShoppingCartItemResponse> shoppingCartItemListToShoppingCartItemResponseList(List<ShoppingCartItem>shoppingCartItems);
    @Mapping(target = "additives", expression = "java(createAdditives(shoppingCartItem))")
    @Mapping(target = "productName", source = "product.name")
    ShoppingCartItemResponse shoppingCartItemToShoppingCartItemResponse(ShoppingCartItem shoppingCartItem);
    @Mapping(target = "price", source = "price")
    @Mapping(target = "quantity", expression = "java(1L)")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "shoppingCart", source = "shoppingCart")
    @Mapping(ignore = true, target = "id")
    ShoppingCartItem createShoppingCart(Product product, ShoppingCart shoppingCart, BigDecimal price);
    default List<String> createAdditives(ShoppingCartItem shoppingCartItem) {
        List<String> additives = new ArrayList<>(shoppingCartItem.getAdditives().size());
        for (Additive additive : shoppingCartItem.getAdditives()) {
            additives.add(additive.getName());
        }
        return additives;
    }
}
