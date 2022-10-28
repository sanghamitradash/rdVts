package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.VehicleActivityMappingDto;
import gov.orsac.RDVTS.entities.ActivityEntity;
import gov.orsac.RDVTS.entities.VehicleActivityMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityService {
    List<ActivityEntity> saveActivity(List<ActivityEntity> activityEntity);

    List<ActivityDto> getActivityById(Integer activityId, Integer userId);

    ActivityEntity updateActivity(Integer id, ActivityDto activityData);

    List<ActivityEntity> getAllActivity();

    Page<ActivityDto> getActivityList(ActivityListDto activity);


    List<ActivityDto> getActivityDD();


    ActivityEntity addActivity(ActivityDto activity);

    List<VehicleActivityMappingEntity> saveVehicleActivityMapping(List<VehicleActivityMappingEntity> vehicleActivityMapping, Integer activityId, Integer userId);

    Integer updateWorkId(Integer workId, Integer activityId);

    Integer updateWorkActivity(Integer workId, Integer activityId, Integer userId);

    Boolean workActivityDeassign(Integer activityId, Integer workId, Integer userId);

    Boolean vehicleActivityDeassign(Integer activityId);

    List<VehicleMaster> unassignVehicleByVehicleTypeId(Integer activityId, Integer vehicleTypeId, Integer userId);

    List<ActivityDto> unassignedActivity();

    Boolean activityVehicleDeassign(Integer vehicleId, Integer activityId);
}


