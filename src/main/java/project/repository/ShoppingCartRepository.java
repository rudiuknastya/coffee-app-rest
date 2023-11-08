package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    @Query(value = "SELECT s FROM ShoppingCart s LEFT JOIN s.user u WHERE u.email = :email ")
    ShoppingCart findByUserEmail(@Param("email")String email);
}
