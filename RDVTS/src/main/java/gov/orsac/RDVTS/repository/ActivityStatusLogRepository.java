package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.ActivityStatusLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityStatusLogRepository extends JpaRepository<ActivityStatusLogEntity, Integer> {

}
