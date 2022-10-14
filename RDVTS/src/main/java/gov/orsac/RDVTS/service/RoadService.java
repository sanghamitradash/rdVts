package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.RoadFilterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.entities.RoadEntity;
import org.springframework.data.domain.Page;

public interface RoadService {

    RoadEntity saveRoad(RoadMasterDto roadMasterDto);

    RoadMasterDto getRoadById(Integer roadId);

    Page<RoadMasterDto> getRoadList(RoadFilterDto road);

//    RoadEntity updateRoad(Integer id, RoadMasterDto roadMasterDto);
}
