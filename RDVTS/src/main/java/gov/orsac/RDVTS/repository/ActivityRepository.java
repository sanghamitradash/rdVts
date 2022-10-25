package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.ActivityListDto;
import gov.orsac.RDVTS.dto.WorkDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityRepository {
    List<ActivityDto> getActivityById(Integer activityId, Integer userId);
    Page<ActivityDto> getActivityList(ActivityListDto activity);


}
