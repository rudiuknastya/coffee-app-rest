package project.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Order;
import project.entity.OrderItem;

public interface OrderItemSpecification {
    static Specification<OrderItem> byOrderId(Long orderId){
        return (root, query, builder) -> {
            Join<OrderItem, Order> orderJoin = root.join("order");
            return builder.equal(orderJoin.get("id"), orderId);
        };
    }
}
