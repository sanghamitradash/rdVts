package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface VehicleService {
    VehicleMaster saveVehicle(VehicleMaster vehicle);
    VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping);
    VehicleOwnerMappingEntity assignVehicleOwner(VehicleOwnerMappingDto vehicleOwnerMapping);
    List<VehicleWorkMappingEntity> assignVehicleWork(List<VehicleWorkMappingDto> vehicleWorkMapping) throws ParseException;
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
    VehicleDeviceInfo getVehicleDeviceMapping(Integer vehicleId);


    List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId);




    List<VehicleWorkMappingDto> getVehicleWorkMapping(Integer vehicleId);
    List<LocationDto> getLocation(Integer vehicleId) throws ParseException;
    List<AlertDto>getAlert(Integer vehicleId) throws ParseException;
    List<VehicleDeviceInfo> getVehicleDeviceMappingAssignedList(Integer vehicleId) throws ParseException;
    List<VehicleWorkMappingDto> getVehicleWorkMappingList(Integer vehicleId) throws ParseException;

    VehicleMaster updateVehicle(int id, VehicleMaster vehicle);
    Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle);
    List<VehicleTypeDto> getVehicleTypeList();
    List<VehicleMasterDto> getUnAssignedVehicleData(Integer userId);
    Page<VehicleMasterDto> getVehicleList1(VehicleMasterDto vehicle);

    List<VehicleMasterDto> getVehicleById(Integer id, Integer userId);

    List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId);

}
