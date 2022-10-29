package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;
import gov.orsac.RDVTS.dto.DistrictWiseVehicleDto;

import java.util.List;

public interface DashboardRepository {
    Integer getTotalVehicle();
    Integer getTotalActive();
    Integer getTotalWork();
    Integer getCompletedWork();

    List<Integer> totalActiveIds();

    List<Integer> totalInactiveIds();


    List<DistrictWiseVehicleDto> getDistrictWiseVehicleCount(List<Integer> ids);
}
