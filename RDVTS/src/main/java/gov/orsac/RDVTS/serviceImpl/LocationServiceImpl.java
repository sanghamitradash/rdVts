package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import gov.orsac.RDVTS.repositoryImpl.LocationRepositoryImpl;
import gov.orsac.RDVTS.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepositoryImpl locationRepository;
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
    public  Double getDistance(Map<String,Object> coordinates){
        Double d=locationRepository.getDistance(coordinates);
        return d;
    }





}
