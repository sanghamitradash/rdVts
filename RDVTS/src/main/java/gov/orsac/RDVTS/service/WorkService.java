package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.WorkEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WorkService {
    WorkEntity addWork (WorkEntity work);
    Page<WorkDto> getWorkList(WorkDto workDto);
    List<WorkDto> getWorkById(int id);
    List<ActivityDto> getActivityByWorkId(int id);
    WorkEntity updateWork(int id, WorkDto workDto);

    List<VehicleMasterDto> getVehicleBywork(List<Integer> workIds);

    List<VehicleWorkMappingDto> getVehicleListByWorkId(Integer workid);

//    List<WorkDto> getUnAssignedWorkData(Integer userId);

}
