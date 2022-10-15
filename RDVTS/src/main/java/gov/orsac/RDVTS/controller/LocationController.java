package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
    private WorkService workService;

    @Autowired
    private RoadService roadService;

    @PostMapping("/getLatestLocationRecord")
    public RDVTSResponse getLatestLocationRecord(@RequestParam(name = "deviceId", required = false) Integer deviceId,
                                                 @RequestParam(name = "vehicleId", required = false) Integer vehicleId,
                                                 @RequestParam(name = "userId",required = false)Integer userId,
                                                    @RequestParam(name = "workId",required = false)Integer workId,
                                                 @RequestParam(name = "roadId",required = false)Integer roadId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        //Get All Device Ids
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> deviceIds = new ArrayList<>();
        List<Integer> workIds = new ArrayList<>();
        List<Integer> roadIds  =new ArrayList<>();
        List<Long> imei1 = new ArrayList<>();
        List<Long> imei2 = new ArrayList<>();

        try {
            //If device Id is present
            if (deviceId !=null){
                deviceIds.add(deviceId);
            } else if (vehicleId !=null) {
                VehicleDeviceMappingDto device=vehicleService.getVehicleDeviceMapping(vehicleId);
                deviceIds.add(device.getDeviceId());
            } else if (workId !=null) {
                workIds.add(workId);


                List<VehicleWorkMappingDto> vehicleByWork= workService.getVehicleBywork(workIds);
                for (VehicleWorkMappingDto item : vehicleByWork)
                {
                    vehicleIds.add(item.getVehicleId());
                }
                List<VehicleDeviceMappingDto> device=vehicleService.getVehicleDeviceMappingList(vehicleIds);
                for (VehicleDeviceMappingDto item : device)
                {
                    deviceIds.add(item.getDeviceId());
                }
            }

            else if (roadId !=null) {
                roadIds.add(roadId);
                List<GeoMasterDto> workByRoad= roadService.getWorkByroadIds(roadIds);

                for (GeoMasterDto item : workByRoad) {
                    workIds.add(item.getWorkId());

                }

                List<VehicleWorkMappingDto> vehicleByWork= workService.getVehicleBywork(workIds);
                for (VehicleWorkMappingDto item : vehicleByWork)
                {
                    vehicleIds.add(item.getVehicleId());
                }
                List<VehicleDeviceMappingDto> device=vehicleService.getVehicleDeviceMappingList(vehicleIds);
                for (VehicleDeviceMappingDto item : device)
                {
                    deviceIds.add(item.getDeviceId());
                }
            }


            // Get Final IMEI Number
            List<DeviceDto> device = deviceService.getDeviceByIds(deviceIds,userId);

            for (DeviceDto item : device)
            {
                if(item.getImeiNo1()!=null || !item.getImeiNo1().toString().isEmpty()){
                    imei1.add(item.getImeiNo1());
                    imei2.add(item.getImeiNo1());
                }
            }


            //Get and Send Final Data
            if(imei1.size()>0){
                List<VtuLocationDto> vtuLocationDto = locationService.getLatestRecordByImeiNumber(imei1,imei2);

                //result.put("user", vtuLocationDto);
                response.setData(vtuLocationDto);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("Device Latest Location");
            } else {
                    response = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                            "Device Id or Vehicle Id is empty",
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
        //Get All Device Ids
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> deviceIds = new ArrayList<>();
        List<Integer> workIds = new ArrayList<>();
        List<Long> imei1 = new ArrayList<>();
        List<Long> imei2 = new ArrayList<>();
        try {
            if (deviceId !=null){
                deviceIds.add(deviceId);
            } else if (vehicleId !=null) {
                VehicleDeviceMappingDto device=vehicleService.getVehicleDeviceMapping(vehicleId);
                deviceIds.add(device.getDeviceId());
            } else if (workId !=null) {
                workIds.add(workId);


                List<VehicleWorkMappingDto> vehicleByWork= workService.getVehicleBywork(workIds);
                for (VehicleWorkMappingDto item : vehicleByWork)
                {
                    vehicleIds.add(item.getVehicleId());
                }
                List<VehicleDeviceMappingDto> device=vehicleService.getVehicleDeviceMappingList(vehicleIds);
                for (VehicleDeviceMappingDto item : device)
                {
                    deviceIds.add(item.getDeviceId());
                }
            }


            // Get Final IMEI Number
            List<DeviceDto> device = deviceService.getDeviceByIds(deviceIds,userId);

            for (DeviceDto item : device)
            {
                if(item.getImeiNo1()!=null || !item.getImeiNo1().toString().isEmpty()){
                    imei1.add(item.getImeiNo1());
                    imei2.add(item.getImeiNo2());
                }
            }


            //Get and Send Final Data
            if(imei1.size()>0){


                //List<VtuLocationDto> vtuLocationDto = locationService.getLatestRecordByImeiNumber(imei1,imei2);

                if (startTime==null && endTime==null){
                    response = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                            "Start date and End Date is not Found",
                            result);
                }
                else {
                    Date startDate=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
                    Date endDate=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);

                    List<VtuLocationDto> vtuLocationDtoList=locationService.getLocationRecordByDateTime(imei1,imei2,startDate,endDate);

                    //result.put("user", vtuLocationDto);
                    response.setData(vtuLocationDtoList);
                    response.setStatus(1);
                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    response.setMessage("Device Latest Location");
                }

                //result.put("user", vtuLocationDto);

            } else {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        "Device Id or Vehicle Id is empty",
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
