package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.entities.GeoMasterEntity;

import java.util.List;

public interface GeoMasterService {

    GeoMasterEntity saveGeoMaster(GeoMasterEntity geoMaster);

    GeoMasterEntity updateGeoMaster (int id, GeoMasterDto geoMasterDto);

    List<GeoMasterDto> getAllGeoMasterByAllId(int id, int geoWorkId, int geoDistId, int geoBlockId, int geoPiuId, int geoContractorId, int workId, int piuId, int distId, int blockId, int roadId);

    List<RoadMasterDto> getRoadByPackageId(Integer packageId);
}
