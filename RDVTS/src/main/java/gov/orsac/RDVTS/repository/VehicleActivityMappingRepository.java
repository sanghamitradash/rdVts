package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.VehicleActivityMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleActivityMappingRepository extends JpaRepository<VehicleActivityMappingEntity, Integer> {
}
