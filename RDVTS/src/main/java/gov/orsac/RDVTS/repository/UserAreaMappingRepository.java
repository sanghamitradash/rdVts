package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.UserAreaMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAreaMappingRepository extends JpaRepository<UserAreaMappingEntity, Integer> {
}
