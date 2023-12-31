package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ActivityWorkMapping;
import gov.orsac.RDVTS.entities.GeoMappingEntity;
import gov.orsac.RDVTS.entities.WorkEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.repository.WorkRepository;
import gov.orsac.RDVTS.repositoryImpl.UserRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.WorkRepositoryImpl;
import gov.orsac.RDVTS.service.HelperService;
import gov.orsac.RDVTS.service.WorkService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkServiceImpl implements WorkService {
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private WorkRepositoryImpl workRepositoryImpl;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private HelperService helperService;

    RDVTSResponse rdvtsResponse = new RDVTSResponse();
    NamedParameterJdbcTemplate namedJdbc;

    @Override
    public WorkEntity addWork(WorkEntity workEntity){
        return workRepository.save(workEntity);
    }

    @Override
    public Page<WorkDto> getWorkList(WorkDto workDto) {
        Page<WorkDto> workDtoPage = workRepositoryImpl.getWorkList(workDto);
        return workDtoPage;
    }

//    public List<PackageDto> getPackageList(PackageDto packageDto)
//    {
//        Page<PackageDto> packageDto =
//    }
    @Override
    public List<WorkDto> getWorkById(int id) {
        return workRepositoryImpl.getWorkById(id);
    }

    @Override
    public List<ActivityDto> getActivityByWorkId(int id){
        return workRepositoryImpl.getActivityByWorkId(id);
    }

    @Override
    public WorkEntity updateWork(int id, WorkDto workDto) {
        WorkEntity existingWork = workRepository.findById(id);
        if (existingWork == null) {
            throw new RecordNotFoundException("WorkEntity", "id", id);
        }
//        existingWork.setGeoWorkId(workDto.getGeoWorkId());
//        existingWork.setGeoWorkName(workDto.getGeoWorkName());
//        existingWork.setAwardDate(workDto.getAwardDate());
        existingWork.setCompletionDate(workDto.getCompletionDate());
//        existingWork.setPmisFinalizeDate(workDto.getPmisFinalizeDate());
        existingWork.setWorkStatus(workDto.getWorkStatus());
//        existingWork.setApprovalStatus(workDto.getApprovalStatus());
//        existingWork.setApprovedBy(workDto.getApprovedBy());
        existingWork.setUpdatedBy(workDto.getUpdatedBy());
//        existingWork.setCreatedBy(workDto.getCreatedBy());
//        existingWork.setUpdatedOn(workDto.getUpdatedOn());


        WorkEntity save = workRepository.save(existingWork);
        return save;
    }

    @Override
    public List<VehicleMasterDto> getVehicleBywork(List<Integer> workIds){
         return workRepositoryImpl.getVehicleBywork(workIds);
    }


    @Override
    public List<VehicleWorkMappingDto> getVehicleListByWorkId(Integer roadId){
        return workRepositoryImpl.getVehicleListByWorkId(roadId);
    }

    public Integer deactivateVehicleWork(List<VehicleWorkMappingDto> vehicleWorkMapping) throws ParseException {
        List<Integer> workIds = new ArrayList<>();
        List<Integer> vehicleIds = new ArrayList<>();
        for (VehicleWorkMappingDto vehicle: vehicleWorkMapping) {
            vehicleIds.add(vehicle.getVehicleId());
            workIds.add(vehicle.getWorkId());
        }
        return vehicleRepository.deactivateVehicleWork(workIds, vehicleIds);
    }

    @Override
    public List<UnassignedWorkDto> getUnAssignedWorkData(Integer userId) {
//        List<Integer> userIdList=new ArrayList<>();
//        UserInfoDto user=userRepositoryImpl.getUserByUserId(userId);
//        if(user.getUserLevelId()!=5){
//            userIdList=helperService.getLowerUserByUserId(userId);
//        }
        return workRepositoryImpl.getUnAssignedWorkData(userId);
    }

    @Override
    public List<GeoMappingEntity> getActivityDetailsByWorkId(Integer workId) {
        return workRepositoryImpl.getActivityDetailsByWorkId(workId);
    }

    @Override
    public List<WorkStatusDto> getWorkStatusDD(Integer userId) {
        return workRepositoryImpl.getWorkStatusDD(userId);
    }

    @Override
    public List<GeoConstructionDto> getPackageDD(Integer userId, Integer piuId, Integer distId) {
        return workRepositoryImpl.getPackageDD(userId, piuId, distId);
    }

    @Override
    public List<WorkDto> getAsignedActivityDetails(Integer id) {
        return workRepositoryImpl.getAsignedActivityDetails(id);
    }

    @Override
    public WorkDto getPackageByvehicleId(Integer vehicleId) {
        return workRepositoryImpl.getPackageByvehicleId(vehicleId);
    }

    @Override
    public Integer getPackageByvehicleIdCount(Integer vehicleId) {
        return workRepositoryImpl.getPackageByvehicleIdCount(vehicleId);
    }

    @Override
    public Integer getPackageByActivityId(Integer activityId) {
        return workRepositoryImpl.getPackageByActivityId(activityId);
    }

    @Override
    public List<PiuDto> getPiuDD(Integer userId) {
        return workRepositoryImpl.getPiuDD(userId);
    }

    @Override
    public List<VehicleMasterDto> getVehicleByPackageId(Integer userId, Integer packageId) {
        return workRepositoryImpl.getVehicleByPackageId(userId, packageId);
    }

    public Integer deactivateVehicleActivity(List<VehicleActivityDto> activity) throws ParseException {
        List<Integer> activityIds = new ArrayList<>();
        List<Integer> vehicleIds = new ArrayList<>();
        for(VehicleActivityDto vehicle :activity ){
            vehicleIds.add(vehicle.getVehicleId());
            activityIds.add(vehicle.getActivityId());
        }
        return vehicleRepository.deactivateVehicleActivity(activityIds,vehicleIds);
    }
}
