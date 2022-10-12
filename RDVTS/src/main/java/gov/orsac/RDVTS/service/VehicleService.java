package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.VehicleFilterDto;
import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.dto.VehicleTypeDto;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.entities.VehicleWorkMappingEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleService {
    VehicleMaster saveVehicle(VehicleMaster vehicle);
    VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping);
    List<VehicleWorkMappingEntity> assignVehicleWork(List<VehicleWorkMappingEntity> vehicleWorkMapping);
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
    VehicleMaster updateVehicle(int id, VehicleMaster vehicle);
    List<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle);
    List<VehicleTypeDto> getVehicleTypeList();
    Page<VehicleMasterDto> getVehicleList1(VehicleMasterDto vehicle);
}
