package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.entities.GeoMasterEntity;
import gov.orsac.RDVTS.repository.GeoMasterRepository;
import gov.orsac.RDVTS.repositoryImpl.GeoMasterRepositoryImpl;
import gov.orsac.RDVTS.service.GeoMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoMasterServiceImpl implements GeoMasterService {
    @Autowired
    private GeoMasterRepository geoMasterRepository;
    @Autowired
    private GeoMasterRepositoryImpl geoMasterRepositoryImpl;

    @Override
    public GeoMasterEntity saveGeoMaster(GeoMasterEntity geoMaster){
        return geoMasterRepository.save(geoMaster);
    }
}
