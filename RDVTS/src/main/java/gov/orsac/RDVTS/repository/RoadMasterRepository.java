package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.PackageMasterEntity;
import gov.orsac.RDVTS.entities.RoadMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadMasterRepository extends JpaRepository <RoadMasterEntity, Integer> {

    PackageMasterEntity findByPackageNo(String Number) ;

}
