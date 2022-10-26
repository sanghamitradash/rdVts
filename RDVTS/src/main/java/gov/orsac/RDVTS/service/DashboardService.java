package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;
import gov.orsac.RDVTS.dto.CompletedAndNotCompletedWorkDto;

public interface DashboardService {
    ActiveAndInactiveVehicleDto getActiveAndInactiveVehicle();
    CompletedAndNotCompletedWorkDto getStatusWiseWorkCount();
}
