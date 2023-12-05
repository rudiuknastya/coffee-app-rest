package project.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import project.entity.Additive;
import project.entity.AdditiveType;

public interface AdditiveSpecification {
    static Specification<Additive> byDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }
    static Specification<Additive> byStatus(){
        return (root, query, builder) ->
                builder.equal(root.get("status"), true);
    }
    static Specification<Additive> byAdditiveTypeId(Long additiveTypeId){
        return (root, query, builder) ->{
            Join<Additive, AdditiveType> additiveTypeJoin = root.join("additiveType");
            return builder.equal(additiveTypeJoin.get("id"), additiveTypeId);
        };

    }
}
