package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.WorkDto;

import java.util.List;

public interface ActivityRepository {
    List<ActivityDto> getActivityById(Integer activityId, Integer userId);


}
