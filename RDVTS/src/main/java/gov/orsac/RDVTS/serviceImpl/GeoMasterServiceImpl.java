package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.entities.GeoMasterEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.GeoMasterRepository;
import gov.orsac.RDVTS.repositoryImpl.GeoMasterRepositoryImpl;
import gov.orsac.RDVTS.service.GeoMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoMasterServiceImpl implements GeoMasterService {
    @Autowired
    private GeoMasterRepository geoMasterRepository;
    @Autowired
    private GeoMasterRepositoryImpl geoMasterRepositoryImpl;

    @Override
    public GeoMasterEntity saveGeoMaster(GeoMasterEntity geoMaster) {
        return geoMasterRepository.save(geoMaster);
    }

    @Override
    public GeoMasterEntity updateGeoMaster(int id, GeoMasterDto geoMasterDto){
        GeoMasterEntity existingGeoMaster = geoMasterRepository.findById(id);
        if (existingGeoMaster == null){
            throw new RecordNotFoundException("GeoMasterEntity", "id", id);
        }
        existingGeoMaster.setGeoWorkId(geoMasterDto.getGeoWorkId());
        existingGeoMaster.setGeoDistId(geoMasterDto.getGeoDistId());
        existingGeoMaster.setGeoBlockId(geoMasterDto.getGeoBlockId());
        existingGeoMaster.setGeoPiuId(geoMasterDto.getGeoPiuId());
        existingGeoMaster.setGeoContractorId(geoMasterDto.getGeoContractorId());
        existingGeoMaster.setWorkId(geoMasterDto.getWorkId());
        existingGeoMaster.setPiuId(geoMasterDto.getPiuId());
        existingGeoMaster.setDistId(geoMasterDto.getDistId());
        existingGeoMaster.setBlockId(geoMasterDto.getBlockId());
        existingGeoMaster.setRoadId(geoMasterDto.getRoadId());

        GeoMasterEntity save = geoMasterRepository.save(existingGeoMaster);
        return save;
    }


    public List<GeoMasterDto> getAllGeoMasterByAllId(GeoMasterDto geoMasterDto){
        return geoMasterRepositoryImpl.getAllGeoMasterByAllId(geoMasterDto);
    }
}
