package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.VehicleActivityMappingDto;
import gov.orsac.RDVTS.entities.ActivityEntity;
import gov.orsac.RDVTS.entities.VehicleActivityMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.ActivityMasterRepository;
import gov.orsac.RDVTS.repository.ActivityRepository;
import gov.orsac.RDVTS.repository.VehicleActivityMappingRepository;
import gov.orsac.RDVTS.repositoryImpl.ActivityRepositoryImpl;
import gov.orsac.RDVTS.service.ActivityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {


    @Autowired
    ActivityMasterRepository activityMasterRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ActivityRepositoryImpl activityRepositoryImpl;

    @Autowired
    VehicleActivityMappingRepository vehicleActivityMappingRepository;


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
    public ActivityEntity updateActivity(Integer id, ActivityDto activityData, MultipartFile[] issueImages) {
        ActivityEntity save = null;
        for (MultipartFile multipartFile : issueImages) {
            ActivityEntity activityEntity = new ActivityEntity();
            ActivityEntity existingActivity = activityMasterRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("id", "id", id));
            BeanUtils.copyProperties(activityData, activityEntity);
            existingActivity.setActualActivityStartDate(activityData.getActualActivityStartDate());
            existingActivity.setActualActivityCompletionDate(activityData.getActualActivityCompletionDate());
            existingActivity.setIssueImage(multipartFile.getOriginalFilename());
            existingActivity.setActivityStatus(activityData.getActivityStatus());
            save = activityMasterRepository.save(existingActivity);
        }
        return save;
    }


    @Override
    public List<ActivityEntity> getAllActivity() {
        return activityMasterRepository.findAll();
    }



    @Override
    public List<ActivityDto> getActivityDD() {
        return activityRepositoryImpl.getActivityDD();
    }

    @Override
    public Page<ActivityDto> getActivityList(ActivityListDto activity) {
        return activityRepository.getActivityList(activity);
    }

    @Override
    public ActivityEntity addActivity(ActivityDto activity) {
        ActivityEntity activityEntity = new ActivityEntity();
        BeanUtils.copyProperties(activity, activityEntity);
        return activityMasterRepository.save(activityEntity);
    }

    @Override
    public List<VehicleActivityMappingEntity> saveVehicleActivityMapping(List<VehicleActivityMappingEntity> vehicleActivityMapping, Integer activityId,Integer userId) {
        List<VehicleActivityMappingEntity> vehicleActivity = new ArrayList<>();
        for (int j = 0; j < vehicleActivityMapping.size(); j++) {
            VehicleActivityMappingEntity vehicleActivityMappingEntity = new VehicleActivityMappingEntity();
            vehicleActivityMappingEntity.setActivityId(activityId);
            vehicleActivityMappingEntity.setVehicleId(vehicleActivityMapping.get(0).getVehicleId());
            vehicleActivityMappingEntity.setStartTime(vehicleActivityMapping.get(0).getStartTime());
            vehicleActivityMappingEntity.setEndTime(vehicleActivityMapping.get(0).getEndTime());
            vehicleActivityMappingEntity.setStartDate(vehicleActivityMapping.get(0).getStartDate());
            vehicleActivityMappingEntity.setEndDate(vehicleActivityMapping.get(0).getEndDate());
            vehicleActivityMappingEntity.setIsActive(true);
            vehicleActivityMappingEntity.setCreatedBy(userId);
            vehicleActivityMappingEntity.setUpdatedBy(vehicleActivityMapping.get(0).getUpdatedBy());
            vehicleActivityMappingEntity.setDeactivationDate(vehicleActivityMapping.get(0).getDeactivationDate());
            vehicleActivityMappingEntity.setGActivityId(vehicleActivityMapping.get(0).getGActivityId());
            vehicleActivity.add(vehicleActivityMappingEntity);
        }
        return vehicleActivityMappingRepository.saveAll(vehicleActivity);
    }

    @Override
    public Integer updateWorkId(Integer workId, Integer activityId, Integer userId) {
        return activityRepositoryImpl.updateWorkId(workId, activityId, userId);
    }
    @Override
    public Integer updateWorkActivity(Integer workId, Integer activityId, Integer userId) {
        return activityRepositoryImpl.updateWorkActivity(workId, activityId, userId);
    }

    @Override
    public Boolean workActivityDeassign(Integer activityId, Integer workId, Integer userId) {
        Boolean res = activityRepositoryImpl.workActivityDeassign(activityId, workId, userId);
        return res;
    }

    @Override
    public Boolean vehicleActivityDeassign(Integer activityId) {
        Boolean res = activityRepositoryImpl.vehicleActivityDeassign(activityId);
        return res;
    }

    @Override
    public List<VehicleMaster> unassignVehicleByVehicleTypeId(Integer activityId, Integer vehicleTypeId, Integer userId) {
        return activityRepositoryImpl.unassignVehicleByVehicleTypeId(activityId, vehicleTypeId, userId);
    }

    @Override
    public List<ActivityDto> unassignedActivity(Integer userId) {
        return activityRepositoryImpl.unassignedActivity(userId);
    }

    @Override
    public Boolean activityVehicleDeassign(Integer vehicleId, Integer activityId) {
        return activityRepositoryImpl.activityVehicleDeassign(vehicleId, activityId);
    }

    @Override
    public List<ActivityStatusDto> activityStatusDD(Integer userId) {
        return activityRepositoryImpl.activityStatusDD(userId);
    }

    @Override
    public List<VehicleMasterDto> getVehicleByActivityId(Integer activityId, Integer userId) {
        return activityRepositoryImpl.getVehicleByActivityId(activityId,userId);
    }


