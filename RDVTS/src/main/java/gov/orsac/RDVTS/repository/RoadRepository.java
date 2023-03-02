package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.RoadFilterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.entities.RoadEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadRepository extends JpaRepository<RoadEntity, Integer> {
    RoadEntity findByPackageName(String packageName);
}
