package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.*;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface VehicleRepository {
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
    VehicleDeviceInfo getVehicleDeviceMapping(Integer vehicleId);

    List<VehicleMasterDto> getVehicleHistoryList(int id);


    List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId);


    VehicleWorkMappingDto getVehicleWorkMapping(Integer activityId);
    Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle, List<Integer> distIds,List<Integer>divisionIds);
    List<VehicleTypeDto> getVehicleTypeList();
    List<VehicleMasterDto> getUnAssignedVehicleData(List<Integer> userIdList,Integer userId);

    List<VehicleMasterDto> getVehicleById(Integer id, Integer userId);


    Integer deactivateVehicleWork(List<Integer> workIds, List<Integer> vehicleIds) throws ParseException;

    Integer deactivateVehicleActivity(List<Integer> activityIds, List<Integer> vehicleIds) throws ParseException;
}
