package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.RoleMenuMaster;
import gov.orsac.RDVTS.entities.VehicleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleMasterSaveRepository extends JpaRepository<VehicleMaster, Integer> {
    VehicleMaster findVehicleById(Integer id);
}
