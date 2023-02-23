package gov.orsac.RDVTS.controller;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.repositoryImpl.VehicleRepositoryImpl;
import gov.orsac.RDVTS.service.AlertService;
import gov.orsac.RDVTS.service.VehicleService;
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
    public AlertService alertService;

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
            if(activityIds!=null && activityIds.size()>0) {
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
                               @RequestParam(name = "alertTypeId", required = false) Integer alertTypeId,
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


try{
    List<VehicleMasterDto> result1 = new ArrayList<>();


    if (vehicleId !=null){
       // VehicleMasterDto vehicleMasterDto=new VehicleMasterDto();

        VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleId);
        vehicle.setAlertCountDTos(alertService.getVehicleAlertForReport(filterDto));
        result1.add(vehicle);
    } else if (workId !=null) {

    } else if (roadId !=null) {

    }


    result.put("vehicleAlert", result1);
    response.setData(result);
    response.setStatus(1);
    } catch (Exception e) {
        e.printStackTrace();
        response = new RDVTSAlertResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
    }
        return response;




}
}
