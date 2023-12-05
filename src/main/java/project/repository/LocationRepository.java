package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.entity.Location;

public interface LocationRepository extends JpaRepository<Location,Long>, JpaSpecificationExecutor<Location> {
}
