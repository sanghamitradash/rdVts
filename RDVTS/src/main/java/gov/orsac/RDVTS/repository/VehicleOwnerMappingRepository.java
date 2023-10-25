package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.entities.VehicleOwnerMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VehicleOwnerMappingRepository extends JpaRepository<VehicleOwnerMappingEntity, Integer> {
    @Query(value="select * from rdvts_oltp.vehicle_owner_mapping where vehicle_id=:vehicleId and is_active=:active",nativeQuery = true)
    VehicleOwnerMappingEntity findByVehicleIdAndActive(Integer vehicleId,boolean active);
//    VehicleOwnerMappingEntity findByVehicleIdAndActive(Integer vehicleId);
}
