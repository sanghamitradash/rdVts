package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleDeviceMappingRepository extends JpaRepository<VehicleDeviceMappingEntity, Integer> {
}
