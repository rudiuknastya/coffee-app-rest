package project.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import project.entity.Additive;
import project.entity.Order;
import project.entity.OrderItem;
import project.entity.ShoppingCartItem;
import project.model.orderItemModel.OrderItemResponse;
import project.repository.OrderItemRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemMapper ORDER_ITEM_MAPPER = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(ignore = true, target = "id")
    @Mapping(target = "price", source = "orderItemPrice")
    @Mapping(target = "order", source = "otOrder")
    OrderItem orderItemToNewOrderItem(OrderItem orderItem, BigDecimal orderItemPrice, Order otOrder);

    List<OrderItem> shoppingCartItemListToOrderItemList(List<ShoppingCartItem> shoppingCartItems, @Context Order orderToSet);

    @Mapping(target = "deleted", expression = "java(false)")
    @Mapping(ignore = true, target = "id")
    @Mapping(target = "price", source = "shoppingCartItem.price")
    OrderItem shoppingCartItemToOrderItem(ShoppingCartItem shoppingCartItem, @Context Order orderToSet);

    @AfterMapping
    default void afterMapping(@MappingTarget OrderItem target, @Context Order orderToSet) {
        target.setOrder(orderToSet);
    }

    List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> orderItems);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "additives", expression = "java(createAdditives(orderItem))")
    OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "productName", source = "orderItem.product.name")
    @Mapping(target = "price", source = "newPrice")
    @Mapping(target = "additives", expression = "java(createAdditives(orderItem))")
    OrderItemResponse oldOrderItemToOrderItemResponse(OrderItem orderItem, BigDecimal newPrice);

    default List<String> createAdditives(OrderItem orderItem) {
        List<String> additives = new ArrayList<>(orderItem.getAdditives().size());
        for (Additive additive : orderItem.getAdditives()) {
            additives.add(additive.getName());
        }
        return additives;
    }
}
