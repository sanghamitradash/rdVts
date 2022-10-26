package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;

public interface DashboardRepository {
    Integer getTotalVehicle();
    Integer getTotalActive();
    Integer getTotalWork();
    Integer getCompletedWork();
}
