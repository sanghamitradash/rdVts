package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.service.DeviceService;
import gov.orsac.RDVTS.service.LocationService;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/getLatestLocationRecord")
    public RDVTSResponse getLatestLocationRecord(@RequestParam(name = "deviceId", required = false) Integer deviceId,
                                                 @RequestParam(name = "vehicleId", required = false) Integer vehicleId,
                                                 @RequestParam(name = "userId",required = false)Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            if (deviceId !=null){

                List<DeviceDto> device = deviceService.getDeviceById(deviceId,userId);
                if(device.size()>0){
//                    if (device.get(0).getImeiNo1()!=null || device.get(0).getImeiNo2()!=null){
                        VtuLocationDto vtuLocationDto = locationService.getLatestRecordByImeiNumber(device);

                        //result.put("user", vtuLocationDto);
                        response.setData(vtuLocationDto);
                        response.setStatus(1);
                        response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                        response.setMessage("Device Latest Location");
                    }
//                }
//                else {
//                    response = new RDVTSResponse(0,
//                            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                            "Device Id or Vechile Id is empty",
//                            result);
//                }

            } else if (vehicleId !=null) {

                VehicleDeviceMappingDto device=vehicleService.getVehicleDeviceMapping(vehicleId);

                //device.getDeviceId();
                List<DeviceDto> deviceDtoList = deviceService.getDeviceById(device.getDeviceId(),userId);
                VtuLocationDto vtuLocationDto = locationService.getLatestRecordByImeiNumber(deviceDtoList);
                response.setData(vtuLocationDto);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("Device Latest Location");
            } else {

                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        "Device Id or Vechile Id is empty",
                        result);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getLocationRecordByDateTime")
    public RDVTSResponse getLocationRecordByDateTime(@RequestParam(name = "deviceId", required = false) Integer deviceId,
                                                 @RequestParam(name = "vehicleId", required = false) Integer vehicleId,
                                                 @RequestParam(name = "userId",required = false)Integer userId,
                                                 @RequestParam(name = "startTime", required = false) String startTime,
                                                 @RequestParam(name = "endTime", required = false) String endTime,
                                                     @RequestParam(name = "WorkId", required = false) Integer workId)

    {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            if (deviceId !=null){
                List<DeviceDto> device = deviceService.getDeviceById(deviceId,userId);
                if(device.size()>0){
                    if (device.get(0).getImeiNo1()!=null || device.get(0).getImeiNo2()!=null){

                        List<VehicleWorkMappingDto> work=vehicleService.getVehicleWorkMapping(vehicleId);
                        // VtuLocationDto vtuLocationDto = locationService.getLatestRecordByImeiNumber(device);
//                        SimpleDateFormat srData = SimpleDateFormat()
                        if (startTime==null && endTime==null){
                            response = new RDVTSResponse(0,
                                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                                    "Start date and End Date is not Found",
                                    result);
                        }
                        else {
                            Date startDate=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
                            Date endDate=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);

                            List<VtuLocationDto> vtuLocationDtoList=locationService.getLocationRecordByDateTime(device,startDate,endDate);

                            //result.put("user", vtuLocationDto);
                            response.setData(vtuLocationDtoList);
                            response.setStatus(1);
                            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                            response.setMessage("Location List");
                        }


                    }
                }
                else {
                    response = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                            "Device Not Found!!",
                            result);
                }

            } else if (vehicleId !=null) {

                VehicleDeviceMappingDto device=vehicleService.getVehicleDeviceMapping(vehicleId);

                //device.getDeviceId();
                List<DeviceDto> deviceDtoList = deviceService.getDeviceById(device.getDeviceId(),userId);
                VtuLocationDto vtuLocationDto = locationService.getLatestRecordByImeiNumber(deviceDtoList);
                response.setData(vtuLocationDto);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("Device Latest Location");
            }
            else if (workId !=null){

                List<VehicleWorkMappingDto> work=vehicleService.getVehicleWorkMapping(vehicleId);

                VehicleDeviceMappingDto device=vehicleService.getVehicleDeviceMapping(vehicleId);

                //device.getDeviceId();
                List<DeviceDto> deviceDtoList = deviceService.getDeviceById(device.getDeviceId(),userId);
                VtuLocationDto vtuLocationDto = locationService.getLatestRecordByImeiNumber(deviceDtoList);
                response.setData(vtuLocationDto);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("Device Latest Location");

            }
            else {

                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        "Device Id or Vechile Id Not Found!!",
                        result);

            }

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
