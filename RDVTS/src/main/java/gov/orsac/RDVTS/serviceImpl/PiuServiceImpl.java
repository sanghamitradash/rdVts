package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.PiuDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.entities.PiuEntity;
import gov.orsac.RDVTS.repository.PiuRepository;
import gov.orsac.RDVTS.service.PiuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PiuServiceImpl implements PiuService {

    @Autowired
    private PiuRepository piuRepository;

    RDVTSResponse rdvtsResponse = new RDVTSResponse();
    NamedParameterJdbcTemplate namedJdbc;
    @Override
    public PiuEntity savePiu (PiuEntity piuEntity){
        return piuRepository.save(piuEntity);
    }
    @Override
    public RDVTSResponse getAllPiu(){
        List<PiuEntity> piuEntityList = piuRepository.findAll();
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        rdvtsResponse.setMessage("");
        rdvtsResponse.setData(piuEntityList);
        rdvtsResponse.setStatus(1);
        return rdvtsResponse;
    }
//    @Override
//    public PiuEntity updatePiu(int id, PiuDto piuDto) {
//
//    }
}
