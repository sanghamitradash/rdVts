package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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




    @PostMapping("/getLocationRecordList")
    public RDVTSResponse getLocationRecordList(@RequestParam(name = "userId", required = false) Integer userId,
                                               @RequestParam(name = "imei1", required = false) List<Long> imei1,
                                               @RequestParam(name = "imei2", required = false) List<Long> imei2,
                                               @RequestParam(name = "deviceId", required = false) List<Integer> deviceId,
                                               @RequestParam(name = "vehicleId", required = false) List<Integer> vehicleId,
                                               @RequestParam(name = "workId", required = false) List<Integer> workId,
                                               @RequestParam(name = "roadId", required = false) List<Integer> roadId,
                                               @RequestParam(name = "contractorId", required = false) List<Integer> contractorId,
                                               @RequestParam(name = "startTime", required = false) String startTime,
                                               @RequestParam(name = "endTime", required = false) String endTime) throws ParseException {

        RDVTSResponse response = new RDVTSResponse();
        List<Map<String, Object>> result = new ArrayList<>();
//        List<>
        Map<Integer, Object> device = new HashMap<>();
        //Get All Device Ids
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> deviceIds = new ArrayList<>();
        List<Date> deviceCreatedDate = new ArrayList<>();
        List<Date> deviceDeactivationDate = new ArrayList<>();

        List<Long> imeiArray1 = new ArrayList<>();
        List<Long> imeiArray2 = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        Date vehicleStartDate = null;
        Date vehicleendDate = null;
        if (startTime != null && endTime != null) {
            startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);
        }

        try {

//
            if (deviceId != null) {
                for (Integer deviceid : deviceId) {
                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceid);

                    Date createdOn = null;
                    Date deactivationDate = null;
                    for (DeviceDto imei : getImeiList) {
                        List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, createdOn, deactivationDate);

                        for (VtuLocationDto vtuobj : vtuLocationDto) {
                            vtuobj.setDeviceId(imei.getId());

                        }
                        Map<String, Object> itemVal = new HashMap<>();
                        itemVal.put("imeiNo", imei.getImeiNo1());
                        itemVal.put("vehicleLocation", vtuLocationDto);
                        result.add(itemVal);
                    }


                }




            } else if (vehicleId != null) {
                for (Integer vehicleitem : vehicleId) {
                    List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleitem, vehicleStartDate, vehicleendDate);

                    for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                        List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                        //int i = 0;
                        for (DeviceDto imei : getImeiList) {
                            List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
//                            i++;
                            for (VtuLocationDto vtuobj : vtuLocationDto) {
                                vtuobj.setDeviceId(imei.getId());
                                vtuobj.setVehicleId(vehicleid.getVehicleId());

                            }
                            Map<String, Object> itemVal = new HashMap<>();
                            itemVal.put("imeiNo", imei.getImeiNo1());
                            itemVal.put("vehicleLocation", vtuLocationDto);
                            result.add(itemVal);
                        }
                        // deviceIds.add(vehicleid.getDeviceId());

                    }

                }
