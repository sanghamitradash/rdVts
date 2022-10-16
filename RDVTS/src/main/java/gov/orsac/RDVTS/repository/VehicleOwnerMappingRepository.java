package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.entities.VehicleOwnerMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleOwnerMappingRepository extends JpaRepository<VehicleOwnerMappingEntity, Integer> {
    VehicleOwnerMappingEntity findByVehicleId(Integer vehicleId);
}
