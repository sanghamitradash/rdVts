package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.entities.DesignationEntity;
import gov.orsac.RDVTS.repository.DesignationRepository;
import gov.orsac.RDVTS.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationServiceImpl implements DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    RDVTSResponse rdvtsResponse = new RDVTSResponse();
    NamedParameterJdbcTemplate namedJdbc;
    @Override
    public DesignationEntity saveDesignation (DesignationEntity designationEntity){
        return designationRepository.save(designationEntity);
    }

    @Override
    public RDVTSResponse getAllDesignation(){
        List<DesignationEntity> designationEntityList = designationRepository.findAll();
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        rdvtsResponse.setMessage("");
        rdvtsResponse.setData(designationEntityList);
        rdvtsResponse.setStatus(1);
        return rdvtsResponse;
    }
}
