package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.DashboardCronEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardCron extends JpaRepository<DashboardCronEntity, Integer> {
    DashboardCronEntity findDashBoardCronByAreaId(Integer areaId);
}
