package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import project.entity.Delivery;
import project.entity.Order;
import project.model.deliveryModel.DeliveryRequest;
@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryMapper DELIVERY_MAPPER = Mappers.getMapper(DeliveryMapper.class);
    @Mapping(target = "order", source = "savedOrder")
    @Mapping(ignore = true, target = "id")
    Delivery deliveryRequestToDelivery(DeliveryRequest deliveryRequest, Order savedOrder);
}
