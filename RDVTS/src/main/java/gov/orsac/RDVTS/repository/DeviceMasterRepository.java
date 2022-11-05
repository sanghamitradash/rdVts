package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeviceMasterRepository {
    Page<DeviceInfo> getDeviceList(DeviceListDto deviceDto);

    List<DeviceDto> getUnassignedDeviceData(Integer userId);

    List<VehicleDeviceMappingDto> getVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId);

    List<userLevelDto> getDeviceUserLevel();

    DeviceAreaMappingDto getStateByDistId(Integer distId);

    DeviceAreaMappingDto getStateDistByBlockId(Integer blockId);

    DeviceAreaMappingDto getStateDistByDivisionId(Integer divisionId);

    List<DeviceAreaMappingDto> getDeviceAreaByDeviceIdInActive(Integer deviceId, Integer userId);
}
