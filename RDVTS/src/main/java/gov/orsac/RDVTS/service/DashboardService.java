package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;
import gov.orsac.RDVTS.dto.CompletedAndNotCompletedWorkDto;
import gov.orsac.RDVTS.dto.DistrictWiseVehicleDto;
import gov.orsac.RDVTS.dto.DivisionWiseVehicleDto;
import io.swagger.models.auth.In;

import java.util.List;

public interface DashboardService {
    ActiveAndInactiveVehicleDto getActiveAndInactiveVehicle(Integer userId);
    CompletedAndNotCompletedWorkDto getStatusWiseWorkCount(Integer userId);
    List<DistrictWiseVehicleDto> getDistrictWiseVehicleCount(Integer userId);

    List<DivisionWiseVehicleDto> getDivisionWiseVehicleCount(Integer userId);
}
