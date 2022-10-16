package gov.orsac.RDVTS.service;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;

import java.util.Date;
import java.util.List;
public interface LocationService {
    List<VtuLocationDto> getLatestRecordByImeiNumber(List<Long> imei1,List<Long> imei2);

     List<VtuLocationDto> getLocationRecordByDateTime(List<Long> imei1,List<Long> imei2, Date startTime,Date endTime);
    List<VtuLocationDto> getLocationRecordByRoadData(List<Long> imei1,List<Long> imei2,List<VehicleWorkMappingDto> vehicleByWork);

    List<VtuLocationDto> getLocationrecordList(Long imei1,Long imei2);
}
