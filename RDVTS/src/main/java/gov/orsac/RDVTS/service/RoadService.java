package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.dto.RoadFilterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.dto.RoadWorkMappingDto;
import gov.orsac.RDVTS.entities.RoadEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoadService {

    RoadEntity saveRoad(RoadMasterDto roadMasterDto);

    List<RoadMasterDto> getRoadById(Integer roadId, Integer userId);

    List<RoadMasterDto> getRoadByWorkId(Integer workId);

    RoadEntity updateRoad(Integer id, RoadMasterDto roadMasterDto);
    Page<RoadMasterDto> getRoadList(RoadFilterDto roadFilterDto);

    List<GeoMasterDto> getWorkByroadIds(Integer roadId);
    List<GeoMasterDto> workByContractorIds(Integer contractorId);
    List<GeoMasterDto> getworkByDistrictId(Integer districtId);


    List<GeoMasterDto> getworkByBlockId(Integer blockId);
    List<GeoMasterDto> getworkByDivisionId(Integer divisionId);


    List<GeoMasterDto> getVehicleListByRoadId(Integer roadId);


    List<RoadMasterDto> getGeomByRoadId(Integer roadId, Integer userId);

    List<RoadWorkMappingDto> getWorkDetailsByRoadId(Integer roadId);

    List<RoadMasterDto> getRoadByRoadIds(List<Integer> roadIds);

//    RoadEntity updateRoad(Integer id, RoadMasterDto roadMasterDto);
}
