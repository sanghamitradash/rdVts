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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VehicleServiceImpl implements VehicleService {
       @Autowired
       private VehicleMasterSaveRepository vehicleMasterSaveRepository;
       @Autowired
       private VehicleRepository vehicleRepository;

       @Autowired
       private VehicleDeviceMappingRepository vehicleDeviceMappingRepository;
       @Autowired
       private VehicleWorkMappingRepository vehicleWorkMappingRepository;
       @Autowired
       private VehicleOwnerMappingRepository vehicleOwnerMappingRepository;
       @Autowired
       private UserRepositoryImpl userRepositoryImpl;
       @Autowired
       private HelperService helperService;

       @Autowired
       private VehicleRepositoryImpl vehicleRepositoryimpl;
       @Override
       public VehicleMaster saveVehicle(VehicleMaster vehicle) {
        return vehicleMasterSaveRepository.save(vehicle);
        }
       @Override
       public VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping) {

              return vehicleDeviceMappingRepository.save(vehicleDeviceMapping);
       }

       @Override
       public VehicleOwnerMappingEntity assignVehicleOwner(VehicleOwnerMappingDto vehicleOwnerMapping) {
              VehicleOwnerMappingEntity vehicle=new VehicleOwnerMappingEntity();
              BeanUtils.copyProperties(vehicleOwnerMapping,vehicle);
              return vehicleOwnerMappingRepository.save(vehicle);
       }

       @Override
       public List<VehicleWorkMappingEntity> assignVehicleWork(List<VehicleWorkMappingDto> vehicleWorkMapping) throws ParseException {
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
//              formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));




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
       public VehicleDeviceInfo getVehicleDeviceMapping(Integer vehicleId) {
              return vehicleRepository.getVehicleDeviceMapping(vehicleId);
       }

       @Override
       public List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId) {
              return vehicleRepository.getVehicleDeviceMappingList(vehicleId);
       }

       @Override
       public List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId) {
              return vehicleRepositoryimpl.getdeviceListByVehicleId(vehicleId);
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
       public List<AlertDto> getAlert(Integer vehicleId) throws ParseException {
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
       public List<VehicleDeviceInfo> getVehicleDeviceMappingAssignedList(Integer vehicleId) throws ParseException {
              List<VehicleDeviceInfo> vehicleList=new ArrayList<>();

              for(int i=0;i<2;i++){
                     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
                     Date dateTime = formatter.parse("2022-10-10 12:10:00");
                     VehicleDeviceInfo vehicle=new VehicleDeviceInfo();
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
              existingVehicle.setActive(vehicle.isActive());
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
