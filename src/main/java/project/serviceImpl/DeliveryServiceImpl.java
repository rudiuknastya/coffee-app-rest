package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.Delivery;
import project.repository.DeliveryRepository;
import project.service.DeliveryService;
@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Delivery saveDelivery(Delivery delivery) {
        logger.info("saveDelivery() - Saving delivery");
        Delivery delivery1 = deliveryRepository.save(delivery);
        logger.info("saveDelivery() - Delivery was saved");
        return delivery1;
    }
}
