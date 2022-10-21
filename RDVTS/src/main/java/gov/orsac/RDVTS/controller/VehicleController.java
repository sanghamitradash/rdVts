package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.repository.VehicleDeviceMappingRepository;
import gov.orsac.RDVTS.repository.VehicleMasterSaveRepository;
import gov.orsac.RDVTS.repository.VehicleOwnerMappingRepository;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.repositoryImpl.DeviceRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.VehicleRepositoryImpl;
import gov.orsac.RDVTS.service.MasterService;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.BeanUtils;
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
    private VehicleRepositoryImpl  vehicleRepositoryImpl;
    @Autowired
    private DeviceRepositoryImpl deviceRepositoryImpl;
    @PostMapping("/addVehicle")
    public RDVTSResponse saveVehicle(@RequestParam(name = "vehicle") String vehicleData,
                                     @RequestParam (name = "vehicleDeviceMapping",required = false) String vehicleDeviceMappingData,
                                     @RequestParam (name = "vehicleWorkMapping",required = false) String vehicleWorkMappingData,
                                     @RequestParam (name = "vehicleOwnerMapping",required = false) String vehicleOwnerMappingData) throws JsonProcessingException {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        VehicleDeviceMappingEntity vehicleDeviceMapping=null;
        VehicleOwnerMappingDto vehicleOwnerMapping=null;
        List<VehicleWorkMappingDto> vehicleWorkMapping=new ArrayList<>();
        VehicleMaster vehicle=mapper.readValue(vehicleData,VehicleMaster.class);
        if (vehicleDeviceMappingData != null) {
           vehicleDeviceMapping = mapper.readValue(vehicleDeviceMappingData, VehicleDeviceMappingEntity.class);
        }
        if(vehicleWorkMappingData!=null) {
             vehicleWorkMapping = mapper.readValue(vehicleWorkMappingData, mapper.getTypeFactory().constructCollectionType(List.class, VehicleWorkMappingDto.class));
        }
        if(vehicleOwnerMappingData!=null) {
            vehicleOwnerMapping = mapper.readValue(vehicleOwnerMappingData, VehicleOwnerMappingDto.class);
        }
        try {
           VehicleMaster vehicleNo= vehicleMasterSaveRepository.getByVehicleNo(vehicle.getVehicleNo());
           if(vehicleNo.getVehicleNo()==null) {
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
                   if (vehicleWorkMapping != null && vehicleWorkMapping.size() > 0) {
                       List<VehicleWorkMappingDto> work = new ArrayList<>();
                       for (VehicleWorkMappingDto work1 : vehicleWorkMapping) {
                           work1.setVehicleId(saveVehicle.getId());
                           work.add(work1);
                       }
                       List<VehicleWorkMappingEntity> saveVehicleWorkMapping = vehicleService.assignVehicleWork(work);
                       result.put("assignVehicleWorkMapping", saveVehicleWorkMapping);
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
           }
             else {
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
    public RDVTSResponse getVehicleByVId(@RequestParam Integer vehicleId,@RequestParam Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleId);
            VehicleDeviceInfo device=vehicleService.getVehicleDeviceMapping(vehicleId);
            List<VehicleWorkMappingDto> work=vehicleService.getVehicleWorkMapping(vehicleId);
            LocationDto location=vehicleService.getLocation(vehicleId);
            List<AlertDto> alertList=vehicleService.getAlert(vehicleId);
            List<VehicleDeviceInfo> deviceHistory=vehicleService.getVehicleDeviceMappingAssignedList(vehicleId);
            List<VehicleWorkMappingDto> workHistory=vehicleService.getVehicleWorkMappingList(vehicleId);

            result.put("vehicle", vehicle);
            result.put("device",device);
            result.put("work",work);
            result.put("location",location);
            result.put("alertList",alertList);
            result.put("deviceHistoryList",deviceHistory);
            result.put("workHistoryList",workHistory);
            response.setData(result);
            response.setStatus(1);
            response.setMessage("Vehicle By Id");
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
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
    public RDVTSListResponse getVehicleList(@RequestParam(name = "vehicleTypeId") Integer vehicleTypeId,
                                            @RequestParam(name = "deviceId") Integer deviceId,
                                            @RequestParam(name = "workId") Integer workId,
                                            @RequestParam(name = "start") Integer start,
                                            @RequestParam(name = "length") Integer length,
                                            @RequestParam(name = "draw") Integer draw,
                                            @RequestParam(name = "userId") Integer userId) {

        VehicleFilterDto vehicle = new VehicleFilterDto();
        vehicle.setVehicleTypeId(vehicleTypeId);
        vehicle.setDeviceId(deviceId);
        vehicle.setWorkId(workId);
        vehicle.setLimit(length);
        vehicle.setOffSet(start);
        vehicle.setUserId(userId);
        vehicle.setDraw(draw);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<VehicleMasterDto> vehicleListPage=vehicleService.getVehicleList(vehicle);
            List<VehicleMasterDto> vehicleList = vehicleListPage.getContent();
            Integer start1=start;
            for(int i=0;i<vehicleList.size();i++){
                    start1=start1+1;
                vehicleList.get(i).setSlNo(start1);
             /*   boolean device=vehicleRepositoryImpl.getDeviceAssignedOrNot(vehicleList.get(i).getId());
                boolean work=vehicleRepositoryImpl.getWorkAssignedOrNot(vehicleList.get(i).getId());
                DeviceDto  deviceData  = deviceRepositoryImpl.getDeviceByIdForTracking(vehicleList.get(i).getDeviceId());
               boolean tracking=vehicleRepositoryImpl.getTrackingLiveOrNot(deviceData.getImeiNo1());*/
                vehicleList.get(i).setDeviceAssigned(true);
                vehicleList.get(i).setWorkAssigned(true);
                vehicleList.get(i).setTrackingStatus(true);
            }
           /* result.put("vehicleList", vehicleList);
            result.put("recordsFiltered", vehicleListPage.getTotalElements());
            result.put("recordsTotal", vehicleListPage.getTotalElements());
            result.put("draw", draw);*/
            response.setData(vehicleList);
            response.setMessage("Vehicle List");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(vehicleListPage.getTotalElements());
            response.setRecordsTotal(vehicleListPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),e.getMessage(),result);
        }

        return response;
    }
    @PostMapping("/getVehicleTypeList")
    public RDVTSResponse getVehicleTypeList(@RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleTypeDto> vehicleTypeList=vehicleService.getVehicleTypeList();
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
                        response.setMessage("Assign Vehicle Device Created Successfully");
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
            VehicleOwnerMappingEntity vehicleOwner=vehicleOwnerMappingRepository.findByVehicleId(vehicleOwnerMappingDto.getVehicleId());
            if(vehicleOwner==null){
            VehicleOwnerMappingEntity saveVehicleOwnerMapping = vehicleService.assignVehicleOwner(vehicleOwnerMappingDto);
            result.put("saveVehicleOwner", saveVehicleOwnerMapping);
            response.setData(result);
            response.setStatus(1);
            response.setMessage("Assign Vehicle Owner Created Successfully");
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            }
            else {
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
    public RDVTSResponse getVehicleById(@RequestParam(name = "id", required = false) Integer id, Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleMasterDto> vehicle = vehicleService.getVehicleById(id, userId);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/getUnAssignedVehicleData")
    public RDVTSResponse getUnAssignedVehicleData(@RequestParam  Integer userId) {
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
    public RDVTSResponse getUserDropDownForVehicleOwnerMapping(@RequestParam  Integer userId) {
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

    @PostMapping("/addVehicleActivityMapping")
    public RDVTSResponse saveVTUVendor(@RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            VehicleActivityMappingEntity vehicleActivityMappingEntity = new VehicleActivityMappingEntity();
            ObjectMapper mapper = new ObjectMapper();
            vehicleActivityMappingEntity = mapper.readValue(data, VehicleActivityMappingEntity.class);

            vehicleActivityMappingEntity = vehicleService.addVehicleActivityMapping(vehicleActivityMappingEntity);
            result.put("addVehicleActivityMapping", vehicleActivityMappingEntity);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Vehicle Activity Created Successfully!!");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getVehicleByActivityId")
    public RDVTSResponse getVehicleByActivityId(@RequestParam(name = "activityId", required = false) Integer activityId, Integer userId) {
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
}
