package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.additiveTypes a WHERE a.deleted=false AND a.status=true AND p.id= :id")
    Optional<Product> findProductWithAdditiveTypesById(@Param("id")Long id);
    @Query(value = "SELECT p FROM Product p WHERE p.deleted=false AND p.status=true AND p.price < 80")
    List<Product> findProductsForAward();
    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.users u WHERE u.email = :email")
    Page<Product> findUserProducts(@Param("email")String email, Pageable pageable);
}
