package project.service;

import project.entity.Delivery;
import project.entity.Order;
import project.model.deliveryModel.DeliveryRequest;

public interface DeliveryService {
    void createDelivery(DeliveryRequest deliveryRequest, Order order);
}
