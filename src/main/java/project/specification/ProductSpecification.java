package project.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Category;
import project.entity.Location;
import project.entity.Product;

public interface ProductSpecification {
    static Specification<Product> byCategoryId(Long categoryId){
        return (root, query, builder) -> {
            Join<Product, Category> productJoin = root.join("category");
            return builder.equal(productJoin.get("id"), categoryId);
        };
    }
    static Specification<Product> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
    static Specification<Product> byStatus(){
        return (root, query, builder) ->
                builder.equal(root.get("status"), true);
    }
}
