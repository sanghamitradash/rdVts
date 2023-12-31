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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {


    @Autowired
    ActivityMasterRepository activityMasterRepository;
    @Autowired
    GeoMappingRepository geoMappingRepository;


    @Value("${accessImagePath}")
    private String accessImagePath;

    @Value("${saveIssueImagePath}")
    private String saveIssueImagePath;

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

    @Autowired
    ActivityStatusLogRepository activityStatusLogRepository ;

    @Autowired
    ActivityPatchDetailsRepository activityPatchDetailsRepository;


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
    public Integer updateActivity(Integer id, ActivityWorkMappingDto activityData, Integer geoMappingId) {
        /*ActivityWorkMapping existingActivity = activityRepositoryImpl.findByActivityId(id);
        existingActivity.setActivityStatus(activityData.getActivityStatus());
        existingActivity.setActivityStartDate(activityData.getActivityStartDate());
        existingActivity.setActivityCompletionDate(activityData.getActivityCompletionDate());
        ActivityWorkMapping save = activityWorkMappingRepository.save(existingActivity);*/
        Integer update=activityRepositoryImpl.updateActivity(id,activityData, geoMappingId);
        return update;
    }

    @Override
    public ActivityWorkMapping getActivity(Integer activityId, Integer workId) {
        return activityRepositoryImpl.getActivity(activityId, workId);
    }


    @Override
    public List<ActivityEntity> getAllActivity(Integer userId) {
        return activityMasterRepository.findAll();
    }



    @Override
    public List<ActivityDto> getActivityDD(Integer userId) {
        return activityRepositoryImpl.getActivityDD(userId);
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
    public List<VehicleActivityMappingEntity> saveVehicleActivityMapping(List<VehicleActivityDto> vehicleActivityMapping, Integer geoMappingId,Integer userId) {
        List<VehicleActivityMappingEntity> vehicleActivity = new ArrayList<>();
        for (int j = 0; j < vehicleActivityMapping.size(); j++) {
            VehicleActivityMappingEntity vehicleActivityMappingEntity = new VehicleActivityMappingEntity();
            vehicleActivityMappingEntity.setGeoMappingId(geoMappingId);
            vehicleActivityMappingEntity.setVehicleId(vehicleActivityMapping.get(0).getVehicleId());

            try{
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date parsedDate = dateFormat.parse(vehicleActivityMapping.get(0).getStartTime());
                Date startDate = dateFormat.parse(vehicleActivityMapping.get(0).getStartTime());
                Date endDate = dateFormat.parse(vehicleActivityMapping.get(0).getEndTime());
                vehicleActivityMappingEntity.setStartTime(startDate);
                vehicleActivityMappingEntity.setEndTime(endDate);

            }catch (Exception e){
                e.printStackTrace();
            }

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
    public Integer workActivityDeassign(Integer activityId, Integer workId, Integer userId ) {
        Integer res = activityRepositoryImpl.workActivityDeassign(activityId, workId, userId);
        return res;
    }

    @Override
    public Integer vehicleActivityDeassign(Integer activityId, Integer workId,Integer userId) {
        Integer res = activityRepositoryImpl.vehicleActivityDeassign(activityId, workId, userId);
        return res;
    }

    @Override
    public List<VehicleMaster> unassignVehicleByVehicleTypeId(Integer activityId, Integer vehicleTypeId, Integer userId) {
        return activityRepositoryImpl.unassignVehicleByVehicleTypeId(activityId, vehicleTypeId, userId);
    }

    @Override
    public List<ActivityDto> unassignedActivity(Integer userId, Integer packageId) {
        return activityRepositoryImpl.unassignedActivity(userId,packageId);
    }

    @Override
    public Boolean activityVehicleDeassign(Integer vehicleId, Integer geoMappingId) {
        return activityRepositoryImpl.activityVehicleDeassign(vehicleId, geoMappingId);
    }

    @Override
    public List<ActivityStatusDto> activityStatusDD(Integer userId) {
        return activityRepositoryImpl.activityStatusDD(userId);
    }

    @Override
    public List<VehicleMasterDto> getVehicleByActivityId(Integer geoMappingId, Integer userId) {
        return activityRepositoryImpl.getVehicleByActivityId(geoMappingId,userId);
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
        issueImage.setIssueImage( saveIssueImagePath + "/" +id+"/"+ issueImages.getOriginalFilename());
        return issueRepository.save(issueImage);
    }

    @Override
    public List<ActivityDto> getActivityByIdAndWorkId(Integer activityId, Integer userId,Integer geoMappingId, Integer packageId) {
        return activityRepositoryImpl.getActivityByIdAndWorkId(activityId,userId,geoMappingId, packageId);
    }

    @Override
    public List<IssueDto> getIssueByWorkId(Integer workId, Integer activityId) {
        return activityRepositoryImpl.getIssueByWorkId(workId,activityId);
    }

    @Override
    public IssueEntity updateIssue(int id, IssueDto issueDto) {
        IssueEntity existingIssue = issueRepository.findById(id);
        if (existingIssue == null) {
            throw new RecordNotFoundException("IssueEntity", "id", id);
        }
        existingIssue.setResolvedDate(issueDto.getResolvedDate());
        existingIssue.setResolvedStatus(issueDto.getResolvedStatus());
        existingIssue.setResolvedBy(issueDto.getResolvedBy());


        IssueEntity save = issueRepository.save(existingIssue);
        return save;
    }

    @Override
    public Integer saveContractorId(Integer contractorId, Integer activityId) {
        return activityRepositoryImpl.saveContractorId(contractorId,  activityId);
    }

    @Override
    public List<ActivityAnalysisDto> getActivityAnalysisByPkgId(Integer userId, Integer packageId) {
        List<ActivityAnalysisDto> responseDto = new ArrayList<>();
        List<ActivityAnalysisDto> actAnalysis = activityRepositoryImpl.getActivityAnalysisByPkgId(userId, packageId);
        for(int i = 0 ; i < actAnalysis.size() ; i++){
            ActivityAnalysisDto ad = new ActivityAnalysisDto();
            ad.setActivityName(actAnalysis.get(i).getActivityName());
            ad.setReqQuantity(actAnalysis.get(i).getReqQuantity());
            ad.setExecutedQuantity(actAnalysis.get(i).getExecutedQuantity());
            responseDto.add(ad);
        }
        return responseDto;
    }

    @Override
    public List<ActivityStatusLogEntity> saveActivityStatusLog(List<ActivityStatusLogEntity> activityStatusLogEntity) {
        return activityStatusLogRepository.saveAll(activityStatusLogEntity);
    }

    @Override
    public List<ActivityPatchDetailsEntity> savePatchActivityDetails(List<ActivityPatchDetailsEntity> activityPatchDetailsEntityList) {
        return activityPatchDetailsRepository.saveAll(activityPatchDetailsEntityList);
    }

    @Override
    public GeoMappingEntity assignWorkActivity(GeoMappingEntity geoMappingEntity) {
        return geoMappingRepository.save(geoMappingEntity);
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

