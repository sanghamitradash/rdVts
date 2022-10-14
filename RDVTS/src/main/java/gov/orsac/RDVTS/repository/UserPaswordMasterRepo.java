package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPaswordMasterRepo extends JpaRepository <UserPasswordMasterEntity, Integer> {

    UserPasswordMasterEntity findByUserId(int userId);
}
