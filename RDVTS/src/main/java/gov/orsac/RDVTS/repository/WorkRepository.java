package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRepository extends JpaRepository <WorkEntity, Integer> {

    WorkEntity findById(int id);
    WorkEntity findByPackageName(String packageName);
}
