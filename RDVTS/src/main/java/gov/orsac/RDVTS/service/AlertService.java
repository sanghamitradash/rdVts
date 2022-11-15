package gov.orsac.RDVTS.service;

import java.util.List;

import gov.orsac.RDVTS.dto.AlertDto;
import gov.orsac.RDVTS.dto.BufferDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import gov.orsac.RDVTS.entities.AlertEntity;

import java.util.List;


public interface AlertService  {
    Integer getTotalAlertToday();
    Integer getTotalAlertWork();
    AlertDto checkAlertExists(Long imei, Integer noDataAlertId);
    AlertEntity saveAlert(AlertEntity alertEntity);
    Boolean updateResolve(Long imei1, Integer noDataAlertId);

    List<Long> getImeiForNoMovement();

    List<VtuLocationDto> getLocationRecordByFrequency(Long imei1, Integer recordLimit);

    List<BufferDto> getBuffer(Long item);

    Boolean checkIntersected(String longitude, String latitude, String vtuItemLongitude, String vtuItemLatitude);

    Boolean bufferQuery(String BufferPointLongitude, String BufferPointLatitude, String longitude, String latitude);

    Boolean checkGeoFenceIntersected(String geom, String longitude, String latitude);

    List<AlertDto> getAllDeviceByVehicle();




    List<VtuLocationDto> getAlertLocationOverSpeed(Long imei, double speedLimit, Integer recordLimit);
}
