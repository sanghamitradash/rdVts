package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.*;
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
       private VehicleDeviceMappingRepository vehicleDeviceMappingRepository;
     /*  @Autowired
       private VehicleServiceImpl vehicleServiceImpl;*/
       @Autowired
       private VehicleWorkMappingRepository vehicleWorkMappingRepository;
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
              return vehicleDeviceMappingRepository.save(vehicleDeviceMapping);
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
                     List<VehicleMasterDto> vehicleMasterDtoList=new ArrayList<>();

                     for(int i=0;i<2;i++){
//                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
//                            Date dateTime = formatter.parse("2022-10-10 12:10:00");
                            VehicleMasterDto vehicle =new VehicleMasterDto();
                            vehicle.setVehicleNo("OD33o0209");
                            vehicle.setVehicleTypeId(12);
                            vehicle.setVehicleTypeName("JCB");
                            vehicle.setModel("Glamour");
                            vehicle.setSpeedLimit(20.2);
                            vehicle.setChassisNo("abdh3543HHJJ");
                            vehicle.setEngineNo("ABCD123JHK");


                            vehicleMasterDtoList.add(vehicle);
                     }
                     return vehicleMasterDtoList;
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
       public List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId, Date vehicleWorkStartDate,Date vehicleWorkEndDate) {
              return vehicleRepositoryimpl.getdeviceListByVehicleId(vehicleId,vehicleWorkStartDate,vehicleWorkEndDate);
       }

       @Override
       public List<VehicleWorkMappingDto>  getVehicleWorkMapping(Integer vehicleId) {
              return vehicleRepository.getVehicleWorkMapping(vehicleId);
       }

       @Override
       public List<LocationDto> getLocation(Integer vehicleId) throws ParseException {
              List<LocationDto> locationList=new ArrayList<>();

              for(int i=0;i<2;i++){
                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
                     LocationDto location=new LocationDto();
                     location.setDateTime(dateTime);
                     location.setLatitude(21.7787878);
                     location.setLongitude(80.676767);
                     location.setSpeed(20);
                     location.setDistanceTravelledToday(200.5);
                     location.setDistanceTravelledTotal(5000.2);
                     location.setAvgDistanceTravelled(300.0);
                     location.setAvgSpeed(30.2);
                     locationList.add(location);
              }
              return locationList;
       }

       @Override
       public List<LocationDto> getLocationArray(int id) throws ParseException {
              List<LocationDto> locationList=new ArrayList<>();

              for(int i=0;i<2;i++){
                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
                     LocationDto location=new LocationDto();
                     location.setDateTime(dateTime);
                     location.setLatitude(21.7787878);
                     location.setLongitude(80.676767);
                     location.setSpeed(20);
                     location.setDistanceTravelledToday(200.5);
                     location.setDistanceTravelledTotal(5000.2);
                     location.setAvgDistanceTravelled(300.0);
                     location.setAvgSpeed(30.2);
                     locationList.add(location);
              }
              return locationList;
       }

       @Override
       public List<AlertDto> getAlert(Integer vehicleId) throws ParseException {
              List<AlertDto> alertList=new ArrayList<>();

              for(int i=0;i<2;i++){
                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
                     AlertDto alert=new AlertDto();
                     alert.setId(1);
                     alert.setVmmId(1);
                     alert.setAlertTypeId(1);
                     alert.setLatitude(20.78378783);
                     alert.setLongitude(81.78278278728);
                     alert.setAltitude(21.877878);
                     alert.setAccuracy(21.76737);
                     alert.setSpeed(20.2);
                     alert.setGpsDtm(dateTime);
                     alert.setActive(true);
                     alert.setResolve(true);
                     alert.setResolvedBy(1);


                     alertList.add(alert);
              }
              return alertList;
       }

       @Override
       public List<AlertDto> getAlertArray(int id) throws ParseException {
              List<AlertDto> alertList=new ArrayList<>();

              for(int i=0;i<2;i++){
                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
                     AlertDto alert=new AlertDto();
                     alert.setVmmId(1);
                     alert.setAlertTypeId(1);
                     alert.setLatitude(20.78378783);
                     alert.setLongitude(81.78278278728);
                     alert.setAltitude(21.877878);
                     alert.setAccuracy(21.76737);
                     alert.setSpeed(20.2);
                     alert.setGpsDtm(dateTime);
                     alert.setActive(true);
                     alert.setResolve(true);
                     alert.setResolvedBy(1);


                     alertList.add(alert);
              }
              return alertList;
       }

       @Override
       public List<RoadMasterDto> getRoadArray(int id) throws ParseException {
              List<RoadMasterDto> roadList=new ArrayList<>();

              for(int i=0;i<2;i++){
//                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
//                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
                     RoadMasterDto road = new RoadMasterDto();
                     road.setId(1);
                     road.setPackageId(12);
                     road.setPackageName("package2");
                     road.setRoadName("road2");
                     road.setRoadLength(3.788);
                     road.setRoadLocation(2.555);
                     road.setRoadAllignment("right");
                     road.setRoadWidth(5.111);
                     road.setGroadId(1);
                     road.setGeoMasterId(2);

                     roadList.add(road);
              }
              return roadList;
       }

       @Override
       public List<VehicleDeviceInfo> getVehicleDeviceMappingAssignedList(Integer vehicleId) throws ParseException {
              List<VehicleDeviceInfo> vehicleList=new ArrayList<>();

              for(int i=0;i<2;i++){
                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
                     VehicleDeviceInfo vehicle=new VehicleDeviceInfo();
                     vehicle.setId(1);
                     vehicle.setVehicleId(1);
                     vehicle.setDeviceId(1);
                     vehicle.setInstallationDate(dateTime);
                     vehicle.setImeiNo1(12345678910L);
                     vehicle.setImeiNo2(12345678910L);
                     vehicle.setSimIccId1("6726832t873232t");
                     vehicle.setSimIccId2("896298270923g");
                     vehicle.setMobileNumber1(12345678910L);
                     vehicle.setMobileNumber2(12345678910L);
                     vehicle.setModelName("OnePlus9");
                     vehicle.setDeviceNo(123);

                     vehicleList.add(vehicle);
              }
              return vehicleList;
       }

       @Override
       public List<VehicleWorkMappingDto> getVehicleWorkMappingList(Integer vehicleId) throws ParseException {
              List<VehicleWorkMappingDto> workList=new ArrayList<>();

              for(int i=0;i<2;i++){
                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
                     VehicleWorkMappingDto work=new VehicleWorkMappingDto();
                     work.setId(1);
                     work.setWorkId(1);
                     work.setVehicleId(1);
                     work.setWorkName("Test");
                     work.setStartTime("2022-10-11 11:12:00");
                     work.setEndTime("2022-10-25 11:12:00");
                     work.setStartDate(dateTime);
                     work.setEndDate(dateTime);
                     work.setActive(true);



                     workList.add(work);
              }
              return workList;
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
              return vehicleRepository.getVehicleList(vehicle);
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
       public Page<VehicleMasterDto> getVehicleList1(VehicleMasterDto vehicle) {
              return null;
       }

       @Override
       public List<VehicleMasterDto> getVehicleById(Integer id, Integer userId) {
              return vehicleRepository.getVehicleById(id, userId);
       }


}