//                for (Integer deviceid : deviceIds) {
//                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceid);
//                    for (DeviceDto imei : getImeiList) {
//                        imeiArray1.add(imei.getImeiNo1());
//                        imeiArray2.add(imei.getImeiNo2());
//                    }
//
//                    device.put(deviceid, getImeiList);
//                }
//
//                int i = 0;
//                for (Long item : imeiArray1) {
//                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(item, imeiArray2.get(i), startDate, endDate);
//                    i++;
//                    Map<String, Object> itemVal = new HashMap<>();
//                    itemVal.put("imeiNo", item);
//                    itemVal.put("vehicleLocation", vtuLocationDto);
//                    result.add(itemVal);
//
//                }


            } else if (workId != null) {

                for (Integer workitem : workId) {
                    List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByRoadId(workitem);
                    for (VehicleWorkMappingDto workIditem : vehicleIdList) {

                        List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(workIditem.getVehicleId(), vehicleStartDate, vehicleendDate);
                        for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                            List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                            //int i = 0;
                            for (DeviceDto imei : getImeiList) {
                                List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                // i++;
                                for (VtuLocationDto vtuobj : vtuLocationDto) {
                                    vtuobj.setDeviceId(imei.getId());
                                    vtuobj.setVehicleId(vehicleid.getVehicleId());
                                    vtuobj.setWorkId(workIditem.getWorkId());
                                }
                                Map<String, Object> itemVal = new HashMap<>();
                                itemVal.put("imeiNo", imei.getImeiNo1());
                                itemVal.put("vehicleLocation", vtuLocationDto);
                                result.add(itemVal);
                            }
                            // deviceIds.add(vehicleid.getDeviceId());

                        }

                    }
                }

            } else if (roadId != null) {
                for (Integer roadid : roadId) {
                    List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadid);

                    for (GeoMasterDto item : workByRoad) {
                        // workIds.add(item.getWorkId());
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByRoadId(item.getWorkId());

                        for (VehicleWorkMappingDto workIditem : vehicleIdList) {
                            //    Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
//                        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);
                            if (workIditem.getStartTime() != null && workIditem.getEndTime() != null) {
//                                vehicleStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(workIditem.getCreatedOn());
//                                vehicleendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(workIditem.getDeactivationDate());
                                vehicleStartDate = workIditem.getCreatedOn();
                                vehicleendDate = workIditem.getDeactivationDate();
                            }

                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(workIditem.getVehicleId(), vehicleStartDate, vehicleendDate);
                            for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                //int i = 0;
                                for (DeviceDto imei : getImeiList) {
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                    // i++;
                                    for (VtuLocationDto vtuobj : vtuLocationDto) {
                                        vtuobj.setDeviceId(imei.getId());
                                        vtuobj.setVehicleId(vehicleid.getVehicleId());
                                        vtuobj.setWorkId(workIditem.getWorkId());
                                        vtuobj.setRoadId(item.getRoadId());

                                    }
                                    Map<String, Object> itemVal = new HashMap<>();
                                    itemVal.put("imeiNo", imei.getImeiNo1());
                                    itemVal.put("vehicleLocation", vtuLocationDto);
                                    result.add(itemVal);
                                }
                                // deviceIds.add(vehicleid.getDeviceId());

                            }

                        }
                    }

                }


            } else {
//

                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("No Imei Found");
            }


            response.setData(result);
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


    @PostMapping("/getLastLocationRecord")
    public RDVTSResponse getLastLocationRecord(@RequestParam(name = "userId", required = false) Integer userId,
                                               @RequestParam(name = "imei1", required = false) List<Long> imei1,
                                               @RequestParam(name = "imei2", required = false) List<Long> imei2,
                                               @RequestParam(name = "deviceId", required = false) List<Integer> deviceId,
                                               @RequestParam(name = "vehicleId", required = false) List<Integer> vehicleId,
                                               @RequestParam(name = "workId", required = false) List<Integer> workId,
                                               @RequestParam(name = "roadId", required = false) List<Integer> roadId,
                                               @RequestParam(name = "contractorId", required = false) List<Integer> contractorId,
                                               @RequestParam(name = "startTime", required = false) String startTime,
                                               @RequestParam(name = "endTime", required = false) String endTime) throws ParseException {

        RDVTSResponse response = new RDVTSResponse();
        List<Map<String, Object>> result = new ArrayList<>();
//        List<>
        Map<Integer, Object> device = new HashMap<>();
        //Get All Device Ids
        List<Integer> vehicleIds = new ArrayList<>();
        List<Integer> deviceIds = new ArrayList<>();
        List<Date> deviceCreatedDate = new ArrayList<>();
        List<Date> deviceDeactivationDate = new ArrayList<>();

        List<Long> imeiArray1 = new ArrayList<>();
        List<Long> imeiArray2 = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        Date vehicleStartDate = null;
        Date vehicleendDate = null;
        if (startTime != null && endTime != null) {
            startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
            endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);
        }

        try {

//
            if (deviceId != null) {
                for (Integer deviceid : deviceId) {
                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceid);

                    Date createdOn = null;
                    Date deactivationDate = null;
                    for (DeviceDto imei : getImeiList) {
                        List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, createdOn, deactivationDate);

                        for (VtuLocationDto vtuobj : vtuLocationDto) {
                            vtuobj.setDeviceId(imei.getId());

                        }
                        Map<String, Object> itemVal = new HashMap<>();
                        itemVal.put("imeiNo", imei.getImeiNo1());
                        itemVal.put("vehicleLocation", vtuLocationDto);
                        result.add(itemVal);
                    }


                }




            } else if (vehicleId != null) {
                for (Integer vehicleitem : vehicleId) {
                    List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleitem, vehicleStartDate, vehicleendDate);

                    for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                        List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                        //int i = 0;
                        for (DeviceDto imei : getImeiList) {
                            List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
//                            i++;
                            for (VtuLocationDto vtuobj : vtuLocationDto) {
                                vtuobj.setDeviceId(imei.getId());
                                vtuobj.setVehicleId(vehicleid.getVehicleId());

                            }
                            Map<String, Object> itemVal = new HashMap<>();
                            itemVal.put("imeiNo", imei.getImeiNo1());
                            itemVal.put("vehicleLocation", vtuLocationDto);
                            result.add(itemVal);
                        }
                        // deviceIds.add(vehicleid.getDeviceId());

                    }

                }
