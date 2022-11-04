package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.RoadLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadLocationRepository extends JpaRepository<RoadLocationEntity, Integer> {
}
