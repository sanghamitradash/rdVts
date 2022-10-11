package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.DesignationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;

public interface DesignationRepository extends JpaRepository <DesignationEntity, Integer> {


    List<DesignationEntity> findAll();

    DesignationEntity findById(int id);

    List<DesignationEntity> findByUserLevelId(int userLevelId);




}