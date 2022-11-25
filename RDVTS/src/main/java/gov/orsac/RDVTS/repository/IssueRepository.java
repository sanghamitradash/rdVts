package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.IssueEntity;
import gov.orsac.RDVTS.entities.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<IssueEntity,Integer> {

    IssueEntity findById(int id);
}
