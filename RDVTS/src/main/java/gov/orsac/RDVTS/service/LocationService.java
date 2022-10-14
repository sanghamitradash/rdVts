package gov.orsac.RDVTS.service;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;

import java.util.Date;
import java.util.List;
public interface LocationService {
    VtuLocationDto getLatestRecordByImeiNumber(List<DeviceDto> deviceDtoList);

     List<VtuLocationDto> getLocationRecordByDateTime(List<DeviceDto> deviceDtoList, Date startTime,Date endTime);
}
