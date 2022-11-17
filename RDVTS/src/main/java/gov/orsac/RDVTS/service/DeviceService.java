package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeviceService {
    DeviceEntity addDevice(DeviceDto deviceDto);


    DeviceMappingEntity saveDeviceAreaMapping(DeviceMappingEntity deviceMapping, Integer deviceId,Integer userLevel);

    List<DeviceDto> getDeviceByIds(List<Integer> deviceId, Integer userId);
    List<DeviceDto> getDeviceById(Integer deviceId, Integer userId);

    List<DeviceAreaMappingDto> getDeviceAreaByDeviceId(Integer deviceId, Integer userId);
    List<DeviceAreaMappingDto> getDeviceAreaByDeviceIdInActive(Integer deviceId, Integer userId);

    DeviceEntity updateDeviceById(Integer id, DeviceDto deviceDto);

    Boolean deactivateDeviceArea(Integer id);

    Page<DeviceInfo> getDeviceList(DeviceListDto deviceDto);


    List<DeviceDto> getUnassignedDeviceData(Integer userId);

    List<VehicleDeviceMappingDto> getVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId);

    List<userLevelDto> getDeviceUserLevel();

    //for Imei Get SOubhagya
    List<DeviceDto> getImeiListByDeviceId(Integer deviceId);
    Integer  getvehicleIdbydevice(Integer deviceId);


    List<VTUVendorMasterDto> getVtuVendorDropDown();

    Boolean deactivateDeviceVehicle(Integer id);

    Boolean deactivateDevice(Integer deviceId,Integer status);

    List<VehicleDeviceMappingDto> getAllVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId);

    Boolean deactivateDeviceAreaMapping(Integer deviceId, Integer status);

    Boolean deactivateDeviceVehicleMapping(Integer deviceId, Integer status);

    List<DeviceDto> getAllDeviceDD(Integer deviceId, Integer userId);

    List<DeviceAreaMappingDto> getAllDeviceAreaByDeviceId(Integer deviceId, Integer userId);

    List<VehicleDeviceMappingDto> getVehicleDeviceMappingDDByDeviceId(Integer deviceId, Integer userId);


}
