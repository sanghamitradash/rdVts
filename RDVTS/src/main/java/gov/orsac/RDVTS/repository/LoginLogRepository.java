package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.LoginLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginLogRepository extends JpaRepository <LoginLogEntity,Integer> {

}
