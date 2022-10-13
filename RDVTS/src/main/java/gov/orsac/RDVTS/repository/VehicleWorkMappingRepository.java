package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleWorkMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleWorkMappingRepository extends JpaRepository<VehicleWorkMappingEntity, Integer> {
}
