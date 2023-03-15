package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.GeoMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface GeoMappingRepository extends JpaRepository <GeoMappingEntity, Integer> {


    @Transactional
    @Modifying
    @Query(
            value = "truncate table rdvts_oltp.geo_mapping",
            nativeQuery = true
    )
    void truncateMyTable();

}
