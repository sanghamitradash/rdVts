package gov.orsac.RDVTS.controller;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ActivityWorkMapping;
import gov.orsac.RDVTS.entities.AlertTypeEntity;
import gov.orsac.RDVTS.entities.GeoMappingEntity;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.repositoryImpl.ActivityRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.AlertRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.RoadRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.VehicleRepositoryImpl;
import gov.orsac.RDVTS.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/report")
public class ReportController {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleRepositoryImpl vehicleRepositoryImpl;

    @Autowired
    private WorkService workService;

    @Autowired
    private RoadRepositoryImpl roadRepositoryImpl;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRepositoryImpl activityRepositoryImpl;


@Autowired
private GeoMasterService geoMasterService;
@Autowired
private AlertRepositoryImpl alertRepositoryImpl;
    @Autowired
    public AlertService alertService;

    @Autowired
    private RoadService roadService;


    @PostMapping("/getVehicleInfoById")
    public RDVTSListResponse getVehicleInfoById(@RequestParam(name = "vehicleId")  Integer vehicleId,@RequestParam(name = "userId")  Integer userId){
        AlertFilterDto alertFiler = new AlertFilterDto();

        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            VehicleWorkMappingDto work=null;
            List<VehicleWorkMappingDto> workHistory=new ArrayList<>();
            VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleId);
            VehicleDeviceInfo device=vehicleService.getVehicleDeviceMapping(vehicleId);
            Integer activityId=vehicleRepositoryImpl.getActivityByVehicleId(vehicleId);
            if(activityId!=null && activityId>0) {
                work= vehicleService.getVehicleWorkMapping(activityId);
            }
            //  LocationDto location=vehicleService.getLocation(vehicleId);

            //alert pagination
            //List<AlertDto> alertPageList=vehicleService.getAlert(alertFiler, vehicleId);
//            List<AlertDto> alertList1 = alertPageList.getContent();
//            Integer start1=start;
//            for(int i=0;i<alertList1.size();i++){
//                start1=start1+1;
//                alertList1.get(i).setSlNo(start1);
//            }

            List<VehicleDeviceInfo> deviceHistory=vehicleService.getVehicleDeviceMappingAssignedList(vehicleId);
            List<Integer> activityIds=vehicleRepositoryImpl.getActivityIdsByVehicleId(vehicleId);
            if(activityIds!=null && activityIds.size()>0)
            {
                workHistory = vehicleService.getVehicleWorkMappingList(activityIds);
            }
            ActivityInfoDto activity=vehicleService.getLiveActivityByVehicleId(vehicleId);
            List<ActivityInfoDto> activityList=vehicleService.getActivityListByVehicleId(vehicleId);

