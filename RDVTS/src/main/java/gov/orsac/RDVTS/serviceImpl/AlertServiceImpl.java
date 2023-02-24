package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.AlertEntity;
import gov.orsac.RDVTS.entities.AlertTypeEntity;
import gov.orsac.RDVTS.entities.WorkCronEntity;
import gov.orsac.RDVTS.repository.AlertRepository;
import gov.orsac.RDVTS.repository.AlertTypeRepository;
import gov.orsac.RDVTS.repository.WorkCronRepository;
import gov.orsac.RDVTS.repositoryImpl.AlertRepositoryImpl;
import gov.orsac.RDVTS.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    public WorkCronRepository workCronRepository;

    @Autowired
    private AlertRepositoryImpl alertRepositoryImpl;

    @Autowired
    public AlertRepository alertRepository;

    @Autowired
    public AlertTypeRepository alertTypeRepository;


    @Override
    public List<AlertCountDto> getTotalAlertToday(/*AlertFilterDto filterDto,*/ Integer id, Integer userId) {
        return alertRepositoryImpl.getTotalAlertToday(/*filterDto,*/ id, userId);
    }

    @Override
    public List<AlertCountDto> getTotalAlertWork(/*AlertFilterDto filterDto,*/ Integer id, Integer userId) {
        return alertRepositoryImpl.getTotalAlertWork(/*filterDto,*/ id, userId);
    }

    @Override
    public Page<AlertCountDto> getAlertToday(AlertFilterDto filterDto) {
        return alertRepositoryImpl.getAlertToday(filterDto);
    }

    @Override
    public Page<AlertCountDto> getWorkAlertTotal(AlertFilterDto filterDto) {
        return alertRepositoryImpl.getWorkAlertTotal(filterDto);
    }

    @Override
    public Page<AlertCountDto> getVehicleAlert(AlertFilterDto filterDto) {
        return alertRepositoryImpl.getVehicleAlert(filterDto);
    }

    @Override
    public Page<AlertCountDto> getRoadAlert(AlertFilterDto filterDto) {
        return alertRepositoryImpl.getRoadAlert(filterDto);
    }
//
//    @Override
//    public Page<AlertCountDto> getTotalAlertWork(Integer id) {
//        List<AlertCountDto> finalCount = new ArrayList<>();
//        return alertRepositoryImpl.getTotalAlertWork(id);
//        AlertCountDto se=new AlertCountDto();
//        for (int i = 0; i < alertCount.size(); i++) {
//            se.setAlertType(alertCount.get(i).getAlertType());
//            se.setAlertTypeId(alertCount.get(i).getAlertTypeId());
//            se.setCount(alertCount.get(i).getCount());
//            se.setWorkId(alertCount.get(i).getWorkId());
//            finalCount.add(se);
//        }
//        return finalCount;
//    }

    public Boolean checkAlertExists(Long imei, Integer noDataAlertId){


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

    @Override
    public List<AlertDto> getAllDeviceByVehicle() {
        return alertRepositoryImpl.getAllDeviceByVehicle();

    }

    @Override
    public List<VtuLocationDto> getAlertLocationOverSpeed(Long imei, double speedLimit) {
        return alertRepositoryImpl.getAlertLocationOverSpeed(imei,speedLimit);
    }

    @Override
    public AlertTypeEntity getAlertTypeDetails(int i) {
        return  alertRepositoryImpl.getAlertTypeDetails(i);
    }

    @Override
    public List<VtuLocationDto> GeoFenceIntersectedRecords(String geom, List<VtuLocationDto> vtuLocationDto) {
         return alertRepositoryImpl.GeoFenceIntersectedRecords(geom,vtuLocationDto);
    }

    @Override
    public List<AlertCountDto> getVehicleAlertForReport(AlertFilterDto filterDto) {
        return alertRepositoryImpl.getVehicleAlertForReport(filterDto);
    }

    @Override
    public List<WorkCronEntity> save(List<WorkCronEntity> work) {
        List<WorkCronEntity> workList=new ArrayList<>();
        for(int i=0;i<work.size();i++){
            WorkCronEntity w=alertRepositoryImpl.getWorkCronByWorkId(work.get(i).getId());
            if(w!=null) {
                w.setTotalDistance(work.get(i).getTotalDistance());
                w.setTodayDistance(work.get(i).getTodayDistance());
                w.setAvgSpeedToday(work.get(i).getAvgSpeedToday());
                w.setTotalSpeedWork(work.get(i).getTotalSpeedWork());
                w.setTotalActiveVehicle(work.get(i).getTotalActiveVehicle());
                workList.add(w);
                workCronRepository.save(w);
            }
            else{
                WorkCronEntity w1=new WorkCronEntity();
                w.setWorkId(work.get(i).getId());
                w.setTotalDistance(work.get(i).getTotalDistance());
                w.setTodayDistance(work.get(i).getTodayDistance());
                w.setAvgSpeedToday(work.get(i).getAvgSpeedToday());
                w.setTotalSpeedWork(work.get(i).getTotalSpeedWork());
                w.setTotalActiveVehicle(work.get(i).getTotalActiveVehicle());
                workList.add(w1);
                workCronRepository.save(w1);
            }
        }
        return workList;
    }


}
