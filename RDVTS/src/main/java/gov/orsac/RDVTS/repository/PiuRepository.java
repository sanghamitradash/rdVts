package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.PiuDto;
import gov.orsac.RDVTS.entities.PiuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PiuRepository extends JpaRepository <PiuEntity, Integer> {

    List<PiuEntity> findAll();

    PiuEntity findByName(String name) ;
}
