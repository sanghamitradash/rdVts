package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.WorkDto;
import gov.orsac.RDVTS.entities.WorkEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WorkService {
    WorkEntity addWork (WorkEntity work);
    Page<WorkDto> getWorkList(WorkDto workDto);
    List<WorkDto> getWorkById(int id);
    WorkEntity updateWork(int id, WorkDto workDto);

    List<VehicleWorkMappingDto> getVehicleBywork(List<Integer> workIds);

    List<VehicleWorkMappingDto> getVehicleListByRoadId(Integer workid);

}
