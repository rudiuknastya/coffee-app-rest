package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
    @Query(value = "SELECT oi FROM OrderItem oi LEFT JOIN FETCH oi.additives LEFT JOIN FETCH oi.order o WHERE o.id = :id ")
    List<OrderItem> findWithAdditivesByOrderId(@Param("id")Long orderId);
    @Query(value = "select sum(additive.price) from additive inner join order_item_additive on order_item_additive.additive_id = additive.id where order_item_id = :id",nativeQuery = true)
    BigDecimal findOrderItemAdditivesSum(@Param("id")Long orderItemId);
}
