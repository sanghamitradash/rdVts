package gov.orsac.RDVTS.service;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.LocationDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import gov.orsac.RDVTS.entities.AlertEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LocationService {
    List<VtuLocationDto> getLatestRecordByImeiNumber(List<Long> imei1,List<Long> imei2);

     List<VtuLocationDto> getLocationRecordByDateTime(List<Long> imei1,List<Long> imei2, Date startTime,Date endTime);
    List<VtuLocationDto> getLocationRecordByRoadData(List<Long> imei1,List<Long> imei2,List<VehicleWorkMappingDto> vehicleByWork);

    List<VtuLocationDto> getLocationrecordList(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    List<VtuLocationDto> getLastLocationrecordList(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    Integer getActiveVehicle(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    Double getDistance(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    Double getTodayDistance(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);
    List<VtuLocationDto> getAvgSpeedToday(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);

    Double getspeed(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate);

    VtuLocationDto getLastLocationByImei(Long imei1);






}
