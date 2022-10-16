package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.service.*;
import org.springframework.beans.BeanUtils;
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
                                                 @RequestParam(name = "userId", required = false) Integer userId,
                                                 @RequestParam(name = "workId", required = false) Integer workId,
                                                 @RequestParam(name = "roadId", required = false) Integer roadId) {

        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        //Get All Device Ids
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> deviceIds = new ArrayList<>();
        List<Integer> workIds = new ArrayList<>();
        List<Integer> roadIds = new ArrayList<>();
        List<Long> imei1 = new ArrayList<>();
        List<Long> imei2 = new ArrayList<>();

        try {
            //If device Id is present
            if (deviceId != null) {
                deviceIds.add(deviceId);
            } else if (vehicleId != null) {
                //Get Data By Vehicle Ids
                VehicleDeviceInfo device = vehicleService.getVehicleDeviceMapping(vehicleId);
                deviceIds.add(device.getDeviceId());
            } else if (workId != null) {
                //Get Data by Work ids
                workIds.add(workId);
                List<VehicleWorkMappingDto> vehicleByWork = workService.getVehicleBywork(workIds);
                for (VehicleWorkMappingDto item : vehicleByWork) {
                    vehicleIds.add(item.getVehicleId());
                }
                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds);
                for (VehicleDeviceMappingDto item : device) {
                    deviceIds.add(item.getDeviceId());
                }
            } else if (roadId != null) {
                //Get Data by Work Ids
                roadIds.add(roadId);
                List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadIds);
                for (GeoMasterDto item : workByRoad) {
                    workIds.add(item.getWorkId());
                }
                List<VehicleWorkMappingDto> vehicleByWork = workService.getVehicleBywork(workIds);
                for (VehicleWorkMappingDto item : vehicleByWork) {
                    vehicleIds.add(item.getVehicleId());
                }
                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds);
                for (VehicleDeviceMappingDto item : device) {
                    deviceIds.add(item.getDeviceId());
                }
            }

            if (deviceIds == null) {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        "Device Id or Vehicle Id is empty",
                        result);
            } else {
                List<DeviceDto> device = deviceService.getDeviceByIds(deviceIds, userId);

                for (DeviceDto item : device) {
                    if (item.getImeiNo1() != null || !item.getImeiNo1().toString().isEmpty()) {
                        imei1.add(item.getImeiNo1());
                        imei2.add(item.getImeiNo2());
                    }
                }


                //Get and Send Final Data
                if (imei1.size() > 0 || imei2.size() > 0) {
                    List<VtuLocationDto> vtuLocationDto = locationService.getLatestRecordByImeiNumber(imei1, imei2);
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
            }
            // Get Final IMEI Number


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
                                                     @RequestParam(name = "userId", required = false) Integer userId,
                                                     @RequestParam(name = "startTime", required = false) String startTime,
                                                     @RequestParam(name = "endTime", required = false) String endTime,
                                                     @RequestParam(name = "WorkId", required = false) Integer workId,
                                                     @RequestParam(name = "roadId", required = false) Integer roadId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        //Get All Device Ids
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> deviceIds = new ArrayList<>();
        List<Integer> workIds = new ArrayList<>();
        List<Integer> roadIds = new ArrayList<>();
        List<Long> imei1 = new ArrayList<>();
        List<Long> imei2 = new ArrayList<>();
        try {
            if (deviceId != null) {
                deviceIds.add(deviceId);
            } else if (vehicleId != null) {
                VehicleDeviceInfo device = vehicleService.getVehicleDeviceMapping(vehicleId);
                deviceIds.add(device.getDeviceId());
            } else if (workId != null) {
                workIds.add(workId);


                List<VehicleWorkMappingDto> vehicleByWork = workService.getVehicleBywork(workIds);
                for (VehicleWorkMappingDto item : vehicleByWork) {
                    vehicleIds.add(item.getVehicleId());
                }
                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds);
                for (VehicleDeviceMappingDto item : device) {
                    deviceIds.add(item.getDeviceId());
                }
            } else if (roadId != null) {
                roadIds.add(roadId);
                List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadIds);

                for (GeoMasterDto item : workByRoad) {
                    workIds.add(item.getWorkId());

                }

                List<VehicleWorkMappingDto> vehicleByWork = workService.getVehicleBywork(workIds);
                for (VehicleWorkMappingDto item : vehicleByWork) {
                    vehicleIds.add(item.getVehicleId());
                }
                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds);
                for (VehicleDeviceMappingDto item : device) {
                    deviceIds.add(item.getDeviceId());
                }
            }

            if (deviceIds == null) {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        "Device Id or Vehicle Id is empty",
                        result);
            } else {
                List<DeviceDto> device = deviceService.getDeviceByIds(deviceIds, userId);

                for (DeviceDto item : device) {
                    if (item.getImeiNo1() != null || !item.getImeiNo1().toString().isEmpty()) {
                        imei1.add(item.getImeiNo1());
                        imei2.add(item.getImeiNo2());
                    }
                }


                //Get and Send Final Data
                if (imei1.size() > 0) {


                    //List<VtuLocationDto> vtuLocationDto = locationService.getLatestRecordByImeiNumber(imei1,imei2);

                    if (startTime == null && endTime == null) {
                        response = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                                "Start date and End Date is not Found",
                                result);
                    } else {
                        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
                        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);

                        List<VtuLocationDto> vtuLocationDtoList = locationService.getLocationRecordByDateTime(imei1, imei2, startDate, endDate);

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
            }
            // Get Final IMEI Number


        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }



    @PostMapping("/getLocationRecordList")
    public RDVTSResponse getLocationRecordList(@RequestParam(name = "imei1", required = false) List<Long> imei1,
                                                 @RequestParam(name = "imei2", required = false)  List<Long> imei2,
                                                 @RequestParam(name = "deviceId", required = false)  List<Integer> deviceId,
                                                @RequestParam(name = "vehicleId", required = false)  Integer vehicleId) {

        RDVTSResponse response = new RDVTSResponse();
        Map<Long, Object> result = new HashMap<>();
        Map<Integer, Object> device = new HashMap<>();
        //Get All Device Ids
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> deviceIds = new ArrayList<>();
        List<Integer> workIds = new ArrayList<>();
        List<Integer> roadIds = new ArrayList<>();
        List<Long> imeiArray1 = new ArrayList<>();
        List<Long> imeiArray2 = new ArrayList<>();

        try {
//            if(imei1.size()>0  && imei2.size()==0){
//                int i=0;
//                for (Long item: imei1) {
//
//                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(item,imei2.get(i));
//                    i++;
//                }
//
//            }
//            if(imei2.size()>0  && imei1.size()==0){
//                for (Long item: imei2) {
//                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(,item);
//                }
//
//            }
            if (deviceId.size()>0){
                for (Integer deviceid: deviceId) {
                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceid);
                    for (DeviceDto imei  :getImeiList) {
                        imeiArray1.add(imei.getImeiNo1());
                        imeiArray2.add(imei.getImeiNo2());
                    }

                    device.put(deviceid,getImeiList);
                }

                int i=0;
                for (Long item: imeiArray1) {
                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(item,imeiArray2.get(i));
                    i++;
                    result.put(item,vtuLocationDto);
                }



            }
//            else if (vehicleId!=null) {
//
//                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds)
//
//            }
            else {
                int i=0;
                for (Long item: imei1) {
                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(item,imei2.get(i));
                    i++;
                    result.put(item,vtuLocationDto);
                }
            }





            response.setData(result);
           // response.setData(device);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("All Location By Imei.");


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
