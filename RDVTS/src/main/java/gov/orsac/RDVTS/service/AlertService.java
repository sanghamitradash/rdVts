package gov.orsac.RDVTS.service;

import java.util.List;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.AlertEntity;
import org.springframework.data.domain.Page;
import gov.orsac.RDVTS.entities.AlertTypeEntity;


public interface AlertService  {
    List<AlertDto> checkAlertExists(Long imei, Integer noDataAlertId);
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

    Page<AlertCountDto> getTotalAlertToday(AlertFilterDto filterDto, Integer id, Integer userId);

    Page<AlertCountDto> getTotalAlertWork(AlertFilterDto filterDto, Integer id, Integer userId);
}
