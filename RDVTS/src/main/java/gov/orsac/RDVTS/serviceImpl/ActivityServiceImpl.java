package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.WorkDto;
import gov.orsac.RDVTS.entities.ActivityEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.ActivityMasterRepository;
import gov.orsac.RDVTS.repository.ActivityRepository;
import gov.orsac.RDVTS.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {


    @Autowired
    ActivityMasterRepository activityMasterRepository;

    @Autowired
    ActivityRepository activityRepository;


    @Override
    public List<ActivityEntity> saveActivity(List<ActivityEntity> activityEntity) {
        List<ActivityEntity> finalList = new ArrayList<>();
        for(ActivityEntity activityEntity1 : activityEntity) {
            activityEntity1.setIsActive(true);
            finalList.add(activityEntity1);
        }
        return activityMasterRepository.saveAll(finalList);
    }

    @Override
    public List<ActivityDto> getActivityById(Integer activityId, Integer userId) {
        return activityRepository.getActivityById(activityId,userId);
    }

    @Override
    public ActivityEntity updateActivity(Integer id, ActivityDto activityData) {
        ActivityEntity existingDevice = activityMasterRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("id", "id", id));
        existingDevice.setActivityName(activityData.getActivityName());
        existingDevice.setActivityQuantity(activityData.getActivityQuantity());
        existingDevice.setActivityStartDate(activityData.getActivityStartDate());
        existingDevice.setActivityCompletionDate(activityData.getActivityCompletionDate());
        existingDevice.setActualActivityStartDate(activityData.getActualActivityStartDate());
        existingDevice.setActualActivityCompletionDate(activityData.getActualActivityCompletionDate());
        existingDevice.setExecutedQuantity(activityData.getExecutedQuantity());
        existingDevice.setWorkId(activityData.getWorkId());
        existingDevice.setActivityStatus(activityData.getActivityStatus());
        ActivityEntity save = activityMasterRepository.save(existingDevice);
        return save;
    }

    @Override
    public List<ActivityEntity> getAllActivity() {
        return activityMasterRepository.findAll();
    }



}

