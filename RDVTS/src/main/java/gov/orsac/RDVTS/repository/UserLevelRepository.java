package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.UserLevelMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLevelRepository extends JpaRepository<UserLevelMaster, Integer> {
    UserLevelMaster findUserLevelById(Integer id);
}
