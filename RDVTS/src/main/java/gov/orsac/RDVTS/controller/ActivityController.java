package gov.orsac.RDVTS.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ActivityEntity;
import gov.orsac.RDVTS.entities.UserAreaMappingEntity;
import gov.orsac.RDVTS.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                         @RequestParam(name = "userId",required = false)Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activity = activityService.getActivityById(activityId,userId);

            result.put("activity", activity);

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
    public RDVTSListResponse getActivityList(@RequestParam(name = "activityStatus") Integer activityStatus,
                                            @RequestParam(name = "roadId") Integer roadId,
                                            @RequestParam(name = "workId") Integer workId,
                                            @RequestParam(name = "activityId")Integer activityId,
                                            @RequestParam(name = "userId") String startDate,
                                            @RequestParam(name = "userId") String endDate,
                                            @RequestParam(name = "userId") String actualStartDate,
                                            @RequestParam(name = "userId") String actualEndDate,
                                            @RequestParam(name = "start") Integer start,
                                            @RequestParam(name = "length") Integer length,
                                            @RequestParam(name = "draw") Integer draw,
                                            @RequestParam(name = "userId") Integer userId) {

        ActivityListDto activity = new ActivityListDto();
        activity.setActivityId(activityId);
        activity.setActivityStatus(activityStatus);
        activity.setWorkId(workId);
        activity.setRoadId(roadId);
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
            Page<ActivityDto> activityListPage=activityService.getActivityList(activity);
            List<ActivityDto> activityList = activityListPage.getContent();
            Integer start1=start;
            for(int i=0;i<activityList.size();i++){
                start1=start1+1;
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
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),e.getMessage(),result);
        }

        return response;
    }

}
