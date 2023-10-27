package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DashboardCronEntity;
import io.swagger.models.auth.In;

import java.util.List;

public interface DashboardService {
    ActiveAndInactiveVehicleDto getActiveAndInactiveVehicle(Integer userId);
    List<DashboardCronEntity>  getActiveAndInactiveVehicleCron(Integer userId);
    CompletedAndNotCompletedWorkDto getStatusWiseWorkCount(Integer userId);
    CompletedAndNotCompletedRoadDto getStatusWiseRoadCount(Integer userId);
    List<DistrictWiseVehicleDto> getDistrictWiseVehicleCount(Integer userId);

    List<DivisionWiseVehicleDto> getDivisionWiseVehicleCount(Integer userId);
    List<DashboardDto> getDashboardData(Integer typeId);
}
