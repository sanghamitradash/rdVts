package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.GeoMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoMasterRepository extends JpaRepository <GeoMasterEntity, Integer> {

    GeoMasterEntity findById(int id);
}
