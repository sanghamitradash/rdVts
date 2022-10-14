package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.service.DeviceService;
import gov.orsac.RDVTS.service.LocationService;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                                 @RequestParam(name = "vehicleId", required = false) Integer vehicleId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            if (deviceId !=null){
                List<DeviceDto> device = deviceService.getDeviceById(deviceId);
                if(device.size()>0){
                    if (device.get(0).getImeiNo1()!=null || device.get(0).getImeiNo2()!=null){
                        VtuLocationDto vtuLocationDto = locationService.getLatestRecordByImeiNumber(device);

                        result.put("user", vtuLocationDto);
                        response.setData(result);
                        response.setStatus(1);
                        response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                        response.setMessage("Device Latest Location");
                    }
                }
                else {
                    response = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                            "Device Id or Vechile Id is empty",
                            result);
                }

            } else if (vehicleId !=null) {

                VehicleDeviceMappingDto device=vehicleService.getVehicleDeviceMapping(vehicleId);

                //VtuLocationDto vtuLocationDto = locationService.getLatestRecordByImeiNumber(device);

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



}
