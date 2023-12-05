package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
