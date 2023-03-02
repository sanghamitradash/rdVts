package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.PackageMasterEntity;
import gov.orsac.RDVTS.entities.PiuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageMasterRepository extends JpaRepository <PackageMasterEntity, Integer> {



    PackageMasterEntity findByPackageNo(String Number) ;
}
