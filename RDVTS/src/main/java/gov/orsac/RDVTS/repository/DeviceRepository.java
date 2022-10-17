package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<DeviceEntity,Integer> {
    Boolean existsByImeiNo1(Long imeiNo1);

    Boolean existsByImeiNo2(Long imeiNo2);
}
