package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import gov.orsac.RDVTS.repositoryImpl.LocationRepositoryImpl;
import gov.orsac.RDVTS.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepositoryImpl locationRepository;
    @Override
    public VtuLocationDto getLatestRecordByImeiNumber(List<DeviceDto> deviceDtoList){

       VtuLocationDto vtuLocationDto= locationRepository.getLatestRecordByImeiNumber(deviceDtoList);

        return vtuLocationDto;
    }


}
