package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.DeviceAreaMappingDto;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.DeviceListDto;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeviceService {
    DeviceEntity addDevice(DeviceDto deviceDto);


    List<DeviceMappingEntity> saveDeviceMapping(List<DeviceMappingEntity> deviceMapping, Integer id);

    DeviceDto getDeviceById(Integer deviceId);

    List<DeviceAreaMappingDto> getDeviceAreaByDeviceId(Integer deviceId);

    DeviceEntity updateDeviceById(Integer id, DeviceDto deviceDto);

    Boolean deactivateDeviceArea(Integer id);

    Page<DeviceDto> getDeviceList(DeviceListDto deviceDto);


}
