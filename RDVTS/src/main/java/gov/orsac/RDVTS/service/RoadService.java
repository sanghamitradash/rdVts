package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.entities.RoadLocationEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoadService {

    RoadEntity saveRoad(RoadMasterDto roadMasterDto);

    List<RoadMasterDto> getRoadById(Integer roadId, Integer userId);

    List<AlertCountDto> getAlert(Integer roadId);

    List<RoadMasterDto> getRoadByWorkId(Integer workId);

    List<RoadMasterDto> getRoadByPackageId(Integer packageId);

    RoadEntity updateRoad(Integer id, RoadMasterDto roadMasterDto);
    Page<RoadMasterDto> getRoadList(RoadFilterDto roadFilterDto);

    List<GeoMasterDto> getWorkByroadIds(Integer roadId);
    List<GeoMasterDto> workByContractorIds(Integer contractorId);
    List<GeoMasterDto> getworkByDistrictId(Integer districtId);


    List<GeoMasterDto> getworkByBlockId(Integer blockId);
    List<GeoMasterDto> getworkByDivisionId(Integer divisionId);


    List<GeoMasterDto> getVehicleListByRoadId(Integer roadId);


    List<RoadMasterDto> getGeomByRoadId(Integer roadId, Integer userId);

    List<RoadWorkMappingDto> getWorkDetailsByRoadId(Integer roadId, Integer userId);

    List<RoadMasterDto> getRoadByRoadIds(List<Integer> id, List<Integer> workIds, List<Integer> distIds, List<Integer> blockIds, List<Integer> vehicleIds, List<Integer> activityIds, List<Integer> deviceIds, Integer userId, List<Integer> packageId);
    RoadStatusDropDownDto getRoadStatusDD(Integer userId);

    int updateGeom(Integer roadId, String geom, Integer userId);

    List<UnassignedRoadDDDto> unassignedRoadDD(Integer userId);

    List<RoadLocationEntity> addRoadLocation(Integer roadId, List<RoadLocationEntity> roadLocation, Integer userId);

    Integer saveGeom(Integer roadId, List<RoadLocationEntity> roadLocation, Integer userId);
    Integer saveLength(Integer roadId, List<RoadLocationEntity> roadLocation, Integer userId);

    List<GeoMasterDto> getWorkByCircleId(Integer circleObj);


//    RoadEntity updateRoad(Integer id, RoadMasterDto roadMasterDto);
}
