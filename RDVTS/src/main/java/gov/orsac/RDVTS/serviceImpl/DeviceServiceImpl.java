package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.DeviceAreaMappingRepository;
import gov.orsac.RDVTS.repository.DeviceMasterRepository;
import gov.orsac.RDVTS.repository.DeviceRepository;
import gov.orsac.RDVTS.repositoryImpl.DeviceRepositoryImpl;
import gov.orsac.RDVTS.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {


    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceRepositoryImpl deviceRepositoryImpl;

    @Autowired
    private DeviceMasterRepository deviceMasterRepository;

    @Autowired
    private DeviceAreaMappingRepository deviceAreaMappingRepository;

    @Override
    public DeviceEntity addDevice(DeviceDto deviceDto) {
        DeviceEntity deviceEntity1 = new DeviceEntity();
        BeanUtils.copyProperties(deviceDto, deviceEntity1);
        deviceEntity1.setIsActive(true);
        deviceEntity1 = deviceRepository.save(deviceEntity1);
        return deviceEntity1;
    }

  /*  @Override
    public DeviceMappingEntity saveDeviceMapping(DeviceMappingEntity deviceMapping, Integer id) {
      *//*  for (DeviceMappingEntity deviceArea1 : deviceMapping) {
            deviceArea1.setDeviceId(id);
            deviceArea1.setIsActive(true);
        }*//*
        return deviceAreaMappingRepository.save(deviceMapping);
    }*/

    @Override
    public List<DeviceDto> getDeviceByIds(List<Integer> deviceId, Integer userId) {
        return deviceRepositoryImpl.getDeviceByIds(deviceId, userId);
    }

    @Override
    public List<DeviceDto> getDeviceById(Integer deviceId, Integer userId) {
        DeviceDto device = new DeviceDto();
        device = deviceRepositoryImpl.getDevice(deviceId);
        if (device.getIsActive() == true) {
            return deviceRepositoryImpl.getDeviceById(deviceId, userId);
        } else {
            return deviceRepositoryImpl.getInActiveDeviceById(deviceId, userId);
        }

    }

       public List<DeviceDto> getAllDeviceDD(Integer deviceId, Integer userId) {
        return deviceRepositoryImpl.getAllDeviceDD(deviceId, userId);
    }

    @Override
    public List<DeviceAreaMappingDto> getAllDeviceAreaByDeviceId(Integer deviceId, Integer userId) {
        return deviceRepositoryImpl.getAllDeviceAreaByDeviceId(deviceId, userId);
    }

    @Override
    public List<VehicleDeviceMappingDto> getVehicleDeviceMappingDDByDeviceId(Integer deviceId, Integer userId) {
        return deviceRepositoryImpl.getVehicleDeviceMappingDDByDeviceId(deviceId,userId);
    }



    @Override
    public List<DeviceAreaMappingDto> getDeviceAreaByDeviceId(Integer deviceId, Integer userId) {
        List<DeviceAreaMappingDto> deviceMapping=new ArrayList<>();
        List<DeviceAreaMappingDto> device = new ArrayList<>();
                device =deviceRepositoryImpl.getDeviceArea(deviceId);
        for (int i = 0; i < device.size(); i++) {
           // Boolean checkTrueOrFalse = deviceRepositoryImpl.getDeviceAreaTrue(deviceId);
            if (device.get(i).getIsActive() == true) {
                deviceMapping= deviceRepositoryImpl.getDeviceAreaByDeviceId(deviceId, userId);
            } else {
                deviceMapping= deviceRepositoryImpl.getDeviceAreaByDeviceIdInActive(deviceId, userId);
            }
        }
        return deviceMapping;
    }



    @Override
    public List<DeviceAreaMappingDto> getDeviceAreaByDeviceIdInActive(Integer deviceId, Integer userId) {
        return deviceRepositoryImpl.getDeviceAreaByDeviceIdInActive(deviceId,userId);
    }

    @Override
    public DeviceEntity updateDeviceById(Integer id, DeviceDto deviceDto) {
        DeviceEntity existingDevice = deviceRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("id", "id", id));
        existingDevice.setImeiNo1(deviceDto.getImeiNo1());
        existingDevice.setImeiNo2(deviceDto.getImeiNo2());
        existingDevice.setMobileNumber1(deviceDto.getMobileNumber1());
        existingDevice.setMobileNumber2(deviceDto.getMobileNumber2());
        existingDevice.setModelName(deviceDto.getModelName());
        existingDevice.setSimIccId1(deviceDto.getSimIccId1());
        existingDevice.setSimIccId2(deviceDto.getSimIccId2());
        existingDevice.setVtuVendorId(deviceDto.getVtuVendorId());
        existingDevice.setUserLevelId(deviceDto.getUserLevelId());
        existingDevice.setDeviceNo(deviceDto.getDeviceNo());
        DeviceEntity save = deviceRepository.save(existingDevice);
        return save;

    }

    @Override
    public Boolean deactivateDeviceArea(Integer id) {
        return deviceRepositoryImpl.deactivateDeviceArea(id);
    }



    @Override
    public Page<DeviceInfo> getDeviceList(DeviceListDto deviceDto) {
        return deviceMasterRepository.getDeviceList(deviceDto);
    }

    @Override
    public List <DeviceDto> getUnassignedDeviceData(Integer userId) {
        return deviceMasterRepository.getUnassignedDeviceData(userId);
    }

    @Override
    public List<VehicleDeviceMappingDto> getVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId) {
        return deviceMasterRepository.getVehicleDeviceMappingByDeviceId(deviceId,userId);
    }

    @Override
    public List<userLevelDto> getDeviceUserLevel() {
        return deviceMasterRepository.getDeviceUserLevel();
    }

    public DeviceMappingEntity saveDeviceAreaMapping(DeviceMappingEntity deviceMapping, Integer deviceId,Integer userLevelId){
        DeviceMappingEntity device = new DeviceMappingEntity();
        BeanUtils.copyProperties(deviceMapping, device);
        if(userLevelId == 1)
         {
               device.setStateId(deviceMapping.getStateId());
               device.setDeviceId(deviceId);
          }
        if(userLevelId ==2){

                 DeviceAreaMappingDto deviceAreaMapping = deviceMasterRepository.getStateByDistId(deviceMapping.getDistId());
                 device.setStateId(deviceAreaMapping.getStateId());
                 device.setDeviceId(deviceId);
            }

        if(userLevelId == 3){

            DeviceAreaMappingDto deviceAreaMapping = deviceMasterRepository.getStateDistByBlockId(deviceMapping.getBlockId());
            device.setStateId(deviceAreaMapping.getStateId());
            device.setDistId(deviceAreaMapping.getDistId());
            device.setDeviceId(deviceId);
        }
        if(userLevelId == 4){

            DeviceAreaMappingDto deviceAreaMapping = deviceMasterRepository.getStateDistByDivisionId(deviceMapping.getDivisionId());
            device.setStateId(deviceAreaMapping.getStateId());
            device.setDistId(deviceAreaMapping.getDistId());
            device.setDeviceId(deviceId);
        }

        return deviceAreaMappingRepository.save(device);
    }


    @Override
    public List<DeviceDto> getImeiListByDeviceId(Integer deviceId){
        return deviceRepositoryImpl.getImeiListByDeviceId(deviceId);
    }
    @Override
    public Integer getvehicleIdbydevice(Integer deviceId){
        return 1;
    }



    @Override
    public List<VTUVendorMasterDto> getVtuVendorDropDown() {
        return deviceRepositoryImpl.getVtuVendorDropDown();
    }

    @Override
    public Boolean deactivateDeviceVehicle(Integer id) {
         return deviceRepositoryImpl.deactivateDeviceVehicle(id);
    }

    @Override
    public Boolean deactivateDevice(Integer deviceId,Integer status) {
        return deviceRepositoryImpl.deactivateDevice(deviceId,status);
    }

    @Override
    public List<VehicleDeviceMappingDto> getAllVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId) {
        return deviceRepositoryImpl.getAllVehicleDeviceMappingByDeviceId(deviceId,userId);
    }

    @Override
    public Boolean deactivateDeviceAreaMapping(Integer deviceId, Integer status) {
        return deviceRepositoryImpl.deactivateDeviceAreaMapping(deviceId,status);
    }

    @Override
    public Boolean deactivateDeviceVehicleMapping(Integer deviceId, Integer status) {
        return deviceRepositoryImpl.deactivateDeviceVehicleMapping(deviceId,status);
    }


}

