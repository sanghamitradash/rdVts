package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.*;
import gov.orsac.RDVTS.repositoryImpl.LocationRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.UserRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.VehicleRepositoryImpl;
import gov.orsac.RDVTS.service.HelperService;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*import static javafx.scene.input.KeyCode.L;*/

@Service
public class VehicleServiceImpl implements VehicleService {
       @Autowired
       private VehicleMasterSaveRepository vehicleMasterSaveRepository;
       @Autowired
       private VehicleRepository vehicleRepository;
       @Autowired
       private VehicleDeviceRepository vehicleDeviceRepository;
       @Autowired
       private LocationRepositoryImpl locationRepositoryImpl;

       @Autowired
       private VehicleDeviceMappingRepository vehicleDeviceMappingRepository;
     /*  @Autowired
       private VehicleServiceImpl vehicleServiceImpl;*/
       @Autowired
       private VehicleWorkMappingRepository vehicleWorkMappingRepository;

       @Autowired
       private VehicleActivityMappingRepository vehicleActivityMappingRepository;
       @Autowired
       private VehicleOwnerMappingRepository vehicleOwnerMappingRepository;
       @Autowired
       private UserRepositoryImpl userRepositoryImpl;
       @Autowired
       private HelperService helperService;
       @Autowired
       private WorkServiceImpl workServiceImpl;

       @Autowired
       private VehicleRepositoryImpl vehicleRepositoryimpl;

       @Autowired
       private VehicleActivityMappingRepository vehicleActivityMappingRepo;
       @Override
       public VehicleMaster saveVehicle(VehicleMaster vehicle) {
        return vehicleMasterSaveRepository.save(vehicle);
        }

       @Override
       public Integer deactivateVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping) throws ParseException {
            return  vehicleDeviceRepository.deactivateVehicleDevice(vehicleDeviceMapping);
       }

       @Override
       public VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping) throws ParseException {
           Integer count=vehicleDeviceRepository.deactivateVehicleDevice(vehicleDeviceMapping);
           if(vehicleDeviceMapping.getDeviceId()>0 && vehicleDeviceMapping.getVehicleId()>0) {
               vehicleDeviceMapping= vehicleDeviceMappingRepository.save(vehicleDeviceMapping);
           }
           return  vehicleDeviceMapping;
       }

       @Override
       public VehicleOwnerMappingEntity assignVehicleOwner(VehicleOwnerMappingDto vehicleOwnerMapping) {
              VehicleOwnerMappingEntity vehicle=new VehicleOwnerMappingEntity();
              BeanUtils.copyProperties(vehicleOwnerMapping,vehicle);
              return vehicleOwnerMappingRepository.save(vehicle);
       }

       @Override
       public Integer deactivateVehicleWork(List<VehicleWorkMappingDto> vehicleWorkMapping) throws ParseException {
              List<Integer> workIds = new ArrayList<>();
              List<Integer> vehicleIds = new ArrayList<>();
          /*    for (VehicleWorkMappingDto eachWorkIds: vehicleWorkMapping) {
                     workIds.add(eachWorkIds.getWorkId());
              }

              for (VehicleWorkMappingDto eachVehicleIds: vehicleWorkMapping) {
                     vehicleIds.add(eachVehicleIds.getVehicleId());
              }*/
              for (VehicleWorkMappingDto vehicle: vehicleWorkMapping) {
                     vehicleIds.add(vehicle.getVehicleId());
                     workIds.add(vehicle.getWorkId());
              }
              return vehicleRepository.deactivateVehicleWork(workIds, vehicleIds);
       }

       @Override
       public List<VehicleWorkMappingEntity> assignVehicleWork(List<VehicleWorkMappingDto> vehicleWorkMapping) throws ParseException {
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
//              formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));


              Integer count = workServiceImpl.deactivateVehicleWork(vehicleWorkMapping);

              List<VehicleWorkMappingEntity> vehicleWork=new ArrayList<>();
              for(VehicleWorkMappingDto vehicle:vehicleWorkMapping){
                     VehicleWorkMappingEntity vehicle1=new VehicleWorkMappingEntity();
                     Date startTime = formatter.parse(vehicle.getStartTime());
                     Date endTime = formatter.parse(vehicle.getEndTime());

                     BeanUtils.copyProperties(vehicle,vehicle1);
                     vehicle1.setStartTime(startTime);
                     vehicle1.setEndTime(endTime);
                     vehicleWork.add(vehicle1);
              }
              return vehicleWorkMappingRepository.saveAll(vehicleWork);
       }

       @Override
       public VehicleMasterDto getVehicleByVId(Integer vehicleId) {
       return vehicleRepository.getVehicleByVId(vehicleId);
       }
       @Override
       public List<VehicleMasterDto> getVehicleHistoryList(int id) {
           return vehicleRepository.getVehicleHistoryList(id);
//                     List<VehicleMasterDto> vehicleMasterDtoList=new ArrayList<>();
              }
       @Override
       public VehicleDeviceInfo getVehicleDeviceMapping(Integer vehicleId) {
              return vehicleRepository.getVehicleDeviceMapping(vehicleId);
       }

       @Override
       public List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId) {
              return vehicleRepository.getVehicleDeviceMappingList(vehicleId);
       }

       @Override
       public List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId, Date vehicleWorkStartDate,Date vehicleWorkEndDate,Integer userId) throws ParseException {
              return vehicleRepositoryimpl.getdeviceListByVehicleId(vehicleId,vehicleWorkStartDate,vehicleWorkEndDate,userId);
       }

       @Override
       public VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping, Integer id) {
                   vehicleDeviceMapping.setDeviceId(id);
                   vehicleDeviceMapping.setActive(true);
                   return vehicleDeviceMappingRepository.save(vehicleDeviceMapping);
              }

       @Override
       public List<VehicleActivityMappingEntity> addVehicleActivityMapping(List<VehicleActivityMappingEntity> vehicleActivityMappingEntity) {
           return vehicleActivityMappingRepo.saveAll(vehicleActivityMappingEntity);
       }

    @Override
    public List<VehicleActivityMappingDto> getVehicleByActivityId(Integer activityId, Integer userId) {
        return vehicleRepositoryimpl.getVehicleByActivityId(activityId, userId);
    }

    @Override
    public List<VehicleMasterDto> getVehicleByVehicleTypeId(Integer vehicleTypeId) {
        return vehicleRepositoryimpl.getVehicleByVehicleTypeId(vehicleTypeId);
    }

    @Override
    public List<RoadMasterDto> getRoadDetailByVehicleId(Integer vehicleId) {
//           List<Integer> vehicleIdList = new ArrayList<>();
//        if (vehicleId!=null && vehicleId > 0){
//            vehicleIdList=vehicleRepositoryimpl.getRoadIdsByVehicleIdsForFilter(vehicleId);
//        }
//        if(vehicleIdList != null && vehicleId > 0){
//            vehicleIdList.add(vehicleId);
//        }
        return vehicleRepositoryimpl.getRoadDetailByVehicleId(vehicleId);
    }

    @Override
    public ActivityInfoDto getLiveActivityByVehicleId(Integer vehicleId) {
        return vehicleRepositoryimpl.getLiveActivityByVehicleId(vehicleId);
    }

    @Override
    public List<ActivityInfoDto> getActivityListByVehicleId(Integer vehicleId) {
        return vehicleRepositoryimpl.getActivityListByVehicleId(vehicleId);
    }



    @Override
    public Boolean deactivateVehicle(Integer vehicleId, Integer status) {
        return vehicleRepositoryimpl.deactivateVehicle(vehicleId,status);
    }

    @Override
    public Boolean deactivateDeviceVehicleMapping(Integer vehicleId, Integer status) {
        return vehicleRepositoryimpl.deactivateDeviceVehicleMapping(vehicleId,status);
    }

    @Override
    public Boolean deactivateVehicleActivityMapping(Integer vehicleId, Integer status) {
        return vehicleRepositoryimpl.deactivateVehicleActivityMapping(vehicleId,status);
    }

    @Override
    public Integer getTotalCount(Integer vehicleId) {
        return vehicleRepositoryimpl.getTotalCount(vehicleId);
    }


    @Override
    public List<VehicleActivityMappingEntity> assignVehicleActivity(List<VehicleActivityDto> activity) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
