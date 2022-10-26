package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;
import gov.orsac.RDVTS.dto.CompletedAndNotCompletedWorkDto;
import gov.orsac.RDVTS.dto.DistrictWiseVehicleDto;

import java.util.List;

public interface DashboardService {
    ActiveAndInactiveVehicleDto getActiveAndInactiveVehicle();
    CompletedAndNotCompletedWorkDto getStatusWiseWorkCount();
    List<DistrictWiseVehicleDto> getDistrictWiseVehicleCount();
}
