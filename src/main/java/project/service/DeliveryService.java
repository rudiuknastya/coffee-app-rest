package project.service;

import project.entity.Delivery;
import project.entity.Order;
import project.model.deliveryModel.DeliveryRequest;

public interface DeliveryService {
    Delivery saveDelivery(Delivery delivery);
    void createDelivery(DeliveryRequest deliveryRequest, Order order);
}
