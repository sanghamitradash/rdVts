package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.ContractorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorRepository extends JpaRepository<ContractorEntity,Integer> {
}
