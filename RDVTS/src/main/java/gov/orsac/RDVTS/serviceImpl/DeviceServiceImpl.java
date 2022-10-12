package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.DeviceAreaMappingDto;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.DeviceListDto;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.DeviceAreaMappingRepository;
import gov.orsac.RDVTS.repository.DeviceRepository;
import gov.orsac.RDVTS.repositoryImpl.DeviceRepositoryImpl;
import gov.orsac.RDVTS.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {


    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceRepositoryImpl deviceRepositoryImpl;

    @Autowired
    private DeviceAreaMappingRepository deviceAreaMappingRepository;

    @Override
    public DeviceEntity addDevice(DeviceDto deviceDto) {
        DeviceEntity deviceEntity1 = new DeviceEntity();
        BeanUtils.copyProperties(deviceDto, deviceEntity1);
        deviceEntity1.setIsActive(true);
        deviceEntity1 = deviceRepository.save(deviceEntity1);
        return deviceEntity1;
    }

    @Override
    public List<DeviceMappingEntity> saveDeviceMapping(List<DeviceMappingEntity> deviceMapping, Integer id) {
        for (DeviceMappingEntity deviceArea1 : deviceMapping) {
            deviceArea1.setDeviceId(id);
            deviceArea1.setIsActive(true);
        }
        return deviceAreaMappingRepository.saveAll(deviceMapping);
    }

    @Override
    public DeviceDto getDeviceById(Integer deviceId) {
        return deviceRepositoryImpl.getDeviceById(deviceId);
    }

    @Override
    public List<DeviceAreaMappingDto> getDeviceAreaByDeviceId(Integer deviceId) {
        return deviceRepositoryImpl.getDeviceAreaByDeviceId(deviceId);
    }

    @Override
    public DeviceEntity updateDeviceById(Integer id, DeviceDto deviceDto) {
        DeviceEntity existingDevice = deviceRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("id", "id", id));
        existingDevice.setImeiNo1(deviceDto.getImeiNo1());
        existingDevice.setImeiNo2(deviceDto.getImeiNo2());
        existingDevice.setMobileNumber1(deviceDto.getMobileNumber1());
        existingDevice.setMobileNumber2(deviceDto.getMobileNumber2());
        existingDevice.setModelName(deviceDto.getModelName());
        existingDevice.setSimIccId1(deviceDto.getSimIccId1());
        existingDevice.setSimIccId2(deviceDto.getSimIccId2());
        existingDevice.setVtuVendorId(deviceDto.getVtuVendorId());
        DeviceEntity save = deviceRepository.save(existingDevice);
        return save;

    }

    @Override
    public Boolean deactivateDeviceArea(Integer id) {
        return deviceRepositoryImpl.deactivateDeviceArea(id);
    }



    @Override
    public Page<DeviceDto> getDeviceList(DeviceListDto deviceDto) {
        return deviceRepositoryImpl.getDeviceList(deviceDto);
    }


}

