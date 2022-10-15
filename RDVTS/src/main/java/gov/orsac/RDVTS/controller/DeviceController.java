package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ContractorEntity;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.service.DeviceService;
import gov.orsac.RDVTS.service.VehicleService;
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
@RequestMapping("/api/v1/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private VehicleService vehicleService;

    //Add Device

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

                    if (deviceDto.getMobileNumber1().toString().length() == 10 && deviceDto.getMobileNumber2().toString().length() == 10) {
                        DeviceEntity deviceEntity = deviceService.addDevice(deviceDto);
                        DeviceMappingEntity deviceMapping = deviceService.saveDeviceMapping(deviceDto.getDeviceMapping(), deviceEntity.getId());
                        VehicleDeviceMappingEntity saveVehicleMapping = vehicleService.assignVehicleDevice(deviceDto.getVehicleDeviceMapping());
                        result.put("deviceEntity", deviceEntity);
                        result.put("deviceMapping", deviceMapping);
                        response.setData(result);
                        response.setStatus(1);
                        response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                        response.setMessage(" Device Added Successfully");
                    }
                    else {
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

            } catch (Exception ex) {
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
            result.put("device", device);
            result.put("deviceArea",deviceArea);
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

    @PostMapping("/getDeviceList")
    public RDVTSListResponse getDeviceList(@RequestParam(name = "imeiNo1",required = false) Long imeiNo1,
                                           @RequestParam(name = "simIccId1",required = false) Long simIccId1,
                                           @RequestParam(name = "mobileNumber1",required = false)Long mobileNumber1,
                                           @RequestParam(name = "imeiNo2",required = false) Long imeiNo2,
                                           @RequestParam(name = "simIccId2",required = false)Long simIccId2,
                                           @RequestParam(name = "mobileNumber2",required = false) Long mobileNumber2,
                                           @RequestParam(name = "vtuVendorId",required = false) Integer vtuVendorId,
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
        device.setDistId(distId);
        device.setBlockId(blockId);
        device.setGDistId(gDistId);
        device.setGBlockId(gBlockId);
        device.setDivisionId(divisionId);
        device.setUserId(userId);
        device.setLimit(length);
        device.setOffSet(start);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            Page<DeviceInfo> deviceListPage = deviceService.getDeviceList(device);
            List<DeviceInfo> deviceList = deviceListPage.getContent();
            //result.put("deviceList", deviceList);
            response.setData(deviceList);
            response.setMessage("Vehicle List");
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
            DeviceMappingEntity deviceMapping = deviceService.saveDeviceMapping(deviceDto.getDeviceMapping(),updateDevice.getId());
            result.put("updateDevice", updateDevice);
            result.put("deviceMapping", deviceMapping);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Device Updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getUnassignedDeviceData")
    public RDVTSResponse getUnassignedDeviceData(@RequestParam(name = "userId", required = false) Integer userId) {
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



}
