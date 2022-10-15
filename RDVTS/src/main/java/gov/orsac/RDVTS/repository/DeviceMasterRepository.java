package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.DeviceInfo;
import gov.orsac.RDVTS.dto.DeviceListDto;
import gov.orsac.RDVTS.dto.VehicleDeviceMappingDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeviceMasterRepository {
    Page<DeviceInfo> getDeviceList(DeviceListDto deviceDto);

    List<DeviceDto> getUnassignedDeviceData(Integer userId);

  List<VehicleDeviceMappingDto> getVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId);
}
