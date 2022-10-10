package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.entities.DesignationEntity;

public interface DesignationService {

    DesignationEntity saveDesignation (DesignationEntity designationEntity);

    RDVTSResponse getAllDesignation();

}
