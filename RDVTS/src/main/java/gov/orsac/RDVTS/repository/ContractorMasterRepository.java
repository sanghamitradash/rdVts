package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.ContractorEntity;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorMasterRepository extends JpaRepository<ContractorEntity,Integer> {
    boolean existsByMobile(Long mobile);
    ContractorEntity findByName(String name);
}