//    @Override
//    public List<VehicleActivityMappingEntity> workActivityVehicleMap(VehicleActivityWorkMappingDto vehicleActivityWorkMappingDto) {
//        List<VehicleActivityMappingEntity> vehicleActivityMappingEntity = new ArrayList<>();
//        for(int i=0; i<=vehicleActivityWorkMappingDto.getVehicleId().size(); i++){
//            VehicleActivityMappingEntity vehicleAct = new VehicleActivityMappingEntity();
//            vehicleAct.setVehicleId(vehicleActivityWorkMappingDto.getVehicleId().get(i));
//            vehicleAct.setActivityId(vehicleActivityWorkMappingDto.getActivityId());
//            vehicleAct.setCreatedBy(vehicleActivityWorkMappingDto.getUserId());
//            vehicleAct.setIsActive(true);
//            vehicleActivityMappingEntity.add(vehicleAct);
//        }
//        return vehicleActivityMappingRepository.saveAll(vehicleActivityMappingEntity);
//    }



//    public VehicleActivityMappingEntity saveVehicleActivity(VehicleActivityWorkMappingDto vehicleActivityDto) {
////        for (VehicleActivityMappingEntity vehicleActivityMappingEntity1: vehicleActivityEntity) {
////
////        }
//        VehicleActivityMappingEntity vehicleActivityMappingEntity = new VehicleActivityMappingEntity();
//        BeanUtils.copyProperties(vehicleActivityDto, vehicleActivityMappingEntity);
//     /*   vehicleActivityMappingEntity.setVehicleId(vehicleActivityEntity.getVehicleId());
//        vehicleActivityMappingEntity.setActivityId(vehicleActivityMappingEntity.getActivityId());
//        vehicleActivityEntity.setIsActive(true);*/
//        return vehicleActivityMappingRepository.save(vehicleActivityMappingEntity);
//    }

//    @Override
//    public List<VehicleActivityMappingEntity> saveVehicleActivity(List<VehicleActivityMappingEntity> vehicleActivity, Integer id) {
//        for(VehicleActivityMappingEntity vehicleActivityMappingEntity1 : vehicleActivity) {
//            vehicleActivityMappingEntity1.setActivityId(id);
//            vehicleActivityMappingEntity1.setIsActive(true);
//        }
//        return vehicleActivityMappingRepository.saveAll(vehicleActivity);
//}





/*    @Override
    public List<VehicleActivityMappingEntity> saveVehicleActivity(List<VehicleActivityMappingDto> vehicleActivity, Integer id) {
        List<VehicleActivityMappingEntity> vehicleActivityMappingEntities=new ArrayList<>();
        for(VehicleActivityMappingDto VehicleActivity1 : vehicleActivity) {
            VehicleActivity1.setActivityId(id);
           // VehicleActivityMappingEntity vehicleActivityMappingEntity=new VehicleActivityMappingEntity();
          //  BeanUtils.copyProperties(VehicleActivity1, vehicleActivityMappingEntity);
            //vehicleActivityMappingEntities.add(vehicleActivityMappingRepository.save(vehicleActivity));
        }
        return vehicleActivityMappingRepository.saveAll(vehicleActivity);
    }*/

}

