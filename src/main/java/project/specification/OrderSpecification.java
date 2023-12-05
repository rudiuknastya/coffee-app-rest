package project.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Order;
import project.entity.User;

public interface OrderSpecification {
    static Specification<Order> byUserEmail(String email){
        return (root, query, builder) -> {
            Join<Order, User> userJoin = root.join("user");
            return builder.equal(userJoin.get("email"), email);
        };
    }
}
