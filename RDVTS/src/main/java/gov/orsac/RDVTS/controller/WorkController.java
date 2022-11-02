package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.WorkEntity;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/work")
public class WorkController {
    @Autowired
    private WorkService workService;
    @Autowired
    private UserService userService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ContractorService contractorService;

    @Autowired
    private LocationService locationService;


    @Autowired
    private DeviceService deviceService;

    //Work Master
    @PostMapping("/addWork")
    public RDVTSResponse addWork(@RequestBody WorkEntity work) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            WorkEntity workEntity = workService.addWork(work);

            result.put("workEntity", workEntity);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getWorkList")
    public RDVTSListResponse getWorkList(@RequestParam(name = "id", required = false) Integer id,
                                         @RequestParam(name = "userId", required = false) Integer userId,
                                         @RequestParam(name = "activityId", required = false) Integer activityId,
                                         @RequestParam(name = "workStatus", required = false) Integer workStatus,
                                         @RequestParam(name = "start") Integer start,
                                         @RequestParam(name = "length") Integer length,
                                         @RequestParam(name = "draw") Integer draw) {
        WorkDto workDto = new WorkDto();
        workDto.setId(id);
        workDto.setUserId(userId);
        workDto.setActivityId(activityId);
        workDto.setWorkStatus(workStatus);
        workDto.setOffSet(start);
        workDto.setLimit(length);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<WorkDto> workDtoPage = workService.getWorkList(workDto);
            List<WorkDto> workDtoList = workDtoPage.getContent();
            Integer start1 = start;
            for (int i = 0; i < workDtoList.size(); i++) {
                start1 = start1 + 1;
                workDtoList.get(i).setSlNo(start1);
            }
            result.put("WorkDtoList", workDtoList);
            response.setData(workDtoList);
            response.setMessage("List of Work.");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(workDtoPage.getTotalElements());
            response.setRecordsTotal(workDtoPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSListResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getWorkById")
    public RDVTSResponse getWorkById(@RequestParam int id,
                                     @RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
//            List<UserAreaMappingDto> userAreaMappingDto = userService.getUserAreaMappingByUserId(userId);
            List<WorkDto> workDto = workService.getWorkById(id);
            List<VehicleMasterDto> vehicle = vehicleService.getVehicleHistoryList(id);
          //  List<LocationDto> location = vehicleService.getLocationArray(id);
            List<AlertDto> alertDtoList = vehicleService.getAlertArray(id);
            List<RoadMasterDto> roadMasterDtoList = vehicleService.getRoadArray(id);
            List<ContractorDto> contractorDtoList = contractorService.getContractorByWorkId(id);

            Date startDate = null;
            Date endDate = null;
            Date vehicleStartDate = null;
            Date vehicleendDate = null;
            Double todayDistance=0.0;
            Double totalDistance = 0.0;
            Double totalSpeedWork=0.0;
            Double avgSpeedToday=0.0;
            Integer totalActiveVehicle=0;

            //distance and speed API
            for (WorkDto workitem : workDto) {
                List<ActivityDto> activityDtoList = workService.getActivityByWorkId(workitem.getId());
                for (ActivityDto activityId : activityDtoList) {

                    List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId);
                    for (VehicleActivityMappingDto vehicleList : veActMapDto) {

                        List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(),userId);
                        for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                            List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                            //int i = 0;
                            for (DeviceDto imei : getImeiList) {
                                List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                totalActiveVehicle+=locationService.getActiveVehicle(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                totalDistance += locationService.getDistance(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                todayDistance += locationService.getTodayDistance(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                               // totalSpeed += locationService.getspeed(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                List<VtuLocationDto> vtuAvgSpeedToday=locationService.getAvgSpeedToday(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                int i=0;
                                for (VtuLocationDto vtuobj : vtuLocationDto) {
                                    i++;
                                    totalSpeedWork+= Double.parseDouble(vtuobj.getSpeed()) ;
                                }
                                totalSpeedWork=totalSpeedWork/i;
                                int j=0;
                                for (VtuLocationDto vtuTodaySpeedObj:vtuAvgSpeedToday) {
                                    j++;
                                    avgSpeedToday+=Double.parseDouble(vtuTodaySpeedObj.getSpeed()) ;
                                }
                                avgSpeedToday=avgSpeedToday/j;

                            }
                        }
                    }
                }
            }
            //Active Inactive vehicle
            List<LocationDto> locationList=new ArrayList<>();
            int totalVehicleCount=vehicleService.getvehicleCountByWorkId(id);
            Double avgDistance;
            if(Double.isNaN(totalDistance/totalVehicleCount)){
                avgDistance=0.0;
            }
            else {
                 avgDistance=totalDistance/totalVehicleCount;
            }
            Double todayAvgDistance=todayDistance/totalVehicleCount;
            Double avgSpeedWork;
            if(Double.isNaN(totalSpeedWork/totalVehicleCount)){
                avgSpeedWork=0.0;
            }
            else {
                avgSpeedWork=totalSpeedWork/totalVehicleCount;
            }
            Double percentageOfTotalActiveVehicle;
            if(Double.isNaN((totalActiveVehicle)/Double.valueOf(totalVehicleCount)*100)){
                percentageOfTotalActiveVehicle=0.0;
            }
            else {
                percentageOfTotalActiveVehicle = Double.valueOf(totalActiveVehicle)/Double.valueOf(totalVehicleCount)*100 ;
            }

            LocationDto location=new LocationDto();
            location.setTotalVehicleCount(totalVehicleCount);
            location.setDistanceTravelledTotal(totalDistance);
            location.setAvgDistanceTravelled(avgDistance);
            location.setDistanceTravelledToday(todayDistance);
            //location.setAvgDistanceTravelled(todayAvgDistance);
            //location.setSpeed(totalSpeed);
            if (Double.isNaN(avgSpeedToday)){
                location.setAvgSpeedToday(0.0);
            } else {
                location.setAvgSpeedToday(avgSpeedToday);
            }
            location.setAvgSpeedWork(avgSpeedWork);
            location.setTotalAlertToday(1);
            location.setTotalAlertWork(1);
            location.setTotalVehicleActive(totalActiveVehicle);
            if (totalVehicleCount > 0 ){
                location.setTotalInactiveVehicle(totalVehicleCount-totalActiveVehicle);
            }else {
                location.setTotalInactiveVehicle(0);
            }

            location.setPercentageOfActiveVehicle(percentageOfTotalActiveVehicle);

            //Static DAta
              // location.setDateTime(dateTime);
//                     location.setLatitude(21.7787878);
//                     location.setLongitude(80.676767);
//                     location.setSpeed(20);

//                     location.setAvgSpeed(30.2);
//                     locationList.add(location);
            locationList.add(location);

            result.put("contractorDto", contractorDtoList);
            result.put("workDto", workDto);
            result.put("vehicleArray", vehicle);
            result.put("locationArray", locationList);
            result.put("roadArray", roadMasterDtoList);
            result.put("alertArray", alertDtoList);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;

    }

    @PostMapping("/getWorkDDById")
    public RDVTSResponse getWorkDDById(@RequestParam int id,
                                       @RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
//            List<UserAreaMappingDto> userAreaMappingDto = userService.getUserAreaMappingByUserId(userId);
            List<WorkDto> workDto = workService.getWorkById(id);

            result.put("workDto", workDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;

    }

    @PostMapping("/getWorkStatusDD")
    public RDVTSResponse getWorkStatusDD(@RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
//            List<UserAreaMappingDto> userAreaMappingDto = userService.getUserAreaMappingByUserId(userId);
            List<WorkStatusDto> workStatusDto = workService.getWorkStatusDD(userId);

            result.put("workStatusDto", workStatusDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;

    }

    @PostMapping("/updateWork")
    public RDVTSResponse updateDesignation(@RequestParam int id,
                                           @RequestParam(name = "data") String data) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            WorkDto updateWorkDto = mapper.readValue(data, WorkDto.class);
            WorkEntity updateWorkEntity = workService.updateWork(id, updateWorkDto);
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            rdvtsResponse.setMessage("Work Updated Successfully");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/getActivityByWorkId")
    public RDVTSResponse getActivityByWorkId(@RequestParam int id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activityDtoList = workService.getActivityByWorkId(id);
            result.put("activityDtoList", activityDtoList);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getUnAssignedWorkDD")
    public RDVTSResponse getUnAssignedWorkData(@RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<UnassignedWorkDto> work = workService.getUnAssignedWorkData(userId);
            result.put("work", work);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Unassigned Work List");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }
}
