package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface VehicleService {
    VehicleMaster saveVehicle(VehicleMaster vehicle);
    Integer deactivateVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping) throws ParseException;
    VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping) throws ParseException;
    VehicleOwnerMappingEntity assignVehicleOwner(VehicleOwnerMappingDto vehicleOwnerMapping);
    Integer deactivateVehicleWork(List<VehicleWorkMappingDto> vehicleWorkMapping) throws ParseException;
    List<VehicleWorkMappingEntity> assignVehicleWork(List<VehicleWorkMappingDto> vehicleWorkMapping) throws ParseException;
    VehicleMasterDto getVehicleByVId(Integer vehicleId);
    VehicleDeviceInfo getVehicleDeviceMapping(Integer vehicleId);

    List<VehicleMasterDto> getVehicleHistoryList(int id);
    List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId);

    VehicleWorkMappingDto getVehicleWorkMapping(Integer activityId);
    LocationDto getLocation(Integer vehicleId) throws ParseException;
    List<LocationDto> getLocationArray(int id) throws ParseException;
    List<AlertDto>getAlert(Integer vehicleId) throws ParseException;
    List<AlertDto>getAlertArray(int id) throws ParseException;
    int getvehicleCountByWorkId(int id);

    List<RoadMasterDto>getRoadArray(int id) throws ParseException;
    List<VehicleDeviceInfo> getVehicleDeviceMappingAssignedList(Integer vehicleId) throws ParseException;
    List<VehicleWorkMappingDto> getVehicleWorkMappingList(List<Integer> activityIds) throws ParseException;

    VehicleMaster updateVehicle(int id, VehicleMaster vehicle);
    Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle);
    List<VehicleTypeDto> getVehicleTypeList();
    List<VehicleMasterDto> getUnAssignedVehicleData(Integer userId);
    List<UserInfoDto> getUserDropDownForVehicleOwnerMapping(Integer userId);
    Page<VehicleMasterDto> getVehicleList1(VehicleMasterDto vehicle);

    List<VehicleMasterDto> getVehicleById(Integer id, Integer userId);

    List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId, Date vehicleWorkStartDate,Date vehicleWorkEndDate,Integer userId) throws ParseException;


    VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping, Integer id);

    List<VehicleActivityMappingEntity> addVehicleActivityMapping(List<VehicleActivityMappingEntity> vehicleActivityMappingEntity);

    List<VehicleActivityMappingDto> getVehicleByActivityId(Integer activityId, Integer userId);

    List<VehicleMasterDto> getVehicleByVehicleTypeId(Integer vehicleTypeId);

    List<RoadMasterDto> getRoadDetailByVehicleId(Integer vehicleId);

    ActivityInfoDto getLiveActivityByVehicleId(Integer vehicleId);
    List<ActivityInfoDto> getActivityListByVehicleId(Integer vehicleId);


    Boolean deactivateVehicle(Integer vehicleId, Integer status);

    Boolean deactivateDeviceVehicleMapping(Integer vehicleId, Integer status);

    Boolean deactivateVehicleActivityMapping(Integer vehicleId, Integer status);

    Integer getTotalCount(Integer vehicleId);

    List<VehicleActivityMappingEntity> assignVehicleActivity(List<VehicleActivityDto> activity) throws ParseException;

    List<VehicleActivityMappingDto> getVehicleByActivityId(Integer activityId, Integer userId, Date actualActivityStartDate, Date actualActivityCompletionDate);
}
