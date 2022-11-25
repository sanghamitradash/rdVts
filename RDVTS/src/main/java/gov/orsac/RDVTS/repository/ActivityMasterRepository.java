package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.entities.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityMasterRepository extends JpaRepository <ActivityEntity,Integer> {

}
