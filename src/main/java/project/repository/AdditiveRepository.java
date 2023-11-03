package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.entity.Additive;

public interface AdditiveRepository extends JpaRepository<Additive, Long>, JpaSpecificationExecutor<Additive> {
}
