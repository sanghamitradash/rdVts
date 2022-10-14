package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.entities.RoadEntity;

public interface RoadService {

    RoadEntity saveRoad(RoadMasterDto roadMasterDto);

    RoadMasterDto getDeviceById(Integer roadId);
}
