package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.entities.VehicleWorkMappingEntity;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface VehicleService {
    VehicleMaster saveVehicle(VehicleMaster vehicle);
    VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping);
    List<VehicleWorkMappingEntity> assignVehicleWork(List<VehicleWorkMappingDto> vehicleWorkMapping) throws ParseException;
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
    VehicleDeviceMappingDto getVehicleDeviceMapping(Integer vehicleId);
    List<VehicleWorkMappingDto> getVehicleWorkMapping(Integer vehicleId);

    VehicleMaster updateVehicle(int id, VehicleMaster vehicle);
    Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle);
    List<VehicleTypeDto> getVehicleTypeList();
    List<VehicleMasterDto> getUnAssignedVehicleData(Integer userId);
    Page<VehicleMasterDto> getVehicleList1(VehicleMasterDto vehicle);
}
