package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ContractorEntity;
import gov.orsac.RDVTS.entities.DeviceEntity;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import gov.orsac.RDVTS.service.DeviceService;
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
                        List<DeviceMappingEntity> deviceMapping = deviceService.saveDeviceMapping(deviceDto.getDeviceMapping(), deviceEntity.getId());
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
    public RDVTSResponse getDeviceById(@RequestParam(name = "deviceId", required = false) Integer deviceId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            DeviceDto device = deviceService.getDeviceById(deviceId);
            List<DeviceAreaMappingDto> deviceArea = deviceService.getDeviceAreaByDeviceId(deviceId);
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
    public RDVTSResponse getDeviceList(@RequestBody DeviceListDto deviceDto) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            Page<DeviceDto> deviceListPage = deviceService.getDeviceList(deviceDto);
            List<DeviceDto> deviceList = deviceListPage.getContent();
            if (!deviceList.isEmpty() && deviceList.size() > 0) {
                result.put("deviceList", deviceList);
                result.put("currentPage", deviceListPage.getNumber());
                result.put("totalItems", deviceListPage.getTotalElements());
                result.put("totalPages", deviceListPage.getTotalPages());
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("deviceList", deviceList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
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
            List<DeviceMappingEntity> deviceMapping = deviceService.saveDeviceMapping(deviceDto.getDeviceMapping(),updateDevice.getId());
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

}
