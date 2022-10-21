package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.entities.VehicleWorkMappingEntity;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface VehicleRepository {
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
    VehicleDeviceInfo getVehicleDeviceMapping(Integer vehicleId);

    List<VehicleMasterDto> getVehicleHistoryList(int id);


    List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId);


    List<VehicleWorkMappingDto> getVehicleWorkMapping(Integer vehicleId);
    Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle);
    List<VehicleTypeDto> getVehicleTypeList();
    List<VehicleMasterDto> getUnAssignedVehicleData(List<Integer> userIdList,Integer userId);

    List<VehicleMasterDto> getVehicleById(Integer id, Integer userId);


    Integer deactivateVehicleWork(List<Integer> workIds, List<Integer> vehicleIds) throws ParseException;
}
