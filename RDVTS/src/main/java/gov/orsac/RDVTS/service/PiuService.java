package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.PiuDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.entities.PiuEntity;

public interface PiuService {

    PiuEntity savePiu(PiuEntity piuEntity);

    RDVTSResponse getAllPiu();

//    PiuEntity updatePiu(int id, PiuDto piuDto);

}
