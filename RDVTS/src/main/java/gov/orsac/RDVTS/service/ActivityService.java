package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.VehicleActivityMappingDto;
import gov.orsac.RDVTS.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ActivityService {
    List<ActivityEntity> saveActivity(List<ActivityEntity> activityEntity);

    List<ActivityDto> getActivityById(Integer activityId, Integer userId);

    Integer updateActivity(Integer id, ActivityWorkMappingDto activityData);
    ActivityWorkMapping getActivity(Integer activityId, Integer workId);

    List<ActivityEntity> getAllActivity(Integer userId);

    Page<ActivityDto> getActivityList(ActivityListDto activity);


    List<ActivityDto> getActivityDD(Integer userId);


    ActivityEntity addActivity(ActivityDto activity);

    List<VehicleActivityMappingEntity> saveVehicleActivityMapping(List<VehicleActivityDto> vehicleActivityMapping, Integer geoMappingId, Integer userId);

    Integer updateWorkId(Integer workId, Integer activityId, Integer userId);

    Integer updateWorkActivity(ActivityWorkDto activityWorkDto);

    Integer workActivityDeassign(Integer activityId, Integer workId, Integer userId);

    Integer vehicleActivityDeassign(Integer activityId, Integer workId,Integer userId);

    List<VehicleMaster> unassignVehicleByVehicleTypeId(Integer activityId, Integer vehicleTypeId, Integer userId);

    List<ActivityDto> unassignedActivity(Integer userId, Integer workId);

    Boolean activityVehicleDeassign(Integer vehicleId, Integer activityId);

    List<ActivityStatusDto> activityStatusDD(Integer userId);

    List<VehicleMasterDto> getVehicleByActivityId(Integer activityId, Integer userId);

    List<ResolvedStatusDto> resolvedStatusDD(Integer userId);

    IssueEntity saveIssueImage(IssueDto issue, Integer id, MultipartFile issueImages);

    List<ActivityWorkMappingDto> getActivityByIdAndWorkId(Integer activityId, Integer userId,Integer workId);

    List<IssueDto> getIssueByWorkId(Integer workId, Integer activityId);

    IssueEntity updateIssue(int id, IssueDto issueDto);

    Integer saveContractorId(Integer contractorId, Integer activityId);
}


