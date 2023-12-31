package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.repositoryImpl.LocationRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.VehicleRepositoryImpl;
import gov.orsac.RDVTS.service.LocationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepositoryImpl locationRepository;

    @Autowired
    VehicleRepositoryImpl vehicleRepositoryimpl;
    @Override
    public List<VtuLocationDto> getLatestRecordByImeiNumber(List<Long> imei2,List<Long> imei1){
        List<VtuLocationDto> vtuLocationDto = new ArrayList<>();
        int i=0;
        for(Long item :  imei1){
            vtuLocationDto.add(locationRepository.getLatestRecordByImeiNumber(item,imei2.get(i)));
            i++;
        }
        return vtuLocationDto;
    }


    public List<VtuLocationDto> getLocationRecordByDateTime(List<Long> imei1,List<Long> imei2,Date startTime, Date endTime){

        List<VtuLocationDto> vtuLocationDtoList= locationRepository.getLocationRecordByDateTime(imei2,imei1,startTime,endTime);
        return vtuLocationDtoList;
    }

    @Override
    public List<VtuLocationDto> getLocationRecordByRoadData(List<Long> imei1,List<Long> imei2,List<VehicleWorkMappingDto> vehicleByWork){
        return locationRepository.getLocationRecordByRoadData(imei2,imei1,vehicleByWork);
    }

    @Override
    public List<VtuLocationDto> getLocationrecordList(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){

        List<VtuLocationDto> vtuLocationDtoList=locationRepository.getLocationrecordList(imei2,imei1,startDate,endDate,deviceVehicleCreatedOn,deviceVehicleDeactivationDate);
        return vtuLocationDtoList;

    }

    @Override
    public List<VtuLocationDto> getLastLocationrecordList(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){

        List<VtuLocationDto> vtuLocationDtoList=locationRepository.getLastLocationrecordList(imei2,imei1,startDate,endDate,deviceVehicleCreatedOn,deviceVehicleDeactivationDate);
        return vtuLocationDtoList;

    }

    @Override
    public List<VtuLocationDto> getLastLocationRecordList(List<VehicleDeviceMappingDto> deviceDetails, Date startDate, Date endDate){

        List<VtuLocationDto> vtuLocationDtoList=locationRepository.getLastLocationRecordList(deviceDetails,startDate,endDate);
        return vtuLocationDtoList;

    }

    @Override
    public Integer getActiveVehicle(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){

        Integer activeVehicle=locationRepository.getActiveVehicle(imei2,imei1,startDate,endDate,deviceVehicleCreatedOn,deviceVehicleDeactivationDate);
        return activeVehicle;

    }
    @Override
    public Double getDistance(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){
        return locationRepository.getDistance(imei2,imei1,startDate,endDate,deviceVehicleCreatedOn,deviceVehicleDeactivationDate);
    }
    @Override
    public Double getTodayDistance(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){
        return locationRepository.getTodayDistance(imei2,imei1,startDate,endDate,deviceVehicleCreatedOn,deviceVehicleDeactivationDate);
    }
    @Override
    public List<VtuLocationDto> getAvgSpeedToday(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){
        return locationRepository.getAvgSpeedToday(imei2,imei1,startDate,endDate,deviceVehicleCreatedOn,deviceVehicleDeactivationDate);
    }


    @Override
    public Double getspeed(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){
        return locationRepository.getspeed(imei2,imei1,startDate,endDate,deviceVehicleCreatedOn,deviceVehicleDeactivationDate);
    }

    @Override
    public VtuLocationDto  getLastLocationByImei(Long imei1, Date startTime, Date endTime){
        return locationRepository.getLastLocationByImei(imei1, startTime, endTime);

    }

    @Override
    public List<VtuLocationDto> getLocationrecordList(Long imeiNo1, Long imeiNo2, Date startDate, Date endDate, Date createdOn, Date deactivationDate, Integer recordLimit) {
        List<VtuLocationDto> vtuLocationDtoList=locationRepository.getLocationrecordList(imeiNo1,imeiNo2,startDate,endDate,createdOn,deactivationDate,recordLimit);
        return vtuLocationDtoList;
    }
    @Override
    public List<VtuLocationDto> getLastLocationByDeviceId(List<Integer> deviceIdList, Integer checkArea){

        return locationRepository.getLastLocationByDeviceId(deviceIdList,checkArea);
    }

    @Override
    public List<VtuLocationDto> getLocationRecordListWithGeofence(Long imeiNo1, Long imeiNo2, Date createdOn, Date deactivationDate, Integer id) {
        List<VtuLocationDto> vtuLocationDtoList=locationRepository.getLocationRecordListWithGeofence(imeiNo1,imeiNo2,createdOn,deactivationDate,id);
        return vtuLocationDtoList;
    }

    @Override
    public AlertDegreeDistanceDto getRotationDetails(String longitude, String latitude, String longitude1, String latitude1) {
        return  locationRepository.getRotationDetails(longitude,latitude,longitude1,latitude1);
    }

    @Override
    public Boolean getLocationExistOrNot(List<Integer> vehicleId, Date startDate, Date endDate) {

        return locationRepository.getLocationExistOrNot(vehicleId,startDate,endDate);
    }


}
