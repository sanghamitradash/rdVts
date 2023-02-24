package gov.orsac.RDVTS.service;

import java.util.List;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.AlertEntity;
import gov.orsac.RDVTS.entities.AlertTypeEntity;
import org.springframework.data.domain.Page;


public interface AlertService  {
    Boolean checkAlertExists(Long imei, Integer noDataAlertId);
    AlertEntity saveAlert(AlertEntity alertEntity);
    Boolean updateResolve(Long imei1, Integer noDataAlertId);

    List<Long> getImeiForNoMovement();

    List<VtuLocationDto> getLocationRecordByFrequency(Long imei1, Integer recordLimit);

    List<BufferDto> getBuffer(Long item);

    Boolean checkIntersected(String longitude, String latitude, String vtuItemLongitude, String vtuItemLatitude);

    Boolean bufferQuery(String BufferPointLongitude, String BufferPointLatitude, String longitude, String latitude);

    Boolean checkGeoFenceIntersected(String geom, String longitude, String latitude);

    List<AlertDto> getAllDeviceByVehicle();




    List<VtuLocationDto> getAlertLocationOverSpeed(Long imei, double speedLimit);

    List<AlertCountDto> getTotalAlertToday(/*AlertFilterDto filterDto, */Integer id, Integer userId);

    List<AlertCountDto> getTotalAlertWork(/*AlertFilterDto filterDto,*/ Integer id, Integer userId);

    Page<AlertCountDto> getAlertToday(AlertFilterDto filterDto);

    Page<AlertCountDto> getWorkAlertTotal(AlertFilterDto filterDto);

    Page<AlertCountDto> getVehicleAlert(AlertFilterDto filterDto);

    Page<AlertCountDto> getRoadAlert(AlertFilterDto filterDto);

    AlertTypeEntity getAlertTypeDetails(Integer i);
    List<AlertTypeEntity> getAlertTypeDetails();

    List<VtuLocationDto> GeoFenceIntersectedRecords(String geom, List<VtuLocationDto> vtuLocationDto);

    List<AlertCountDto> getVehicleAlertForReport(AlertFilterDto filterDto);
}
