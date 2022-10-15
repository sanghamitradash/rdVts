package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.DeviceAreaMappingDto;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.DeviceInfo;
import gov.orsac.RDVTS.dto.DeviceListDto;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeviceService {
    DeviceEntity addDevice(DeviceDto deviceDto);


    DeviceMappingEntity saveDeviceMapping(DeviceMappingEntity deviceMapping, Integer id);

    List<DeviceDto> getDeviceByIds(List<Integer> deviceId, Integer userId);
    List<DeviceDto> getDeviceById(Integer deviceId, Integer userId);

    List<DeviceAreaMappingDto> getDeviceAreaByDeviceId(Integer deviceId, Integer userId);

    DeviceEntity updateDeviceById(Integer id, DeviceDto deviceDto);

    Boolean deactivateDeviceArea(Integer id);

    Page<DeviceInfo> getDeviceList(DeviceListDto deviceDto);


    List<DeviceDto> getUnassignedDeviceData(Integer userId);
}
