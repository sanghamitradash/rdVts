package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.ActivityEntity;
import gov.orsac.RDVTS.entities.GeoMasterLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoMasterLogRepository extends JpaRepository <GeoMasterLogEntity,Integer> {

}
