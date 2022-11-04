package gov.orsac.RDVTS.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ActivityEntity;
import gov.orsac.RDVTS.entities.UserAreaMappingEntity;
import gov.orsac.RDVTS.entities.VehicleActivityMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.service.ActivityService;
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
@RequestMapping("/api/v1/activity")
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    @PostMapping("/createActivity")
    public RDVTSResponse saveActivity(@RequestBody List<ActivityEntity> activityEntity) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityEntity> activity = activityService.saveActivity(activityEntity);
            result.put("activity", activity);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage(" Activity Created Successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getActivityById")
    public RDVTSResponse getActivityById(@RequestParam(name = "activityId", required = false) Integer activityId,
                                         @RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activity = activityService.getActivityById(activityId, userId);
            List<VehicleMasterDto> vehicle = activityService.getVehicleByActivityId(activityId, userId);
            result.put("activity", activity);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Activity By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getAllActivity")
    public RDVTSResponse getAllActivity() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityEntity> activityList = activityService.getAllActivity();
            result.put("activityList", activityList);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("List of Activity");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/updateActivity")
    public RDVTSResponse updateActivity(@RequestParam Integer id, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ActivityDto activityData = mapper.readValue(data, ActivityDto.class);
            ActivityEntity activity1 = activityService.updateActivity(id, activityData);
            result.put("activity1", activity1);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Activity Updated successfully.");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getActivityDD")
    public RDVTSResponse getActivityDD() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activityDD = activityService.getActivityDD();
            result.put("activityDD", activityDD);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("List of Activity Dropdown");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getActivityList")
    public RDVTSListResponse getActivityList(@RequestParam(name = "activityStatus", required = false) Integer activityStatus,
                                             @RequestParam(name = "roadId", required = false) Integer roadId,
                                             @RequestParam(name = "workId", required = false) Integer workId,
                                             @RequestParam(name = "activityId", required = false) Integer activityId,
                                             @RequestParam(name = "vehicleId", required = false) Integer vehicleId,
                                             @RequestParam(name = "startDate", required = false) String startDate,
                                             @RequestParam(name = "endDate", required = false) String endDate,
                                             @RequestParam(name = "actualStartDate", required = false) String actualStartDate,
                                             @RequestParam(name = "actualEndDate", required = false) String actualEndDate,
                                             @RequestParam(name = "start", required = false) Integer start,
                                             @RequestParam(name = "length", required = false) Integer length,
                                             @RequestParam(name = "draw", required = false) Integer draw,
                                             @RequestParam(name = "userId", required = false) Integer userId) {

        ActivityListDto activity = new ActivityListDto();
        activity.setActivityId(activityId);
        activity.setActivityStatus(activityStatus);
        activity.setWorkId(workId);
        activity.setRoadId(roadId);
        activity.setVehicleId(vehicleId);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setActualStartDate(actualStartDate);
        activity.setActualEndDate(actualEndDate);
        activity.setLimit(length);
        activity.setOffSet(start);
        activity.setUserId(userId);
        activity.setDraw(draw);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<ActivityDto> activityListPage = activityService.getActivityList(activity);
            List<ActivityDto> activityList = activityListPage.getContent();
            Integer start1 = start;
            for (int i = 0; i < activityList.size(); i++) {
                start1 = start1 + 1;
                activityList.get(i).setSlNo(start1);
            }
            response.setData(activityList);
            response.setMessage("Activity List");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(activityListPage.getTotalElements());
            response.setRecordsTotal(activityListPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }

        return response;
    }


    @PostMapping("/addVehicleActivityWorkMapping")
    public RDVTSResponse saveVTUVendor(@RequestBody ActivityWorkDto activityWork) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            Integer updateWorkId = activityService.updateWorkId(activityWork.getWorkId(), activityWork.getActivityId(), activityWork.getUserId());
            List<VehicleActivityMappingEntity> VehicleActivityMapping = activityService.saveVehicleActivityMapping(activityWork.getVehicle(), activityWork.getActivityId(), activityWork.getUserId());
            result.put("VehicleActivityMapping", VehicleActivityMapping);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Vehicle Activity Created Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/assignWorkActivityMapping")
    public RDVTSResponse updateWorkActivity(@RequestBody ActivityWorkDto activityWork) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Integer updateWorkId = activityService.updateWorkActivity(activityWork.getWorkId(), activityWork.getActivityId(), activityWork.getUserId());
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Activity Assigned Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/addVehicleActivityMapping")
    public RDVTSResponse addVehicleActivityMapping(@RequestBody ActivityWorkDto activityWork) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            List<VehicleActivityMappingEntity> VehicleActivityMapping = activityService.saveVehicleActivityMapping(activityWork.getVehicle(), activityWork.getActivityId(), activityWork.getUserId());
            result.put("VehicleActivityMapping", VehicleActivityMapping);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Vehicle Activity Created Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/workActivityDeassign")
    public RDVTSResponse workActivityDeassign(@RequestBody ActivityWorkDto activityWork) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            Boolean res = activityService.workActivityDeassign(activityWork.getActivityId(), activityWork.getWorkId(), activityWork.getUserId());
            Boolean res1 = activityService.vehicleActivityDeassign(activityWork.getActivityId());
            if (res == true && res1 == true) {
                response.setData(result);
                response.setStatus(1);
                response.setMessage("Activity Deassigned!");
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                response.setStatus(0);
                response.setMessage("Something went wrong");
                response.setStatusCode(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/unassignVehicleByVehicleTypeId")
    public RDVTSResponse unassignVehicleByVehicleTypeId(@RequestBody ActivityWorkDto activityWork) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            List<VehicleMaster> unassignVehicle = activityService.unassignVehicleByVehicleTypeId(activityWork.getActivityId(), activityWork.getVehicleTypeId(), activityWork.getUserId());
            result.put("unassignVehicle", unassignVehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Unassigned vehicles!!");
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/unassignedActivityDD")
    public RDVTSResponse unassignedActivity(@RequestParam Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activityDto = activityService.unassignedActivity(userId);

            result.put("unassignedActivity", activityDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Unassigned Activity Dropdown");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/activityVehicleDeassign")
    public RDVTSResponse activityVehicleDeassign(@RequestBody VehicleActivityMappingDto vehicleActivityDto) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Boolean res = activityService.activityVehicleDeassign(vehicleActivityDto.getVehicleId(), vehicleActivityDto.getActivityId());
            response.setData(res);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Activity Vehicle Deassigned");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/activityStatusDD")
    public RDVTSResponse activityStatusDD(Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityStatusDto> activityStatusDropdown = activityService.activityStatusDD(userId);

            result.put("activityStatusDropdown", activityStatusDropdown);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Activity Status Dropdown Dropdown");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

}
