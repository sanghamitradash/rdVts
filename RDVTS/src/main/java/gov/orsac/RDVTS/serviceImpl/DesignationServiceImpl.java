package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.entities.DesignationEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.DesignationRepository;
import gov.orsac.RDVTS.repositoryImpl.DesignationRepositoryImpl;
import gov.orsac.RDVTS.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationServiceImpl implements DesignationService {

    @Autowired
    private DesignationRepositoryImpl designationRepositoryImpl;

    @Autowired
    private DesignationRepository designationRepository;

    RDVTSResponse rdvtsResponse = new RDVTSResponse();
    NamedParameterJdbcTemplate namedJdbc;

    @Override
    public DesignationEntity saveDesignation(DesignationEntity designationEntity) {
        return designationRepository.save(designationEntity);
    }

//    @Override
//    public RDVTSResponse getAllDesignation() {
//        List<DesignationEntity> designationEntityList = designationRepository.findAll();
//        RDVTSResponse rdvtsResponse = new RDVTSResponse();
//        rdvtsResponse.setMessage("");
//        rdvtsResponse.setData(designationEntityList);
//        rdvtsResponse.setStatus(1);
//        return rdvtsResponse;
//    }

    @Override
    public Page<DesignationDto> getDesignationList(DesignationDto designationDto) {
        return  designationRepositoryImpl.getDesignationList(designationDto);
    }

    @Override
    public List<DesignationDto> getAllDesignationByUserLevelId(int userLevelId) {
        List<DesignationDto> designationDtoList= designationRepositoryImpl.getDesignationByUserLevelId(userLevelId);
        return designationDtoList;
    }

    @Override
    public List<DesignationDto> getDesignationById(int id) {
        return designationRepositoryImpl.getDesignationById(id);
    }

    @Override
    public DesignationEntity updateDesignation(int id, DesignationDto designationDto) {
        DesignationEntity existingUser = designationRepository.findById(id);
        if (existingUser == null) {
            throw new RecordNotFoundException("DesignationEntity", "id", id);
        }
        existingUser.setName(designationDto.getName());
        existingUser.setDescription(designationDto.getDescription());
        existingUser.setParentId(designationDto.getParentId());
        existingUser.setUserLevelId(designationDto.getUserLevelId());
        existingUser.setUpdatedBy(designationDto.getUpdatedBy());


        DesignationEntity save = designationRepository.save(existingUser);
        return save;
    }

    @Override
    public Boolean activateAndDeactivateDesignation(Integer id) {
        return designationRepositoryImpl.activateAndDeactivateDesignation(id);
    }
}
