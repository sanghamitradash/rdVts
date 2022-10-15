package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.entities.GeoMasterEntity;

import java.util.List;

public interface GeoMasterService {

    GeoMasterEntity saveGeoMaster(GeoMasterEntity geoMaster);

    GeoMasterEntity updateGeoMaster (int id, GeoMasterDto geoMasterDto);

    List<GeoMasterDto> getAllGeoMasterByAllId(GeoMasterDto geoMasterDto);

}
