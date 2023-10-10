package gov.orsac.RDVTS.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.service.AWSS3StorageService;
import gov.orsac.RDVTS.service.ActivityService;
import gov.orsac.RDVTS.serviceImpl.AWSS3StorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/activity")
public class ActivityController {


    @Autowired
    private AWSS3StorageService awss3StorageService;

    @Autowired
    private AWSS3StorageServiceImpl awsS3StorageServiceImpl;

    @Autowired
    private ActivityService activityService;


    @Value("${accessImagePath}")
    private String accessImagePath;

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

      public RDVTSResponse getActivityByIdAndWorkId(@RequestParam(name = "activityId") Integer activityId,
                                                    @RequestParam(name = "userId") Integer userId,
                                                    @RequestParam(name = "geoMappingId", required = false) Integer geoMappingId,
                                                    @RequestParam(name = "packageId", required = false) Integer packageId){
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            List<ActivityDto> activityWork = activityService.getActivityByIdAndWorkId(activityId, userId,geoMappingId, packageId);

//            List<IssueDto> issue = activityService.getIssueByWorkId(activityWork.get(0).getWorkId(), activityWork.get(0).getActivityId());
            List<IssueDto> issue = new ArrayList<>();

            List<VehicleMasterDto> vehicle = activityService.getVehicleByActivityId(geoMappingId, userId);
            result.put("activity", activityWork);
            result.put("issue",issue);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Activity By Id");
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getAllActivity")
    public RDVTSResponse getAllActivity(@RequestParam (name ="userId")Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityEntity> activityList = activityService.getAllActivity(userId);
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

    public RDVTSResponse updateActivity(@RequestParam Integer activityId,@RequestParam Integer geoMappingId,
                                        @RequestParam Integer userId,
                                        @RequestParam(name = "data") String data,
                                        @RequestParam (name = "issue",required = false)String  issue,
                                        @RequestParam(name = "image",required = false) MultipartFile issueImages) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ActivityWorkMappingDto activityData = mapper.readValue(data, ActivityWorkMappingDto.class);
            IssueDto issueData=mapper.readValue(issue,IssueDto.class);
//            ActivityWorkMapping activity = activityService.getActivity(activityId,geoMappingId);

//            if (activity != null){
                Integer update = activityService.updateActivity(activityId, activityData, geoMappingId);

                if (issueImages != null ) {
                IssueEntity issueImage = activityService.saveIssueImage(issueData, activityId, issueImages);
                //issueImage.setIssueImage( issueImages.getOriginalFilename());
                boolean saveIssueImage = awss3StorageService.uploadIssueImages(issueImages, String.valueOf(activityId), issueImages.getOriginalFilename());

            }
//        }
                    result.put("updateActivity", activityData);
                    response.setData(result);
                    response.setStatus(1);
                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    response.setMessage("Activity Updated successfully.");

        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getActivityDD")
    public RDVTSResponse getActivityDD(@RequestParam Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activityDD = activityService.getActivityDD(userId);
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
        try
        {
            Integer updateWorkId = activityService.updateWorkActivity(activityWork);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Activity Assigned Successfully!!");
        }
        catch (Exception e)
        {
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
            List<VehicleActivityMappingEntity> VehicleActivityMapping = activityService.saveVehicleActivityMapping(activityWork.getVehicle(),
                    activityWork.getActivityId(), activityWork.getUserId());

            Integer res = activityService.saveContractorId(activityWork.getContractorId(), activityWork.getActivityId() );

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
            Integer res1 = activityService.vehicleActivityDeassign(activityWork.getActivityId(),activityWork.getWorkId(),activityWork.getUserId());
            Integer res = activityService.workActivityDeassign(activityWork.getActivityId(), activityWork.getWorkId(), activityWork.getUserId());

            if (res > 0 && res1 > 0) {
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
            e.printStackTrace();
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
    public RDVTSResponse unassignedActivity(@RequestParam(name = "userId") Integer userId,
                                            @RequestParam(name = "workId")Integer workId ) {
        ActivityDto activity = new ActivityDto();
        activity.setWorkId(workId);
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activityDto = activityService.unassignedActivity(userId,workId);

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
            Boolean res = activityService.activityVehicleDeassign(vehicleActivityDto.getVehicleId(), vehicleActivityDto.getGeoMappingId());
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
    public RDVTSResponse activityStatusDD(@RequestParam(name = "userId" ) Integer userId) {
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


    @PostMapping("/resolvedStatusDD")
    public RDVTSResponse resolvedStatusDD(@RequestParam(name = "userId" ) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ResolvedStatusDto> resolvedStatus = activityService.resolvedStatusDD(userId);

            result.put("resolvedStatus", resolvedStatus);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Resolved Status Dropdown Dropdown");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/updateIssue")
    public RDVTSResponse updateIssue(@RequestParam int id,
                                    @RequestParam(name = "data") String data) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            IssueDto updateIssueDto = mapper.readValue(data, IssueDto.class);
            IssueEntity issueEntity = activityService.updateIssue(id, updateIssueDto);
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            rdvtsResponse.setMessage("Issue Updated Successfully");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/getAnalysisByPkgId")
    public RDVTSResponse getActivityAnalysisByPkgId(@RequestParam(name = "userId", required = false) Integer userId,
                                     @RequestParam(name = "packageId", required = false) Integer packageId) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<String> activityName=new ArrayList<>();
            List<Double> reqQuantity=new ArrayList<>();
            List<Double> executedQuantity=new ArrayList<>();
            List<ActivityAnalysisDto> actAnalysis = activityService.getActivityAnalysisByPkgId(userId, packageId);
            for (ActivityAnalysisDto data : actAnalysis) {
                activityName.add(data.getActivityName());
                reqQuantity.add(data.getReqQuantity());
                executedQuantity.add(data.getExecutedQuantity());
            }
            result.put("activityName", activityName);
            result.put("reqQuantity", reqQuantity);
            result.put("executedQuantity", executedQuantity);
            result.put("actAnalysis", actAnalysis);
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            rdvtsResponse.setMessage("Activity Analysis Details.");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }


}
