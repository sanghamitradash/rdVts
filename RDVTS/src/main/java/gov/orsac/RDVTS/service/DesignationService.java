package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.entities.DesignationEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DesignationService {

    DesignationEntity saveDesignation(DesignationEntity designationEntity);

//    RDVTSResponse getAllDesignation();

    Page<DesignationDto> getDesignationList(DesignationDto designationDto);

    List<DesignationDto> getAllDesignationByUserLevelId(int userLevelId);

    List<DesignationDto> getDesignationById(int id);

    DesignationEntity updateDesignation(int id, DesignationDto designationDto);

    Boolean activateAndDeactivateDesignation(Integer id);

}
