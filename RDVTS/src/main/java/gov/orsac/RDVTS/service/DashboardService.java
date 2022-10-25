package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;

public interface DashboardService {
    ActiveAndInactiveVehicleDto getActiveAndInactiveVehicle();
}
