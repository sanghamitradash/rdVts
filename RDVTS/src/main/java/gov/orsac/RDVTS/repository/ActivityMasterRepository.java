package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityMasterRepository extends JpaRepository <ActivityEntity,Integer> {
}
