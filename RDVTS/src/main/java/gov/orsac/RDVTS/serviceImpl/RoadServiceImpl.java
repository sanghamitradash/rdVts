package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.entities.VTUVendorMasterEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
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
    public List<RoadMasterDto> getRoadByWorkId(Integer workId) {
        return roadRepositoryImpl.getRoadWorkById(workId);
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
    public List<RoadWorkMappingDto> getWorkDetailsByRoadId(Integer roadId) {
        return roadRepositoryImpl.getWorkDetailsByRoadId(roadId);
    }

    @Override
    public List<RoadMasterDto> getRoadByRoadIds(List<Integer> id, List<Integer> workIds, List<Integer> distIds, List<Integer> blockIds, List<Integer> vehicleIds) {
        List<Integer> workIdList = new ArrayList<>();
        List<Integer> distIdList = new ArrayList<>();
            workIdList = roadRepositoryImpl.getWorkIdsByRoadId(id);
            distIdList = roadRepositoryImpl.getDistIdsByRoadId(id);
        return roadRepositoryImpl.getRoadByRoadIds(id, workIdList, distIdList, blockIds, vehicleIds);
    }

    @Override
    public RoadStatusDropDownDto getRoadStatusDD() {
        return roadRepositoryImpl.getRoadStatusDD();
    }

    @Override
    public int updateGeom(Integer roadId, String geom) {
        return roadRepositoryImpl.updateGeom(roadId, geom);
    }

    @Override
    public List<GeoMasterDto> getVehicleListByRoadId(Integer roadId){
        return roadRepositoryImpl.getVehicleListByRoadId(roadId);
    }

}