//                for (Integer deviceid : deviceIds) {
//                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceid);
//                    for (DeviceDto imei : getImeiList) {
//                        imeiArray1.add(imei.getImeiNo1());
//                        imeiArray2.add(imei.getImeiNo2());
//                    }
//
//                    device.put(deviceid, getImeiList);
//                }
//
//                int i = 0;
//                for (Long item : imeiArray1) {
//                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(item, imeiArray2.get(i), startDate, endDate);
//                    i++;
//                    Map<String, Object> itemVal = new HashMap<>();
//                    itemVal.put("imeiNo", item);
//                    itemVal.put("vehicleLocation", vtuLocationDto);
//                    result.add(itemVal);
//
//                }


            }
            else if (workId != null) {
                for (Integer workitem : workId) {
                    List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByRoadId(workitem);
                    for (VehicleWorkMappingDto workIditem : vehicleIdList) {

                        List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(workIditem.getVehicleId(), vehicleStartDate, vehicleendDate);
                        for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                            List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                            //int i = 0;
                            for (DeviceDto imei : getImeiList) {

                                List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                // i++;
                                for (VtuLocationDto vtuobj : vtuLocationDto) {
                                    vtuobj.setDeviceId(imei.getId());
                                    vtuobj.setVehicleId(vehicleid.getVehicleId());
                                    vtuobj.setWorkId(workIditem.getWorkId());
                                }
                                Map<String, Object> itemVal = new HashMap<>();
                                itemVal.put("imeiNo", imei.getImeiNo1());
                                itemVal.put("vehicleLocation", vtuLocationDto);
                                result.add(itemVal);
                            }
                            // deviceIds.add(vehicleid.getDeviceId());

                        }

                    }
                }
            } else if (roadId != null) {
                for (Integer roadid : roadId) {
                    List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadid);

                    for (GeoMasterDto item : workByRoad) {
                        // workIds.add(item.getWorkId());
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByRoadId(item.getWorkId());

                        for (VehicleWorkMappingDto workIditem : vehicleIdList) {
                            //    Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
//                        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);
                            if (workIditem.getStartTime() != null && workIditem.getEndTime() != null) {
//                                vehicleStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(workIditem.getCreatedOn());
//                                vehicleendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(workIditem.getDeactivationDate());
                                vehicleStartDate = workIditem.getCreatedOn();
                                vehicleendDate = workIditem.getDeactivationDate();
                            }

                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(workIditem.getVehicleId(), vehicleStartDate, vehicleendDate);
                            for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                //int i = 0;
                                for (DeviceDto imei : getImeiList) {
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                    // i++;
                                    for (VtuLocationDto vtuobj : vtuLocationDto) {
                                        vtuobj.setDeviceId(imei.getId());
                                        vtuobj.setVehicleId(vehicleid.getVehicleId());
                                        vtuobj.setWorkId(workIditem.getWorkId());
                                        vtuobj.setRoadId(item.getRoadId());

                                    }
                                    Map<String, Object> itemVal = new HashMap<>();
                                    itemVal.put("imeiNo", imei.getImeiNo1());
                                    itemVal.put("vehicleLocation", vtuLocationDto);
                                    result.add(itemVal);
                                }
                                // deviceIds.add(vehicleid.getDeviceId());

                            }

                        }
                    }

                }


            } else {
//

                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("No Imei Found");
            }


            response.setData(result);
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
