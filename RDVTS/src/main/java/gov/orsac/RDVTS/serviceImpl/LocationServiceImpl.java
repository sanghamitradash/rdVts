package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import gov.orsac.RDVTS.repositoryImpl.LocationRepositoryImpl;
import gov.orsac.RDVTS.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepositoryImpl locationRepository;
    @Override
    public List<VtuLocationDto> getLatestRecordByImeiNumber(List<Long> imei2,List<Long> imei1){

       List<VtuLocationDto> vtuLocationDto= locationRepository.getLatestRecordByImeiNumber(imei2,imei1);

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


}