//              formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));


        Integer count = workServiceImpl.deactivateVehicleActivity(activity);

        List<VehicleActivityMappingEntity> vehicleActivity=new ArrayList<>();
        for(VehicleActivityDto vehicleActivity1:activity){
            VehicleActivityMappingEntity vehicle1=new VehicleActivityMappingEntity();
            BeanUtils.copyProperties(vehicleActivity1,vehicle1);
            if(vehicleActivity1.getStartTime()!=null) {
                Date startTime = formatter.parse(vehicleActivity1.getStartTime());
                vehicle1.setStartTime(startTime);
            }
            if(vehicleActivity1.getEndTime()!=null) {
                Date endTime = formatter.parse(vehicleActivity1.getEndTime());
                vehicle1.setEndTime(endTime);
            }

            vehicleActivity.add(vehicle1);
        }
        return vehicleActivityMappingRepository.saveAll(vehicleActivity);
    }

    @Override
    public List<VehicleActivityMappingDto> getVehicleByActivityId(Integer activityId, Integer userId, Date actualActivityStartDate, Date actualActivityCompletionDate) {
        return vehicleRepositoryimpl.getVehicleByActivityId(activityId, userId,actualActivityStartDate,actualActivityCompletionDate);
    }


    @Override
       public VehicleWorkMappingDto  getVehicleWorkMapping(Integer activityId) {
              return vehicleRepository.getVehicleWorkMapping(activityId);
       }

       @Override
       public LocationDto getLocation(Integer vehicleId) throws ParseException {
           VehicleDeviceInfo device =vehicleRepositoryimpl.getVehicleDeviceMapping(vehicleId);
           LocationDto location=null;
           if(device!=null) {
                location = vehicleRepositoryimpl.getLatestLocationByDeviceId(device.getImeiNo1());
           }
           else{
               return null;
           }

              return location;
       }

       @Override
       public List<LocationDto> getLocationArray(int id) throws ParseException {
              List<LocationDto> locationList=new ArrayList<>();


             int totalVehicleCount=vehicleRepositoryimpl.getvehicleCountByWorkId(id);

           LocationDto location=new LocationDto();
           location.setTotalVehicleCount(totalVehicleCount);

           locationList.add(location);
//              for(int i=0;i<2;i++){
//
//                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
//                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
//                     LocationDto location=new LocationDto();
//                     location.setDateTime(dateTime);
//                     location.setLatitude(21.7787878);
//                     location.setLongitude(80.676767);
//                     location.setSpeed(20);
//                     location.setDistanceTravelledToday(200.5);
//                     location.setDistanceTravelledTotal(5000.2);
//                     location.setAvgDistanceTravelled(300.0);
//                     location.setAvgSpeed(30.2);
//                     locationList.add(location);
//              }
              return locationList;
       }

       @Override
       public List<AlertDto> getAlert(Integer vehicleId) throws ParseException {
              List<AlertDto> alertList=new ArrayList<>();
           VehicleDeviceInfo device =vehicleRepositoryimpl.getVehicleDeviceMapping(vehicleId);
           if(device!=null){
               alertList=vehicleRepositoryimpl.getAlertList(device.getImeiNo1());
           }

              return alertList;
       }

       @Override
       public List<AlertDto> getAlertArray(int id) throws ParseException {
              List<AlertDto> alertList=new ArrayList<>();

              List<ActivityDto> activityDtoList = workServiceImpl.getActivityByWorkId(id);
//              List<Integer> activityList=new ArrayList<>();
//              for(ActivityDto activity:activityDtoList){
//                  activityList.add(activity.getId());
//              }
//              List<Integer> vehicleIds= vehicleRepositoryimpl.getVehicleIdsByActivityId(activityList);
//              List<Integer> deviceIds=vehicleRepositoryimpl.getDeviceIdsByVehicleIds(vehicleIds);
//              List<String> imei=vehicleRepositoryimpl.getImeiByDeviceId(deviceIds);

              return alertList;
       }

       public int getvehicleCountByWorkId(int id){
           return vehicleRepositoryimpl.getvehicleCountByWorkId(id);
       }

       @Override
       public List<RoadMasterDto> getRoadArray(int id) throws ParseException {
           return vehicleRepositoryimpl.getRoadArray(id);
       }

       @Override
       public List<VehicleDeviceInfo> getVehicleDeviceMappingAssignedList(Integer vehicleId) throws ParseException {

              return vehicleRepositoryimpl.getVehicleDeviceMappingAssignedList(vehicleId);
       }

       @Override
       public List<VehicleWorkMappingDto> getVehicleWorkMappingList(List<Integer> activityIds) throws ParseException {

              return  vehicleRepositoryimpl.getVehicleWorkMappingList(activityIds);
       }

       @Override
       public VehicleMaster updateVehicle(int id, VehicleMaster vehicle) {
              VehicleMaster existingVehicle= vehicleMasterSaveRepository.findVehicleById(id);
              if (existingVehicle == null) {
                     throw new RecordNotFoundException("Role", "id", id);
              }
              existingVehicle.setVehicleNo(vehicle.getVehicleNo());
              existingVehicle.setVehicleTypeId(vehicle.getVehicleTypeId());
              existingVehicle.setModel(vehicle.getModel());
              existingVehicle.setSpeedLimit(vehicle.getSpeedLimit());
              existingVehicle.setChassisNo(vehicle.getChassisNo());
              existingVehicle.setEngineNo(vehicle.getEngineNo());
              existingVehicle.setActive(vehicle.getActive());
              existingVehicle.setUpdatedBy(vehicle.getUpdatedBy());

              return vehicleMasterSaveRepository.save(existingVehicle);
       }

       @Override
           public Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle) {
            List<Integer>distIds = vehicleRepositoryimpl.getDistIds();
            List<Integer>divisionIds = vehicleRepositoryimpl.getDivisionIds();
            return vehicleRepository.getVehicleList(vehicle,distIds,divisionIds);
       }

       @Override
       public List<VehicleTypeDto> getVehicleTypeList() {
              return vehicleRepository.getVehicleTypeList();
       }

       @Override
       public List<VehicleMasterDto> getUnAssignedVehicleData(Integer userId) {
              List<Integer> userIdList=new ArrayList<>();
              UserInfoDto user=userRepositoryImpl.getUserByUserId(userId);
              if(user.getUserLevelId()!=5){
                     userIdList=helperService.getLowerUserByUserId(userId);
              }
              return vehicleRepository.getUnAssignedVehicleData(userIdList,userId);
       }
    @Override
    public List<UserInfoDto> getUserDropDownForVehicleOwnerMapping(Integer userId) {
        return vehicleRepositoryimpl.getUserDropDownForVehicleOwnerMapping(userId);
    }

       @Override
       public Page<VehicleMasterDto> getVehicleList1(VehicleMasterDto vehicle) {
              return null;
       }

       @Override
       public List<VehicleMasterDto> getVehicleById(Integer id, Integer userId) {
              return vehicleRepository.getVehicleById(id, userId);
       }


}
