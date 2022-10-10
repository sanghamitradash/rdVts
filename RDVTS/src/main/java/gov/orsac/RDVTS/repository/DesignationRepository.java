package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.DesignationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignationRepository extends JpaRepository <DesignationEntity, Integer> {

    List<DesignationEntity> findAll();
}