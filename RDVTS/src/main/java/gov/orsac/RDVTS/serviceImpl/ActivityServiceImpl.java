package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.*;
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
    ActivityWorkMappingRepository activityWorkMappingRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    IssueRepository issueRepository;

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
    public ActivityWorkMapping updateActivity(Integer id, ActivityWorkMappingDto activityData) {
            ActivityWorkMapping existingActivity = activityWorkMappingRepository.findByActivityId(id);
            //BeanUtils.copyProperties(activityData, activityEntity);
            existingActivity.setActivityId(id);
            existingActivity.setActualActivityStartDate(activityData.getActualActivityStartDate());
            existingActivity.setActualActivityCompletionDate(activityData.getActualActivityCompletionDate());
            existingActivity.setActivityStatus(activityData.getActivityStatus());
            return activityWorkMappingRepository.save(existingActivity);
    }


    @Override
    public List<ActivityEntity> getAllActivity(Integer userId) {
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
    public Integer updateWorkActivity(ActivityWorkDto activityWorkDto) {
        return activityRepositoryImpl.updateWorkActivity(activityWorkDto);
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
    public List<ActivityDto> unassignedActivity(Integer userId, Integer workId) {
        return activityRepositoryImpl.unassignedActivity(userId,workId);
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

    @Override
    public List<ResolvedStatusDto> resolvedStatusDD(Integer userId) {
        return activityRepositoryImpl.resolvedStatusDD(userId);
    }

    @Override
    public IssueEntity saveIssueImage(IssueDto issue, Integer id, MultipartFile issueImages) {
        IssueEntity issueImage = new IssueEntity();
        BeanUtils.copyProperties(issue, issueImage);
        issueImage.setActivityWorkId(id);
        issueImage.setIssueImage("https://ofarisbucket.s3.ap-south-1.amazonaws.com/rdvts/" + issueImages.getOriginalFilename());
        return issueRepository.save(issueImage);
    }

    @Override
    public List<ActivityWorkMapping> getActivityByIdAndWorkId(Integer activityId, Integer userId,Integer workId) {
        return activityRepositoryImpl.getActivityByIdAndWorkId(activityId,userId,workId);
    }

    @Override
    public IssueDto getIssueByWorkId(Integer workId) {
        return activityRepositoryImpl.getIssueByWorkId(workId);
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

