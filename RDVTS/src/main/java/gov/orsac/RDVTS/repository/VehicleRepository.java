package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.entities.VehicleMaster;

public interface VehicleRepository {
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
}
