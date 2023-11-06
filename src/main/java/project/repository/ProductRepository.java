package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.additiveTypes a WHERE a.deleted=false AND a.status=true AND p.id= :id")
    Product findProductWithAdditiveTypesById(@Param("id")Long id);
    @Query(value = "SELECT p FROM Product p WHERE p.deleted=false AND p.status=true AND p.price < 80")
    List<Product> findProductsForAward();
}
