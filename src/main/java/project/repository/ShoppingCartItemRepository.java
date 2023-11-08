package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    @Query(value = "SELECT s FROM ShoppingCartItem s LEFT JOIN FETCH s.additives LEFT JOIN FETCH s.shoppingCart sc WHERE sc.user.email = :email ")
    List<ShoppingCartItem> findShoppingCartItemsWithAdditives(@Param("email")String email);
}