            result.put("vehicle", vehicle);
            result.put("device",device);
            result.put("work",work);
           // result.put("location",location);
            //result.put("alertList",alertPageList);
            result.put("deviceHistoryList",deviceHistory);
            result.put("workHistoryList",workHistory);
            result.put("activity",activity);
            result.put("activityList",activityList);
            response.setData(result);
            response.setStatus(1);
            response.setMessage("Vehicle Information");

            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        }
        catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSListResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getAlertInfo")
    public Object getAlertInfo(@RequestParam(name = "vehicleId" ,required = false)  Integer vehicleId,
                               @RequestParam(name = "roadId" ,required = false)  Integer roadId,
                               @RequestParam(name = "workId" ,required = false)  Integer workId,
                               @RequestParam(name = "packageId" ,required = false)  Integer packageId,
                               @RequestParam(name = "userId")  Integer userId,
                               @RequestParam(name = "startDate", required = false) String startDate,
                               @RequestParam(name = "alertTypeId") Integer alertTypeId,
                               @RequestParam(name = "endDate", required = false) String endDate){

        RDVTSAlertResponse response = new RDVTSAlertResponse();
        Map<String, Object> result = new HashMap<>();
        AlertFilterDto filterDto = new AlertFilterDto();

        filterDto.setUserId(userId);
        filterDto.setRoadId(roadId);
        filterDto.setVehicleId(vehicleId);
        filterDto.setWorkId(workId);
        filterDto.setStartDate(startDate);
        filterDto.setEndDate(endDate);
        filterDto.setAlertTypeId(alertTypeId);
        filterDto.setPackageId(packageId);


try{
    List<VehicleMasterDto> result1 = new ArrayList<>();


//    if (packageId !=null && packageId>0) {
//        List<RoadMasterDto> roadMasterDtoList = geoMasterService.getRoadByPackageId(packageId);
//        for (RoadMasterDto road:roadMasterDtoList) {
//            List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(road.getGroadId());
//            for (GeoMasterDto item : workByRoad) {
//                List<GeoMappingEntity> activityDtoList = workService.getActivityDetailsByWorkId(item.getWorkId());
//                for (GeoMappingEntity activityId : activityDtoList) {
//                    List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId,activityId.getActivityStartDate(),activityId.getActivityCompletionDate());
//                    for (VehicleActivityMappingDto vehicleObj : veActMapDto) {
//                        VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleObj.getVehicleId());
//                        vehicle.setAlertList(alertService.getVehicleAlertForReport(filterDto));
//                        result1.add(vehicle);
//                    }
//
//                }
//            }
//        }
//
//    }
    if (packageId !=null && packageId>0) {

        List<WorkDto> workDto = new ArrayList<>();
        List<RoadMasterDto> roadMasterDtoList = new ArrayList<>();
        List<VehicleMasterDto> vehicleList = new ArrayList<>();
        List<ActivityDto> activityList = new ArrayList<>();
        List<AlertCountDto> alertList = new ArrayList<>();

        workDto = workService.getWorkById(packageId);
        roadMasterDtoList = vehicleService.getRoadArray(packageId);
        vehicleList = vehicleService.getVehicleHistoryList(packageId);

//        for (RoadMasterDto road:roadMasterDtoList) {
//            List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(road.getRoadId());
//            for (GeoMasterDto item : workByRoad) {
        activityList = workService.getActivityByWorkId(packageId);
        alertList = alertService.getVehicleAlertForReport(filterDto);
//                for (ActivityDto activityId : activityDtoList) {
//                    List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getActivityId(), userId,activityId.getActivityStartDate(),activityId.getActivityCompletionDate());
//                    for (VehicleMasterDto vehicleObj : vehicleList) {
//                        VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleObj.getId());
//                        vehicle.setAlertList(alertService.getVehicleAlertForReport(filterDto));
//                        result1.add(vehicle);
//                    }

        result.put("workList", workDto);
        result.put("roadList", roadMasterDtoList);
        result.put("vehicleList", vehicleList);
        result.put("activityList", activityList);
        result.put("alertList", alertList);
//                }
//            }
//        }

    }

    else if (roadId !=null && roadId>0) {
        List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadId);
        for (GeoMasterDto item : workByRoad) {
            List<GeoMappingEntity> activityDtoList = workService.getActivityDetailsByWorkId(item.getWorkId());
            for (GeoMappingEntity activityId : activityDtoList) {
                List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId,activityId.getActivityStartDate(),activityId.getActivityCompletionDate());
                for (VehicleActivityMappingDto vehicleObj : veActMapDto) {
                    VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleObj.getVehicleId());
                    vehicle.setAlertList(alertService.getVehicleAlertForReport(filterDto));
                    result1.add(vehicle);
                }

            }
        }
    }
    else if (workId !=null && workId>0) {
            List<ActivityDto> activityDtoList = workService.getActivityByWorkId(workId);
            for (ActivityDto activityId : activityDtoList) {
                List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId,activityId.getActivityStartDate(),activityId.getActivityCompletionDate());
                for (VehicleActivityMappingDto vehicleObj : veActMapDto) {
                    VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleObj.getVehicleId());
                    vehicle.setAlertList(alertService.getVehicleAlertForReport(filterDto));
                    result1.add(vehicle);
                }

            }

    }
    else if (vehicleId !=null && vehicleId>0){
        VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleId);
        vehicle.setAlertList(alertService.getVehicleAlertForReport(filterDto));
        result1.add(vehicle);
    }

    result.put("vehicle", result1);
    response.setData(result);
    response.setStatus(1);
    } catch (Exception e) {
        e.printStackTrace();
        response = new RDVTSAlertResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
    }
        return response;




}

    @PostMapping("/getAllAlert")
    public Object getPackage(@RequestParam(name = "userId")  Integer userId){
        RDVTSListResponse response=new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();

        try{
            List<AlertTypeEntity> alertTypeEntity = alertService.getAlertTypeDetails();
            response.setData(alertTypeEntity);
            response.setStatus(1);
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }
        return response;




    }
  /*  @PostMapping("/getReportData")
    public Object getPackage(@RequestBody AlertFilterDto filter)
    {
        RDVTSListResponse response=new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();

        try{
            PackageDto pak = alertRepositoryImpl.getPackageData(filter);
            if(pak!=null){
                List<RoadMasterDto> road=roadRepositoryImpl.getRoadByPackageIdAndRoadId(filter.getPackageId(),filter.getRoadId());
                if(road!=null) {
//                    pak.setRoad(road);
                    for (RoadMasterDto roadDto : road) {


                        List<VehicleMasterDto> vehicleData = alertRepositoryImpl.getVehicleByPackageIdAndFilter(filter, roadDto.getRoadId());
                        if (vehicleData != null && vehicleData.size()>0)
                        {

                            for (VehicleMasterDto vehi : vehicleData) {
                                List<ActivityDto> activity = alertRepositoryImpl.getActivityByVehicleId(vehi.getId());
                                vehi.setActivityList(activity);
                                List<AlertDto> alert = alertRepositoryImpl.getAlertByVehicleId(vehi.getId(),filter);
                                vehi.setAlertDataList(alert);
                            }
//                        pak.setVehicle(vehicleData);
                            roadDto.setVehicle(vehicleData);
                        }
                    }
                    pak.setRoad(road);
                }
            }
//            if(pak!=null) {
//                List<RoadMasterDto> vehicle = vehicleService.getVehicleByRoadId(filter.getRoadId());
//                if (vehicle!= null)
//                {
//                    vehicle.setVehicle(vehicle);
//                }
            result.put("packageData",pak);
            response.setData(result);
            response.setStatus(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }
        return response;
    }*/
    @PostMapping("/getReportData1")
    public Object getReportData(@RequestBody AlertFilterDto filter)
    {
        RDVTSListResponse response=new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();

        try{
            PackageDto pak = alertRepositoryImpl.getPackageData(filter);
            if(pak!=null){
                List<RoadMasterDto> roadMasterDtoList=roadRepositoryImpl.getRoadNameByPackageIdAndRoadId(filter.getPackageId(),filter.getRoadId());
                if(roadMasterDtoList!=null && !roadMasterDtoList.isEmpty()) {
                    for (RoadMasterDto road : roadMasterDtoList) {
                      List<ActivityDto> activityDtoList=alertRepositoryImpl.getActivityByRoadId(pak.getPackageId(),road.getId(),filter.getActivityId());
                      if(activityDtoList!=null && !activityDtoList.isEmpty()){
                          for(ActivityDto activity:activityDtoList) {
                              //,filter.getRoadId()
                              List<VehicleMasterDto> vehicleMasterDtoList = alertRepositoryImpl.getVehicleByActivityId(pak.getPackageId(),activity.getId(),filter.getVehicleId(),road.getId(),activity.getStartDate(),activity.getEndDate());
                              if(vehicleMasterDtoList!=null && !vehicleMasterDtoList.isEmpty()){
                                  for(VehicleMasterDto vehicle:vehicleMasterDtoList) {
                                      List<AlertDto> alert = alertRepositoryImpl.getAlertByVehicleId(vehicle.getVehicleId(), filter.getAlertTypeId());
                                      vehicle.setAlertDataList(alert);
                                  }
                              }
                              activity.setVehicleList(vehicleMasterDtoList);
                          }
                      }
                     road.setActivityList(activityDtoList);
                    }
                    pak.setRoad(roadMasterDtoList);
                }
                //to cheeck the status of the package

            }
//            if(pak!=null) {
//                List<RoadMasterDto> vehicle = vehicleService.getVehicleByRoadId(filter.getRoadId());
//                if (vehicle!= null)
//                {
//                    vehicle.setVehicle(vehicle);
//                }
            result.put("packageData",pak);
            response.setData(result);
            response.setStatus(1);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }
        return response;
    }

    @PostMapping("/getActivityByRoadId")
    public RDVTSResponse getActivityByRoadId(@RequestParam(name = "roadId") Integer roadId,
                                             @RequestParam(name = "packageId",required = false) Integer packageId)
    {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            List<ActivityDto> activityByRoadId=activityRepositoryImpl.getActivityByRoadId(roadId,packageId);
//            result.put("road", road);
            response.setData(activityByRoadId);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Activity details through roadId");
        }
        catch (Exception ex)
        {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }
}



