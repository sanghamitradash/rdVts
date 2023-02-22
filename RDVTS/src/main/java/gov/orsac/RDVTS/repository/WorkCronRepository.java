package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.DashboardCronEntity;
import gov.orsac.RDVTS.entities.WorkCronEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkCronRepository extends JpaRepository<WorkCronEntity, Integer> {
}
