package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.VehicleFilterDto;
import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.dto.VehicleTypeDto;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.entities.VehicleWorkMappingEntity;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.service.MasterService;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;
    @PostMapping("/addVehicle")
    public RDVTSResponse saveVehicle(@RequestBody VehicleMaster vehicle) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            if(vehicle.getVehicleTypeId()!=null && vehicle.getVehicleNo()!=null && vehicle.getChassisNo()!=null
                    && vehicle.getEngineNo()!=null && vehicle.getSpeedLimit()!=null) {
                VehicleMaster saveVehicle = vehicleService.saveVehicle(vehicle);
                result.put("saveVehicle", saveVehicle);
                response.setData(result);
                response.setStatus(1);
                response.setMessage("Vehicle Created Successfully");
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            }
            else {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.OK),
                        "Vehicle Type,Vehicle No.,Vehicle Chassis No.,Vehicle Engine No.,Vehicle SpeedLiMit mandatory",
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
    @PostMapping("/getVehicleByVId")
    public RDVTSResponse getVehicleByVId(@RequestParam Integer vehicleId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            VehicleMasterDto vehicle = vehicleService.getVehicleByVId(vehicleId);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setMessage("Vehicle By Id");
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
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
    public RDVTSResponse getVehicleList(@RequestParam(name = "vehicleTypeId") Integer vehicleTypeId,
                                        @RequestParam(name = "deviceId") Integer deviceId,
                                        @RequestParam(name = "work_id") Integer workId) {

        VehicleFilterDto vehicle = new VehicleFilterDto();
        vehicle.setVehicleTypeId(vehicleTypeId);
        vehicle.setDeviceId(deviceId);
        vehicle.setWorkId(workId);
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleMasterDto> vehicleList=vehicleService.getVehicleList(vehicle);
            result.put("vehicleList", vehicleList);
            response.setData(result);
            response.setMessage("Vehicle List");
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
    @PostMapping("/getVehicleTypeList")
    public RDVTSResponse getVehicleTypeList() {
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
/*            if(vehicle.getVehicleTypeId()!=null && vehicle.getVehicleNo()!=null && vehicle.getChassisNo()!=null
                    && vehicle.getEngineNo()!=null && vehicle.getSpeedLimit()!=null) {*/
                VehicleDeviceMappingEntity saveVehicleMapping = vehicleService.assignVehicleDevice(vehicleDeviceMapping);
                result.put("saveVehicleMapping", saveVehicleMapping);
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
    @PostMapping("/assignVehicleWork")
    public RDVTSResponse assignVehicleWork(@RequestBody List<VehicleWorkMappingEntity> vehicleWorkMapping) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
/*            if(vehicle.getVehicleTypeId()!=null && vehicle.getVehicleNo()!=null && vehicle.getChassisNo()!=null
                    && vehicle.getEngineNo()!=null && vehicle.getSpeedLimit()!=null) {*/
            List<VehicleWorkMappingEntity> saveVehicleWorkMapping = vehicleService.assignVehicleWork(vehicleWorkMapping);
            result.put("saveVehicleMapping", saveVehicleWorkMapping);
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

}
