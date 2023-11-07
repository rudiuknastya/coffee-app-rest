package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
}
