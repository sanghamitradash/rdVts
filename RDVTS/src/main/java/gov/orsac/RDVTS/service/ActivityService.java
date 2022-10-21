package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.entities.ActivityEntity;

import java.util.List;

public interface ActivityService {
    List<ActivityEntity> saveActivity(List<ActivityEntity> activityEntity);

    ActivityDto getActivityById(Integer activityId, Integer userId);

    ActivityEntity updateActivity(Integer id, ActivityDto activityData);

    List<ActivityEntity> getAllActivity();

    List<ActivityDto> getActivityDD();
}
