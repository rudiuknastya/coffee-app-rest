package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.ShoppingCartItem;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
}
