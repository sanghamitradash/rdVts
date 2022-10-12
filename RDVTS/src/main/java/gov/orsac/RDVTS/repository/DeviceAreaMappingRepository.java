package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceAreaMappingRepository extends JpaRepository<DeviceMappingEntity,Integer> {
}
