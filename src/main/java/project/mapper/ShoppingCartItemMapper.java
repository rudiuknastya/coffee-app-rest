package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import project.entity.Additive;
import project.entity.ShoppingCartItem;
import project.model.shoppingCartItemModel.ShoppingCartItemResponse;

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

    default List<String> createAdditives(ShoppingCartItem shoppingCartItem) {
        List<String> additives = new ArrayList<>(shoppingCartItem.getAdditives().size());
        for (Additive additive : shoppingCartItem.getAdditives()) {
            additives.add(additive.getName());
        }
        return additives;
    }
}
