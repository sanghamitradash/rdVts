package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.repository.*;
import gov.orsac.RDVTS.repositoryImpl.DeviceRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.VehicleRepositoryImpl;
import gov.orsac.RDVTS.service.ActivityService;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMasterSaveRepository vehicleMasterSaveRepository;
    @Autowired
    private VehicleDeviceMappingRepository vehicleDeviceMappingRepository;
    @Autowired
    private VehicleOwnerMappingRepository vehicleOwnerMappingRepository;
    @Autowired
    private VehicleRepositoryImpl vehicleRepositoryImpl;
    @Autowired
    private DeviceRepositoryImpl deviceRepositoryImpl;

    @Autowired
    private ActivityService activityService;

    @PostMapping("/addVehicle")
    public RDVTSResponse saveVehicle(@RequestParam(name = "vehicle") String vehicleData,
                                     @RequestParam(name = "vehicleDeviceMapping", required = false) String vehicleDeviceMappingData,
                                     @RequestParam(name = "vehicleActivityMapping", required = false) String VehicleActivityMappingData,
                                     @RequestParam(name = "vehicleOwnerMapping", required = false) String vehicleOwnerMappingData) throws JsonProcessingException {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        VehicleDeviceMappingEntity vehicleDeviceMapping = null;
        VehicleOwnerMappingDto vehicleOwnerMapping = null;
        List<VehicleWorkMappingDto> vehicleWorkMapping = new ArrayList<>();
        List<VehicleActivityDto> vehicleActivityMapping = new ArrayList<>();
        VehicleMaster vehicle = mapper.readValue(vehicleData, VehicleMaster.class);
        if (vehicleDeviceMappingData != null) {
            vehicleDeviceMapping = mapper.readValue(vehicleDeviceMappingData, VehicleDeviceMappingEntity.class);
        }
        if (VehicleActivityMappingData != null) {
            vehicleActivityMapping = mapper.readValue(VehicleActivityMappingData, mapper.getTypeFactory().constructCollectionType(List.class, VehicleActivityDto.class));
        }
        if (vehicleOwnerMappingData != null) {
            vehicleOwnerMapping = mapper.readValue(vehicleOwnerMappingData, VehicleOwnerMappingDto.class);
        }
        try {
            VehicleMaster vehicleNo = vehicleMasterSaveRepository.getByVehicleNo(vehicle.getVehicleNo());
            if (vehicleNo == null) {
                if (vehicle.getVehicleTypeId() != null && vehicle.getVehicleNo() != null && vehicle.getChassisNo() != null
                        && vehicle.getEngineNo() != null && vehicle.getSpeedLimit() != null) {
                    VehicleMaster saveVehicle = vehicleService.saveVehicle(vehicle);
                    result.put("saveVehicle", saveVehicle);
                    if (vehicleDeviceMappingData != null) {
                        vehicleDeviceMapping.setVehicleId(saveVehicle.getId());
                        VehicleDeviceMappingEntity assignVehicleDevice = vehicleService.assignVehicleDevice(vehicleDeviceMapping);
                        result.put("assignVehicleDevice", assignVehicleDevice);
                    }
                    if (vehicleOwnerMappingData != null) {
                        vehicleOwnerMapping.setVehicleId(saveVehicle.getId());
                        VehicleOwnerMappingEntity assignVehicleOwner = vehicleService.assignVehicleOwner(vehicleOwnerMapping);
                        result.put("assignVehicleOwner", assignVehicleOwner);
                    }
                    if (vehicleActivityMapping != null && vehicleActivityMapping.size() > 0) {
                        List<VehicleActivityDto> activity = new ArrayList<>();
                        for (VehicleActivityDto activity1 : vehicleActivityMapping) {
                            activity1.setVehicleId(saveVehicle.getId());
                            activity.add(activity1);
                        }
                        List<VehicleActivityMappingEntity> saveVehicleActivityMapping = vehicleService.assignVehicleActivity(activity);
                        result.put("assignVehicleActivityMapping", saveVehicleActivityMapping);
                    }
                    response.setData(result);
                    response.setStatus(1);
                    response.setMessage("Vehicle Created Successfully");
                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                } else {
                    response = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.OK),
                            "Vehicle Type,Vehicle No.,Vehicle Chassis No.,Vehicle Engine No.,Vehicle SpeedLiMit mandatory",
                            result);
                }
            } else {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.OK),
                        "Vehicle No Already Exist",
                        result);
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

    @PostMapping("/getVehicleByVId")
    public RDVTSListResponse getVehicleByVId(@RequestParam Integer vehicleId,
                                             @RequestParam Integer userId
                                             /*@RequestParam(name = "startDate", required = false) String startDate,
                                             @RequestParam (name = "endDate", required = false) String endDate,
                                             @RequestParam(name = "alertTypeId", required = false) Integer alertTypeId,
                                             @RequestParam(name = "start") Integer start,
                                             @RequestParam(name = "length") Integer length,
                                             @RequestParam(name = "draw") Integer draw*/) {

        AlertFilterDto alertFiler = new AlertFilterDto();
//        alertFiler.setStartDate(startDate);
//        alertFiler.setEndDate(endDate);
//        alertFiler.setAlertTypeId(alertTypeId);
//        alertFiler.setOffSet(start);
//        alertFiler.setLimit(length);
//        alertFiler.setDraw(draw);

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
            LocationDto location=vehicleService.getLocation(vehicleId);

            //alert pagination
            List<AlertDto> alertPageList=vehicleService.getAlert(alertFiler, vehicleId);
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
            result.put("location",location);
            result.put("alertList",alertPageList);
            result.put("deviceHistoryList",deviceHistory);
            result.put("workHistoryList",workHistory);
            result.put("activity",activity);
            result.put("activityList",activityList);
            response.setData(result);
            response.setStatus(1);
            response.setMessage("Vehicle By Id");
//            response.setDraw(draw);
//            response.setRecordsFiltered(alertPageList.getTotalElements());
//            response.setRecordsTotal(alertPageList.getTotalElements());
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

    @PostMapping("/updateVehicle")
    public RDVTSResponse updateVehicle(@RequestParam Integer id, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            VehicleMaster updateVehicle = mapper.readValue(data, VehicleMaster.class);
            VehicleMaster vehicle = vehicleService.updateVehicle(id, updateVehicle);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setMessage("Vehicle Update Successfully");
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

    @PostMapping("/getVehicleList")
    public RDVTSListResponse getVehicleList(@RequestParam(name = "vehicleTypeId", required = false) Integer vehicleTypeId,
                                            @RequestParam(name = "deviceId", required = false) Integer deviceId,
                                            @RequestParam(name = "workId", required = false) Integer workId,
                                            @RequestParam(name = "activityId", required = false) Integer activityId,
                                            @RequestParam(name = "start", required = false) Integer start,
                                            @RequestParam(name = "length", required = false) Integer length,
                                            @RequestParam(name = "draw", required = false) Integer draw,
                                            @RequestParam(name = "userId", required = false) Integer userId,
                                            @RequestParam(name = "distId",required = false) Integer distId,
                                            @RequestParam(name ="userByVehicleId",required = false)Integer userByVehicleId,
                                            @RequestParam(name = "divisionId",required = false) Integer divisionId,
                                            @RequestParam(name = "contractorId",required = false)Integer contractorId,
                                            @RequestParam(name = "deviceAssign", required = false) Boolean deviceAssign,
                                            @RequestParam(name = "trackingActive", required = false) Boolean trackingActive,
                                            @RequestParam(name = "activityAssign", required = false) Boolean activityAssign) {

        VehicleFilterDto vehicle = new VehicleFilterDto();
        vehicle.setVehicleTypeId(vehicleTypeId);
        vehicle.setDeviceId(deviceId);
        vehicle.setWorkId(workId);
        vehicle.setActivityId(activityId);
        vehicle.setLimit(length);
        vehicle.setOffSet(start);
        vehicle.setUserId(userId);
        vehicle.setVehicleTypeId(userByVehicleId);
        vehicle.setDistId(distId);
        vehicle.setDivisionId(divisionId);
        vehicle.setContractorId(contractorId);
        vehicle.setDraw(draw);

        //Check and Assign Filter
        if (activityAssign != null) {
            vehicle.setActivityAssign(activityAssign);
        }
        if (deviceAssign != null) {
            vehicle.setDeviceAssign(deviceAssign);
        }
        if (trackingActive != null) {
            vehicle.setTrackingAssign(trackingActive);
        }

        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
       /* try {
            Page<VehicleMasterDto> vehicleListPage=vehicleService.getVehicleList(vehicle);
            List<VehicleMasterDto> vehicleList = vehicleListPage.getContent();
            List<VehicleMasterDto> vehicleUpdatedList=new ArrayList<>();
            Integer start1=start;
            boolean tracking=false;
            for(int i=0;i<vehicleList.size();i++){
                    start1=start1+1;
                vehicleList.get(i).setSlNo(start1);
                boolean device=vehicleRepositoryImpl.getDeviceAssignedOrNot(vehicleList.get(i).getId());
                if(deviceAssign!=null||activityAssign!=null||trackingAssign!=null){
//                    if(vehicleList.get(i).getDeviceId()!=null) {
                        DeviceDto deviceData = deviceRepositoryImpl.getDeviceByIdForTracking(vehicleList.get(i).getDeviceId());
                        boolean trackingVehicle = vehicleRepositoryImpl.getTrackingLiveOrNot(deviceData.getImeiNo1());
                        tracking=trackingVehicle;
//                    }
                    if(deviceAssign!=null && deviceAssign==device){
                        if(trackingAssign!=null && trackingVehicle==trackingAssign){
                            vehicleUpdatedList.add(vehicleList.get(i));
                        }else if(trackingAssign==null) {
                            vehicleUpdatedList.add(vehicleList.get(i));
                        }
                    }else if(deviceAssign==null){
                        if(trackingAssign!=null && trackingVehicle==trackingAssign){
                            vehicleUpdatedList.add(vehicleList.get(i));
                        }else if(trackingAssign==null) {
                            vehicleUpdatedList.add(vehicleList.get(i));
                        }
//                        vehicleUpdatedList.add(vehicleList.get(i));
                    }
                }
                else{
                        boolean device1=vehicleRepositoryImpl.getDeviceAssignedOrNot(vehicleList.get(i).getId());
                        DeviceDto deviceData = deviceRepositoryImpl.getDeviceByIdForTracking(vehicleList.get(i).getDeviceId());
                        boolean trackingVehicle = vehicleRepositoryImpl.getTrackingLiveOrNot(deviceData.getImeiNo1());
                        tracking=trackingVehicle;
                    vehicleList.get(i).setDeviceAssigned(device);
                    // vehicleList.get(i).setWorkAssigned(work);
                    vehicleList.get(i).setTrackingStatus(tracking);
                    vehicleUpdatedList.add(vehicleList.get(i));
                }
               // boolean work=vehicleRepositoryImpl.getWorkAssignedOrNot(vehicleList.get(i).getId());
            }


            response.setData(vehicleList);
            response.setMessage("Vehicle List");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(vehicleListPage.getTotalElements());
            response.setRecordsTotal(vehicleListPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } */
        try {
            Page<VehicleMasterDto> vehicleListPage = vehicleService.getVehicleList(vehicle);
            List<VehicleMasterDto> vehicleList = vehicleListPage.getContent();
            List<VehicleMasterDto> vehicleDeviceList = new ArrayList<>();
            List<VehicleMasterDto> vehicleTrackingList = new ArrayList<>();
            List<VehicleMasterDto> vehicleActivityList = new ArrayList<>();

            Integer start1 = start;
            for (int i = 0; i < vehicleList.size(); i++) {
                start1 = start1 + 1;
                vehicleList.get(i).setSlNo(start1);
             /*   boolean device = vehicleRepositoryImpl.getDeviceAssignedOrNot(vehicleList.get(i).getId());
                if (vehicleList.get(i).getDeviceId() != null) {
                    DeviceDto deviceData = deviceRepositoryImpl.getDeviceByIdForTracking(vehicleList.get(i).getDeviceId());
                    boolean trackingVehicle = vehicleRepositoryImpl.getTrackingLiveOrNot(deviceData.getImeiNo1());
                    vehicleList.get(i).setTrackingStatus(trackingVehicle);
                }
                boolean activity = vehicleRepositoryImpl.getActivityAssignOrNot(vehicleList.get(i).getId());
                vehicleList.get(i).setDeviceAssigned(device);
                vehicleList.get(i).setActivityAssigned(activity);*/
            }
/*

            if (vehicleList != null && vehicleList.size() > 0) {
                for (VehicleMasterDto master : vehicleList) {
                    if (deviceAssign != null) {
                        if (deviceAssign == master.isDeviceAssigned()) {
                            vehicleDeviceList.add(master);
                        }
                    } else {
                        vehicleDeviceList.add(master);
                    }

                }
            }
            if (vehicleDeviceList != null && vehicleDeviceList.size() > 0) {
                for (VehicleMasterDto master : vehicleDeviceList) {
                    if (trackingActive != null) {
                        if (trackingActive == master.isTrackingStatus()) {
                            vehicleTrackingList.add(master);
                        }
                    } else {
                        vehicleTrackingList.add(master);
                    }

                }
            }
            if (vehicleTrackingList != null && vehicleTrackingList.size() > 0) {
                for (VehicleMasterDto master : vehicleTrackingList) {
                    if (activityAssign != null) {
                        if (activityAssign == master.isActivityAssigned()) {
                            vehicleActivityList.add(master);
                        }
                    } else {
                        vehicleActivityList.add(master);
                    }

                }
            }*/

            response.setData(vehicleList);
            // response.setData(vehicleActivityList);
            response.setMessage("Vehicle List");
            response.setStatus(1);
            response.setDraw(draw);
           // response.setRecordsFiltered(Long.valueOf(vehicleListPage.getNumberOfElements()));
            response.setRecordsFiltered(vehicleListPage.getTotalElements());
            response.setRecordsTotal(vehicleListPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }

        return response;
    }

    @PostMapping("/getVehicleTypeList")
    public RDVTSResponse getVehicleTypeList(@RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleTypeDto> vehicleTypeList = vehicleService.getVehicleTypeList();
            result.put("vehicleTypeList", vehicleTypeList);
            response.setData(result);
            response.setMessage("VehicleType List");
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

    @PostMapping("/assignVehicleDevice")
    public RDVTSResponse assignVehicleDevice(@RequestBody VehicleDeviceMappingEntity vehicleDeviceMapping) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
          /*  VehicleDeviceMappingEntity vehicleDevice=vehicleDeviceMappingRepository.findByVehicleId(vehicleDeviceMapping.getVehicleId());
                if(vehicleDevice==null){
                    VehicleDeviceMappingEntity mapped=vehicleDeviceMappingRepository.findByDeviceId(vehicleDeviceMapping.getDeviceId());
                    if(mapped==null) {*/
            //Integer count=vehicleService.deactivateVehicleDevice(vehicleDeviceMapping);
            VehicleDeviceMappingEntity saveVehicleMapping = vehicleService.assignVehicleDevice(vehicleDeviceMapping);
            result.put("saveVehicleMapping", saveVehicleMapping);
            response.setData(result);
            response.setStatus(1);
            response.setMessage("Assign Vehicle Device Updated Successfully");
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            /*        }
                    else {
                        response = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Device is Already Assigned to a Vehicle",
                                result);
                    }

            }
            else {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.OK),
                        "Vehicle is assigned to a Device ",
                        result);
            }*/
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/assignVehicleOwner")
    public RDVTSResponse assignVehicleOwner(@RequestBody VehicleOwnerMappingDto vehicleOwnerMappingDto) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            VehicleOwnerMappingEntity vehicleOwner = vehicleOwnerMappingRepository.findByVehicleId(vehicleOwnerMappingDto.getVehicleId());
            if (vehicleOwner == null) {
                VehicleOwnerMappingEntity saveVehicleOwnerMapping = vehicleService.assignVehicleOwner(vehicleOwnerMappingDto);
                result.put("saveVehicleOwner", saveVehicleOwnerMapping);
                response.setData(result);
                response.setStatus(1);
                response.setMessage("Assign Vehicle Owner Created Successfully");
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.OK),
                        "Vehicle Is Already Assigned To An Owner",
                        result);
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/assignVehicleWork")
    public RDVTSResponse assignVehicleWork(@RequestBody List<VehicleWorkMappingDto> vehicleWorkMapping) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
/*            if(vehicle.getVehicleTypeId()!=null && vehicle.getVehicleNo()!=null && vehicle.getChassisNo()!=null
                    && vehicle.getEngineNo()!=null && vehicle.getSpeedLimit()!=null) {*/

            /* Integer count = vehicleService.deactivateVehicleWork(vehicleWorkMapping);*/
            List<VehicleWorkMappingEntity> saveVehicleWorkMapping = vehicleService.assignVehicleWork(vehicleWorkMapping);
            //List<VehicleWorkMappingEntity> deactivateVehicleWork = vehicleService.deactivateVehicleWork(vehicleWorkMapping);
            result.put("saveVehicleMapping", saveVehicleWorkMapping);
            //result.put("deactivateVehicleWork", deactivateVehicleWork);
            response.setData(result);
            response.setStatus(1);
            response.setMessage("Assign Vehicle Device Created Successfully");
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
           /* }
            else {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.OK),
                        "Vehicle Type,Vehicle No.,Vehicle Chassis No.,Vehicle Engine No.,Vehicle SpeedLiMit mandatory",
                        result);
            }*/
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    /*@PostMapping("/getVehicleList")
    public RDVTSResponse getVehicleList(@RequestBody VehicleMasterDto vehicle) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<VehicleMasterDto> vehiclePageList = vehicleService.getVehicleList1(vehicle);
            List<VehicleMasterDto> vehicleList = vehiclePageList.getContent();
            result.put("vehicleList", vehicleList);
            result.put("currentPage", vehiclePageList.getNumber());
            result.put("totalItems", vehiclePageList.getTotalElements());
            result.put("totalPages", vehiclePageList.getTotalPages());
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("List of Vehicles.");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }*/

    @PostMapping("/getVehicleById")
    public RDVTSResponse getVehicleById(@RequestParam(name = "id", required = false) Integer id,
                                        @RequestParam Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleMasterDto> vehicle = vehicleService.getVehicleById(id, userId);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Vehicle By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getUnAssignedVehicleData")
    public RDVTSResponse getUnAssignedVehicleData(@RequestParam Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleMasterDto> vehicle = vehicleService.getUnAssignedVehicleData(userId);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Unassigned Vehicle Data");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getUserDropDownForVehicleOwnerMapping")
    public RDVTSResponse getUserDropDownForVehicleOwnerMapping(@RequestParam Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<UserInfoDto> user = vehicleService.getUserDropDownForVehicleOwnerMapping(userId);
            result.put("user", user);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("User Data For DropDown");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getVehicleByActivityId")
    public RDVTSResponse getVehicleByActivityId(@RequestParam(name = "activityId", required = false) Integer activityId,
                                                @RequestParam(name = "userId" )Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId, userId);
            result.put("VehicleActivityMapping", veActMapDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Vehicle By activityId");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getVehicleByVehicleTypeId")
    public RDVTSResponse getVehicleByVehicleTypeId(@RequestParam(value = "vehicleTypeId", required = false) Integer vehicleTypeId,
                                                   @RequestParam(name = "userId" )Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleMasterDto> veActMapDto = vehicleService.getVehicleByVehicleTypeId(vehicleTypeId);
            result.put("VehicleByVehicleTypeId", veActMapDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Vehicle By vehicleId");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getRoadDetailByVehicleId")
    public RDVTSResponse getRoadDetailByVehicleId(@RequestParam(value = "vehicleId", required = false) Integer vehicleId,
                                                  @RequestParam(name = "userId" )Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoadMasterDto> veActMapDto = vehicleService.getRoadDetailByVehicleId(vehicleId);
            result.put("RoadDetailByVehicleId", veActMapDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road By vehicleId");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/deactivateVehicle")
    public RDVTSResponse deactivateVehicle(@RequestParam Integer vehicleId, @RequestParam Integer status, @RequestParam  Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            if (status == 0) {
                Integer count = vehicleService.getTotalCount(vehicleId);
                if (count == 0) {
                    Boolean res2 = vehicleService.deactivateVehicleActivityMapping(vehicleId, status);
                    Boolean res1 = vehicleService.deactivateDeviceVehicleMapping(vehicleId, status);
                    Boolean res = vehicleService.deactivateVehicle(vehicleId, status);
                    if (res == true && res1 == true && res2 == true) {
                        response.setData(result);
                        response.setStatus(1);
                        response.setMessage("vehicle Deactivated ");
                        response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    }
                }
            } else if (status == 1) {
//                Integer count = vehicleService.getTotalCount(vehicleId);
//                if (count == 0) {
//                    Boolean res2 = vehicleService.deactivateVehicleActivityMapping(vehicleId, status);
//                    Boolean res1 = vehicleService.deactivateDeviceVehicleMapping(vehicleId, status);
                Boolean res = vehicleService.deactivateVehicle(vehicleId, status);
                if (res == true) {
                    response.setData(result);
                    response.setStatus(1);
                    response.setMessage("vehicle Activated ");
                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                }
            }

     else {
                response.setStatus(0);
                response.setMessage("Something went wrong");
                response.setStatusCode(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(), result);
        }
        return response;
    }

}
