package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.AlertDto;
import gov.orsac.RDVTS.dto.BufferDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import gov.orsac.RDVTS.entities.AlertEntity;
import gov.orsac.RDVTS.repository.AlertRepository;
import gov.orsac.RDVTS.repositoryImpl.AlertRepositoryImpl;
import gov.orsac.RDVTS.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    public AlertRepositoryImpl alertRepositoryImpl;

    @Autowired
    public AlertRepository alertRepository;

    public AlertDto checkAlertExists(Long imei, Integer noDataAlertId){


        return alertRepositoryImpl.checkAlertExists(imei, noDataAlertId);
    }

    public AlertEntity saveAlert(AlertEntity alertEntity){
        return alertRepository.save(alertEntity);
    }
    public Boolean updateResolve(Long imei1, Integer noDataAlertId){
        //AlertEntity alertEntity=new AlertEntity();
       return alertRepositoryImpl.updateResolve(imei1,noDataAlertId);
    }

    public List<Long>  getImeiForNoMovement(){
        return alertRepositoryImpl.getImeiForNoMovement();
    }

    public List<VtuLocationDto> getLocationRecordByFrequency(Long imei1, Integer recordLimit){
        return alertRepositoryImpl.getLocationRecordByFrequency(imei1,recordLimit);
    }
    public List<BufferDto> getBuffer(Long item){
        return alertRepositoryImpl.getBuffer(item);
    }

    @Override
    public Boolean checkIntersected(String longitude, String latitude, String vtuItemLongitude, String vtuItemLatitude) {
        return alertRepositoryImpl.checkIntersected(longitude,latitude,vtuItemLongitude,vtuItemLatitude);
    }

    @Override
    public Boolean bufferQuery(String BufferPointLongitude, String BufferPointLatitude, String longitude, String latitude) {
        return alertRepositoryImpl.bufferQuery(BufferPointLongitude,BufferPointLatitude,longitude,latitude);
    }

    @Override
    public Boolean checkGeoFenceIntersected(String geom, String longitude, String latitude) {
        return alertRepositoryImpl.checkGeoFenceIntersected(geom,longitude,latitude);
    }


}
