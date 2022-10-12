package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;

public interface VehicleService {
    VehicleMaster saveVehicle(VehicleMaster vehicle);
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
    VehicleMaster updateVehicle(int id, VehicleMaster vehicle);
}
