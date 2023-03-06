package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.GeoMappingEntity;
import gov.orsac.RDVTS.entities.GeoMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GeoMappingRepository extends JpaRepository <GeoMappingEntity, Integer> {
    @Query(value = "SELECT * FROM tutorials t WHERE t.published=true", nativeQuery = true)
    List<GeoMappingEntity> findByPublishedNative();
}
