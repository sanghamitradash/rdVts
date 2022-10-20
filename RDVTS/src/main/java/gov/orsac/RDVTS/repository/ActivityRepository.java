package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.ActivityDto;

public interface ActivityRepository {
    ActivityDto getActivityById(Integer activityId, Integer userId);
}
