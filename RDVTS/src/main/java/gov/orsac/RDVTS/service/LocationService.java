package gov.orsac.RDVTS.service;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import java.util.List;
public interface LocationService {
    VtuLocationDto getLatestRecordByImeiNumber(List<DeviceDto> deviceDtoList);
}
