package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.Delivery;
import project.model.deliveryModel.DeliveryRequest;
@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryMapper DELIVERY_MAPPER = Mappers.getMapper(DeliveryMapper.class);
    Delivery deliveryRequestToDelivery(DeliveryRequest deliveryRequest);
}
