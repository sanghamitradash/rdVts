package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.dto.RoadFilterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.dto.VTUVendorMasterDto;
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
        existingId.setGeoMasterId(roadMasterDto.getGeoMasterId());
        RoadEntity save = roadRepository.save(existingId);
        return save;
    }

    @Override
    public Page<RoadMasterDto> getRoadList(RoadFilterDto roadFilterDto) {
        return roadRepository.getRoadList(roadFilterDto);
    }
    @Override
    public List<GeoMasterDto> getWorkByroadIds(List<Integer> roadIds){
        return roadRepositoryImpl.getWorkByroadIds(roadIds);
    }

    @Override
    public List<GeoMasterDto> getVehicleListByRoadId(Integer roadId){
        return roadRepositoryImpl.getVehicleListByRoadId(roadId);
    }

}
