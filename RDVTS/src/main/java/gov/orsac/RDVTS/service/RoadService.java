package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.dto.RoadFilterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.entities.RoadEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoadService {

    RoadEntity saveRoad(RoadMasterDto roadMasterDto);

    List<RoadMasterDto> getRoadById(Integer roadId, Integer userId);

    RoadEntity updateRoad(Integer id, RoadMasterDto roadMasterDto);
    Page<RoadMasterDto> getRoadList(RoadFilterDto roadFilterDto);

    List<GeoMasterDto> getWorkByroadIds(List<Integer> roadIds);
    List<GeoMasterDto> getVehicleListByRoadId(Integer roadId);


//    RoadEntity updateRoad(Integer id, RoadMasterDto roadMasterDto);
}
