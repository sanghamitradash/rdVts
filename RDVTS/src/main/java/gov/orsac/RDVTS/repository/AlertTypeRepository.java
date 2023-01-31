package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.AlertTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertTypeRepository extends JpaRepository<AlertTypeEntity,Integer> {

    AlertTypeEntity findById(int id);

}
