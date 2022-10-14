package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.entities.VehicleWorkMappingEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.VehicleDeviceMappingRepository;
import gov.orsac.RDVTS.repository.VehicleMasterSaveRepository;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.repository.VehicleWorkMappingRepository;
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
       @Override
       public VehicleMaster saveVehicle(VehicleMaster vehicle) {
        return vehicleMasterSaveRepository.save(vehicle);
        }
       @Override
       public VehicleDeviceMappingEntity assignVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping) {
              return vehicleDeviceMappingRepository.save(vehicleDeviceMapping);
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
       public VehicleDeviceMappingDto getVehicleDeviceMapping(Integer vehicleId) {
              return vehicleRepository.getVehicleDeviceMapping(vehicleId);
       }

       @Override
       public List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId) {
              return vehicleRepository.getVehicleDeviceMappingList(vehicleId);
       }

       @Override
       public List<VehicleWorkMappingDto>  getVehicleWorkMapping(Integer vehicleId) {
              return vehicleRepository.getVehicleWorkMapping(vehicleId);
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
              return vehicleRepository.getUnAssignedVehicleData(userId);
       }

       @Override
       public Page<VehicleMasterDto> getVehicleList1(VehicleMasterDto vehicle) {
              return null;
       }
}
