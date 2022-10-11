package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.entities.DesignationEntity;

import java.util.List;

public interface DesignationService {

    DesignationEntity saveDesignation (DesignationEntity designationEntity);

    //RDVTSResponse getAllDesignation();

    List<DesignationEntity> getAllDesignationByUserLevelId(int userLevelId);

    DesignationEntity updateDesignation (int id, DesignationDto designationDto);

    Boolean activateAndDeactivateDesignation(Integer id);

}
