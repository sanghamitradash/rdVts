package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.entities.RoadLocationEntity;
import gov.orsac.RDVTS.entities.VTUVendorMasterEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.RoadLocationRepository;
import gov.orsac.RDVTS.repository.RoadRepository;
import gov.orsac.RDVTS.repositoryImpl.RoadRepositoryImpl;
import gov.orsac.RDVTS.service.RoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RoadServiceImpl implements RoadService {

    @Autowired
    private RoadRepository roadRepository;

    @Autowired
    private RoadRepositoryImpl roadRepositoryImpl;

    @Autowired
    private RoadLocationRepository roadLocationRepository;

    @Override
    public RoadEntity saveRoad(RoadMasterDto roadMasterDto){
        roadMasterDto.setIsActive(true);
        roadMasterDto.setCreatedBy(1);
        roadMasterDto.setUpdatedBy(1);
        RoadEntity roadEntity = new RoadEntity();
        BeanUtils.copyProperties(roadMasterDto, roadEntity);
        return roadRepository.save(roadEntity);
    }

    @Override
    public List<RoadMasterDto> getRoadById(Integer roadId, Integer userId) {
        return roadRepositoryImpl.getRoadById(roadId, userId);
    }

    @Override
    public List<AlertCountDto> getAlert(Integer roadId) {
        return roadRepositoryImpl.getAlert(roadId);
    }

    @Override
    public List<RoadMasterDto> getRoadByWorkId(Integer workId) {
        return roadRepositoryImpl.getRoadWorkById(workId);
    }

    @Override
    public List<RoadMasterDto> getRoadByPackageId(Integer packageId)
    {
        return roadRepositoryImpl.getRoadByPackageId(packageId);
    }

    @Override
    public RoadEntity updateRoad(Integer id, RoadMasterDto roadMasterDto){
        RoadEntity existingId = roadRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("id", "id", id));
        existingId.setPackageId(roadMasterDto.getPackageId());
        existingId.setPackageName(roadMasterDto.getPackageName());
        existingId.setRoadName(roadMasterDto.getRoadName());
        existingId.setRoadLength(roadMasterDto.getRoadLength());
        existingId.setRoadLocation(roadMasterDto.getRoadLocation());
        existingId.setRoadAllignment(roadMasterDto.getRoadAllignment());
        existingId.setRoadWidth(roadMasterDto.getRoadWidth());
        existingId.setGroadId(roadMasterDto.getGroadId());
//        existingId.setGeoMasterId(roadMasterDto.getGeoMasterId());
        existingId.setCompletedRoadLength(roadMasterDto.getCompletedRoadLength());
        existingId.setSanctionDate(roadMasterDto.getSanctionDate());
        existingId.setRoadCode(roadMasterDto.getRoadCode());
        existingId.setRoadStatus(roadMasterDto.getRoadStatus());
        existingId.setApprovalStatus(roadMasterDto.getApprovalStatus());
        existingId.setApprovedBy(roadMasterDto.getApprovedBy());
        RoadEntity save = roadRepository.save(existingId);
        return save;
    }

    @Override
    public Page<RoadMasterDto> getRoadList(RoadFilterDto roadFilterDto) {
        return roadRepositoryImpl.getRoadList(roadFilterDto);
    }
    @Override
    public List<GeoMasterDto> getWorkByroadIds(Integer roadId){
        return roadRepositoryImpl.getWorkByroadIds(roadId);
    }
    @Override
    public List<GeoMasterDto> workByContractorIds(Integer contractorId){
        return roadRepositoryImpl.workByContractorIds(contractorId);
    }

    @Override
    public List<GeoMasterDto> getworkByDistrictId(Integer districtId){
        return roadRepositoryImpl.getworkByDistrictId(districtId);
    }
    @Override
    public List<GeoMasterDto> getworkByBlockId(Integer blockId){
        return roadRepositoryImpl.getworkByBlockId(blockId);
    }
    @Override
    public List<GeoMasterDto> getworkByDivisionId(Integer divisionId){
        return roadRepositoryImpl.getworkByDivisionId(divisionId);
    }




    @Override
    public List<RoadMasterDto> getGeomByRoadId(Integer roadId, Integer userId) {
        return roadRepositoryImpl.getGeomByRoadId(roadId, userId);
    }

    @Override
    public List<RoadWorkMappingDto> getWorkDetailsByRoadId(Integer roadId, Integer userId) {
        return roadRepositoryImpl.getWorkDetailsByRoadId(roadId, userId);
    }

    @Override
    public List<RoadMasterDto> getRoadByRoadIds(List<Integer> id, List<Integer> workIds, List<Integer> distIds, List<Integer> blockIds, List<Integer> vehicleIds, List<Integer> activityIds, List<Integer> deviceIds, Integer userId, List<Integer> packageId) {
        List<Integer> roadIdList = new ArrayList<>();
        List<Integer> workIdList = new ArrayList<>();
        List<Integer> distIdList = new ArrayList<>();

           /* if(workIds != null && !workIds.isEmpty()){
                roadIdList = roadRepositoryImpl.getRoadIdsByWorkId(workIds);
            }
            if(distIds != null && !distIds.isEmpty()){
                roadIdList = roadRepositoryImpl.getRoadIdsBydistIds(distIds);
            }
            if(blockIds != null && !blockIds.isEmpty()){
                roadIdList = roadRepositoryImpl.getRoadIdsByblockIds(blockIds);
            }
            if(vehicleIds != null && !vehicleIds.isEmpty()){
                roadIdList = roadRepositoryImpl.getRoadIdsByVehicleId(vehicleIds);
            }*/

        if (vehicleIds!=null && vehicleIds.size()>0){
            roadIdList=roadRepositoryImpl.getRoadIdsByVehicleIdsForFilter(vehicleIds);
        }
        if (activityIds!=null && activityIds.size()>0){
            roadIdList=roadRepositoryImpl.getRoadIdsByActivityIdsForFilter(activityIds);
        }
        if (deviceIds!=null && deviceIds.size()>0){
            roadIdList=roadRepositoryImpl.getRoadIdsByDeviceIdsForFilter(deviceIds);
        }
        if(id!=null && id.size()>0) {
            roadIdList.addAll(id);
        }
        return roadRepositoryImpl.getRoadByRoadIds(roadIdList, workIds, distIds, blockIds, vehicleIds, userId, packageId);
    }
