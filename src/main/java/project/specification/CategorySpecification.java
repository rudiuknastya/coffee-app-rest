package project.specification;

import org.springframework.data.jpa.domain.Specification;
import project.entity.Category;

public interface CategorySpecification {
    static Specification<Category> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
    static Specification<Category> byStatus(){
        return (root, query, builder) ->
                builder.equal(root.get("status"), true);
    }
}
