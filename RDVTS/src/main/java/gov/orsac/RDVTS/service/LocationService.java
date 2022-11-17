package gov.orsac.RDVTS.service;
import gov.orsac.RDVTS.dto.*;

import java.util.Date;
import java.util.List;

public interface LocationService {
    List<VtuLocationDto> getLatestRecordByImeiNumber(List<Long> imei1,List<Long> imei2);

     List<VtuLocationDto> getLocationRecordByDateTime(List<Long> imei1,List<Long> imei2, Date startTime,Date endTime);
    List<VtuLocationDto> getLocationRecordByRoadData(List<Long> imei1,List<Long> imei2,List<VehicleWorkMappingDto> vehicleByWork);

    List<VtuLocationDto> getLocationrecordList(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    List<VtuLocationDto> getLastLocationrecordList(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    List<VtuLocationDto> getLastLocationRecordList(List<VehicleDeviceMappingDto> deviceDetails, Date startDate, Date endDate);
    Integer getActiveVehicle(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    Double getDistance(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    Double getTodayDistance(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    List<VtuLocationDto> getAvgSpeedToday(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);

    Double getspeed(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);

    VtuLocationDto getLastLocationByImei(Long imei1);


    List<VtuLocationDto> getLocationrecordList(Long imeiNo1, Long imeiNo2, Date startDate, Date endDate, Date createdOn, Date deactivationDate, Integer recordLimit);

    List<VtuLocationDto> getLastLocationByDeviceId(List<Integer> deviceIdList, Integer checkArea);


}