//    @Override
//    public List<RoadMasterDto> getRoadByRoadIds(List<Integer> id, List<Integer> workIds, List<Integer> distIds, List<Integer> blockIds, List<Integer> vehicleIds, List<Integer> activityIds) {
//        return roadRepositoryImpl.getRoadByRoadIds(id, workIds, distIds, blockIds, vehicleIds, activityIds);
//    }
    @Override
    public RoadStatusDropDownDto getRoadStatusDD(Integer userId) {
        return roadRepositoryImpl.getRoadStatusDD(userId);
    }

    @Override
    public int updateGeom(Integer roadId, String geom, Integer userId) {
        return roadRepositoryImpl.updateGeom(roadId, geom, userId);
    }

    @Override
    public List<UnassignedRoadDDDto> unassignedRoadDD(Integer userId) {
        return roadRepositoryImpl.unassignedRoadDD(userId);
    }

    @Override
    public List<RoadLocationEntity> addRoadLocation(Integer roadId, List<RoadLocationEntity> roadLocation, Integer userId) {
        for (int j = 0; j < roadLocation.size(); j++) {
            roadLocation.get(j).setRoadId(roadId);
        }
        return roadLocationRepository.saveAll(roadLocation);
    }

    @Override
    public Integer saveGeom(Integer roadId, List<RoadLocationEntity> roadLocation, Integer userId) {
        return roadRepositoryImpl.saveGeom(roadId, roadLocation, userId);
    }

    @Override
    public Integer saveLength(Integer roadId, List<RoadLocationEntity> roadLocation, Integer userId) {
        return roadRepositoryImpl.saveLength(roadId, roadLocation, userId);
    }

    @Override
    public List<GeoMasterDto> getWorkByCircleId(Integer circleObj) {
        return roadRepositoryImpl.getWorkByCircleId(circleObj);
    }


    @Override
    public List<GeoMasterDto> getVehicleListByRoadId(Integer roadId){
        return roadRepositoryImpl.getVehicleListByRoadId(roadId);
    }

}
