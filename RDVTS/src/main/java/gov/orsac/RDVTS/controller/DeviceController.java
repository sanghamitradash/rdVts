package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.exception.RecordExistException;
import gov.orsac.RDVTS.repository.DeviceRepository;
import gov.orsac.RDVTS.repositoryImpl.DeviceRepositoryImpl;
import gov.orsac.RDVTS.service.DeviceService;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/api/v1/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepositoryImpl deviceRepositoryImpl;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DeviceRepository deviceRepository;

    //Add Device

    public Boolean checkImeiNo1Exists(Long imeiNo1) {
        return deviceRepository.existsByImeiNo1(imeiNo1);
    }

    public Boolean checkImeiNo2Exists(Long imeiNo2) {
        return deviceRepository.existsByImeiNo2(imeiNo2);
    }


    //Save Device

    @PostMapping("/addDevice")
    public RDVTSResponse addDevice(@RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        if (data != null && !data.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                DeviceDto deviceDto = objectMapper.readValue(data, DeviceDto.class);

                if (deviceDto.getImeiNo1() != null && deviceDto.getImeiNo2() != null
                        && deviceDto.getMobileNumber1() != null && deviceDto.getMobileNumber2() != null
                        && deviceDto.getSimIccId1() != null && deviceDto.getSimIccId2() != null && deviceDto.getModelName() != null && deviceDto.getVtuVendorId() != null &&
                        !deviceDto.getImeiNo1().toString().isEmpty()
                        && !deviceDto.getImeiNo2().toString().isEmpty() && !deviceDto.getMobileNumber1().toString().isEmpty() &&
                        !deviceDto.getMobileNumber2().toString().isEmpty() && !deviceDto.getSimIccId1().toString().isEmpty() && !deviceDto.getSimIccId2().toString().isEmpty() && !deviceDto.getModelName().isEmpty()) {
                    if (checkImeiNo1Exists(deviceDto.getImeiNo1())) {
                        throw new RecordExistException("Device", "imeiNo1", deviceDto.getImeiNo1());
                    }
                    if (checkImeiNo2Exists(deviceDto.getImeiNo2())) {
                        throw new RecordExistException("Device", "imeiNo2", deviceDto.getImeiNo2());
                    }
                    if (deviceDto.getMobileNumber1().toString().length() >= 10 || deviceDto.getMobileNumber1().toString().length() <= 16 && deviceDto.getMobileNumber2().toString().length() >= 10 || deviceDto.getMobileNumber2().toString().length() <= 16) {
                        DeviceEntity deviceEntity = deviceService.addDevice(deviceDto);
                        result.put("deviceEntity", deviceEntity);
                        DeviceMappingEntity deviceMapping = deviceService.saveDeviceAreaMapping(deviceDto.getDeviceMapping(), deviceEntity.getId(), deviceEntity.getUserLevelId());
                        result.put("deviceMapping", deviceMapping);
                        VehicleDeviceMappingEntity vehicle = new VehicleDeviceMappingEntity();
                        BeanUtils.copyProperties(deviceDto.getVehicleDeviceMapping(), vehicle);
                        vehicle.setDeviceId(deviceEntity.getId());
                        if (vehicle.getDeviceId() != null && vehicle.getInstallationDate() != null && !vehicle.getInstalledBy().toString().isEmpty() && vehicle.getVehicleId() != null) {

                            VehicleDeviceMappingEntity saveVehicleMapping = vehicleService.assignVehicleDevice(vehicle);
                            result.put("deviceVehicleMapping", saveVehicleMapping);
                        }

                        response.setData(result);
                        response.setStatus(1);
                        response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                        response.setMessage(" Device Added Successfully");
                    } else {
                        response = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter proper mobile number !!",
                                result);
                    }

                }

                else{
                    response = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.OK),
                            "Please enter proper Information  !!",
                            result);
                }

            }
            catch (Exception ex) {
                ex.printStackTrace();
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        ex.getMessage(),
                        result);
            }
        }
        else {
            Map<String, Object> result = new HashMap<>();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.OK),
                    "No Data found!!",
                    result);
        }
        return response;
    }

    //Get Device Details By ID

    @PostMapping("/getDeviceById")
    public RDVTSResponse getDeviceById(@RequestParam(name = "deviceId", required = false) Integer deviceId,
                                       @RequestParam(name = "userId",required = false)Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<DeviceDto> device = deviceService.getDeviceById(deviceId,userId);
            List<DeviceAreaMappingDto> deviceArea = deviceService.getDeviceAreaByDeviceId(deviceId,userId);
            List<VehicleDeviceMappingDto> vehicle  = deviceService.getVehicleDeviceMappingByDeviceId(deviceId,userId);
            List<VehicleDeviceMappingDto> vehicleDeviceMap = deviceService.getAllVehicleDeviceMappingByDeviceId(deviceId,userId);
            result.put("device", device);
            result.put("deviceArea",deviceArea);
            result.put("vehicle",vehicle);
            result.put("vehicleDeviceMap",vehicleDeviceMap);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Device By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    //Get Device List

    @PostMapping("/getDeviceList")
    public RDVTSListResponse getDeviceList(@RequestParam(name = "imeiNo1",required = false) Long imeiNo1,
                                           @RequestParam(name = "simIccId1",required = false) String simIccId1,
                                           @RequestParam(name = "mobileNumber1",required = false)Long mobileNumber1,
                                           @RequestParam(name = "imeiNo2",required = false) Long imeiNo2,
                                           @RequestParam(name = "simIccId2",required = false)String simIccId2,
                                           @RequestParam(name = "mobileNumber2",required = false) Long mobileNumber2,
                                           @RequestParam(name = "vtuVendorId",required = false) Integer vtuVendorId,
                                           @RequestParam(name = "vehicleId",required = false)Integer vehicleId,
                                           @RequestParam(name = "distId",required = false) Integer distId,
                                           @RequestParam(name = "blockId",required = false) Integer blockId,
                                           @RequestParam(name = "gDistId",required = false) Integer gDistId,
                                           @RequestParam(name = "gBlockId", required = false) Integer gBlockId,
                                           @RequestParam(name = "divisionId",required = false) Integer divisionId,
                                           @RequestParam(name = "userId",required = false)Integer userId,
                                           @RequestParam(name = "start") Integer start,
                                           @RequestParam(name = "length") Integer length,
                                           @RequestParam(name = "draw") Integer draw) {
        DeviceListDto device = new DeviceListDto();
        device.setImeiNo1(imeiNo1);
        device.setSimIccId1(simIccId1);
        device.setMobileNumber1(mobileNumber1);
        device.setImeiNo2(imeiNo2);
        device.setSimIccId2(simIccId2);
        device.setMobileNumber2(mobileNumber2);
        device.setVtuVendorId(vtuVendorId);
        device.setVehicleId(vehicleId);
        device.setDistId(distId);
        device.setBlockId(blockId);
        device.setGDistId(gDistId);
        device.setGBlockId(gBlockId);
        device.setDivisionId(divisionId);
        device.setUserId(userId);
        device.setLimit(length);
        device.setOffSet(start);
        device.setDraw(draw);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            Page<DeviceInfo> deviceListPage = deviceService.getDeviceList(device);
            List<DeviceInfo> deviceList = deviceListPage.getContent();
            List<DeviceInfo> finalDeviceList=new ArrayList<>();
            Integer start1=start;
            for(int i=0;i<deviceList.size();i++){

                start1=start1+1;
                deviceList.get(i).setSlNo(start1);

                boolean device1= deviceRepositoryImpl.getDeviceAssignedOrNot(deviceList.get(i).getId());
                deviceList.get(i).setDeviceAssigned(device1);
                deviceList.get(i).setVehicleAssigned(device1);

            }
            //result.put("deviceList", deviceList);
            response.setData(deviceList);
            response.setMessage("Device List");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(deviceListPage.getTotalElements());
            response.setRecordsTotal(deviceListPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }
        return response;
    }



    //Update Device By ID

    @PostMapping("/updateDeviceById")
    public RDVTSResponse updateDeviceById(@RequestParam Integer id, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            DeviceDto deviceDto = objectMapper.readValue(data, DeviceDto.class);
            DeviceEntity updateDevice = deviceService.updateDeviceById(id, deviceDto);
            deviceService.deactivateDeviceArea(updateDevice.getId());
            DeviceMappingEntity deviceMapping = deviceService.saveDeviceAreaMapping(deviceDto.getDeviceMapping(), updateDevice.getId(), updateDevice.getUserLevelId());
            VehicleDeviceMappingEntity vehicle = new VehicleDeviceMappingEntity();
           /* BeanUtils.copyProperties(deviceDto.getVehicleDeviceMapping(), vehicle);
            vehicle.setDeviceId(updateDevice.getId());*/
            if (vehicle.getDeviceId() != null && vehicle.getInstallationDate() != null && vehicle.getInstalledBy() != null & vehicle.getVehicleId() != null) {
                deviceService.deactivateDeviceVehicle(updateDevice.getId());
                VehicleDeviceMappingEntity saveVehicleMapping = vehicleService.assignVehicleDevice(deviceDto.getVehicleDeviceMapping(), updateDevice.getId());
                result.put("saveVehicleMapping", saveVehicleMapping);
            }

                //VehicleDeviceMappingEntity saveVehicleMapping = vehicleService.assignVehicleDevice(deviceDto.getVehicleDeviceMapping(), updateDevice.getId());
                result.put("updateDevice", updateDevice);
                result.put("deviceMapping", deviceMapping);

                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("Device Updated successfully.");

        }catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    //Get UnAssigned Device By ID

    @PostMapping("/getUnassignedDeviceData")
    public RDVTSResponse getUnassignedDeviceData (@RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
           List<DeviceDto>device = deviceService.getUnassignedDeviceData(userId);

            result.put("device",device);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Get Unassigned Device Data");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    //Get Device UserLevel DropDown

    @PostMapping("/getDeviceUserLevel")
    public RDVTSResponse getDeviceUserLevel() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
           List<userLevelDto> userLevel = deviceService.getDeviceUserLevel();
            result.put("userLevel", userLevel);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Device User Level");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    //Get vtu Vendor DropDown

    @PostMapping("/getVtuVendorDropDown")
    public RDVTSResponse getVtuVendorDropDown() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VTUVendorMasterDto> vtuVendor = deviceService.getVtuVendorDropDown();
            result.put("vtuVendor", vtuVendor);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Vtu Vendor List");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    //Delete Device

    @PostMapping("/deactivateDevice")
    public RDVTSResponse deactivateDevice(@RequestParam Integer deviceId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Boolean res = deviceService.deactivateDevice(deviceId);
            Boolean res1 = deviceService.deactivateDeviceArea(deviceId);
            Boolean res2 = deviceService.deactivateDeviceVehicle(deviceId);
            if (res==true && res1==true && res2==true) {
                response.setData(result);
                response.setStatus(1);
                response.setMessage("Device Deactivated ");
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
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
