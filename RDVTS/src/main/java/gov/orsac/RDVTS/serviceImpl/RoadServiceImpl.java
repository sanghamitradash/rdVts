package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.entities.VTUVendorMasterEntity;
import gov.orsac.RDVTS.repository.RoadRepository;
import gov.orsac.RDVTS.repositoryImpl.RoadRepositoryImpl;
import gov.orsac.RDVTS.service.RoadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public RoadMasterDto getRoadById(Integer roadId) {
        return roadRepositoryImpl.getRoadById(roadId);
    }
}
