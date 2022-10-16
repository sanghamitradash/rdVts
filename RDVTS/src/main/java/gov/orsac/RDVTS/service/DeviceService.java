package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeviceService {
    DeviceEntity addDevice(DeviceDto deviceDto);


    DeviceMappingEntity saveDeviceAreaMapping(DeviceMappingEntity deviceMapping, Integer deviceId,Integer userLevel);

    List<DeviceDto> getDeviceByIds(List<Integer> deviceId, Integer userId);
    List<DeviceDto> getDeviceById(Integer deviceId, Integer userId);

    List<DeviceAreaMappingDto> getDeviceAreaByDeviceId(Integer deviceId, Integer userId);

    DeviceEntity updateDeviceById(Integer id, DeviceDto deviceDto);

    Boolean deactivateDeviceArea(Integer id);

    Page<DeviceInfo> getDeviceList(DeviceListDto deviceDto);


    List<DeviceDto> getUnassignedDeviceData(Integer userId);

    List<VehicleDeviceMappingDto> getVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId);

    List<userLevelDto> getDeviceUserLevel();
}
