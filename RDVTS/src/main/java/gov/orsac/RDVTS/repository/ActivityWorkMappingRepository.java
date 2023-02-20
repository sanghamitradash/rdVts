package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.ActivityWorkMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityWorkMappingRepository extends JpaRepository<ActivityWorkMapping,Integer> {

    ActivityWorkMapping findByActivityId(Integer id);
}
