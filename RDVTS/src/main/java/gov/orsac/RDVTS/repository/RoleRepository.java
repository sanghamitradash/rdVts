package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findRoleById(Integer id);
}
