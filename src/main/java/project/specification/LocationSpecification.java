package project.specification;

import org.springframework.data.jpa.domain.Specification;
import project.entity.Category;
import project.entity.Location;

public interface LocationSpecification {
    static Specification<Location> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
}
