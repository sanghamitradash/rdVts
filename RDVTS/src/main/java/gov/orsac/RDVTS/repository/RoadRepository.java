package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.RoadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadRepository extends JpaRepository<RoadEntity, Integer> {
}
