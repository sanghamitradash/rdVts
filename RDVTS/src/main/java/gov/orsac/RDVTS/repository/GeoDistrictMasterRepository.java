package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.GeoDistrictMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GeoDistrictMasterRepository extends JpaRepository<GeoDistrictMasterEntity,Integer> {
    @Query(value = "SELECT * FROM rdvts_oltp.geo_district_m g WHERE g.g_district_name = :districtName", nativeQuery = true)
    GeoDistrictMasterEntity existsBygDistrictName(@Param("districtName") String districtName);
}
