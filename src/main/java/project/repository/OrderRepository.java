package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.Order;

import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {
    @Query(value = "select sum(order_item.price) from order_item inner join orders on order_item.order_id = orders.id where orders.id = :id",nativeQuery = true)
    BigDecimal findOrderSum(@Param("id")Long orderId);
}
