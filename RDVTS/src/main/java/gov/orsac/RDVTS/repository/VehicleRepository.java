package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.VehicleFilterDto;
import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.dto.VehicleTypeDto;
import gov.orsac.RDVTS.entities.VehicleMaster;

import java.util.List;

public interface VehicleRepository {
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
    List<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle);
    List<VehicleTypeDto> getVehicleTypeList();
}
