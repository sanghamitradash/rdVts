package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.service.*;
import io.swagger.models.auth.In;
import javassist.runtime.Inner;
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

//    @PostMapping("/getLatestLocationRecord")
//    public RDVTSResponse getLatestLocationRecord(@RequestParam(name = "deviceId", required = false) Integer deviceId,
//                                                 @RequestParam(name = "vehicleId", required = false) Integer vehicleId,
//                                                 @RequestParam(name = "userId", required = false) Integer userId,
//                                                 @RequestParam(name = "workId", required = false) Integer workId,
//                                                 @RequestParam(name = "roadId", required = false) Integer roadId) {
//
//        RDVTSResponse response = new RDVTSResponse();
//        Map<String, Object> result = new HashMap<>();
//        //Get All Device Ids
//        List<Integer> vehicleIds = new ArrayList<>();
//        List<Integer> deviceIds = new ArrayList<>();
//        List<Integer> workIds = new ArrayList<>();
//        List<Integer> roadIds = new ArrayList<>();
//        List<Long> imei1 = new ArrayList<>();
//        List<Long> imei2 = new ArrayList<>();
//
//        try {
//            //If device Id is present
//            if (deviceId != null) {
//                deviceIds.add(deviceId);
//            } else if (vehicleId != null) {
//                //Get Data By Vehicle Ids
//                VehicleDeviceInfo device = vehicleService.getVehicleDeviceMapping(vehicleId);
//                deviceIds.add(device.getDeviceId());
//            } else if (workId != null) {
//                //Get Data by Work ids
//                workIds.add(workId);
//                List<VehicleWorkMappingDto> vehicleByWork = workService.getVehicleBywork(workIds);
//                for (VehicleWorkMappingDto item : vehicleByWork) {
//                    vehicleIds.add(item.getVehicleId());
//                }
//                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds);
//                for (VehicleDeviceMappingDto item : device) {
//                    deviceIds.add(item.getDeviceId());
//                }
//            } else if (roadId != null) {
//                //Get Data by Work Ids
//                roadIds.add(roadId);
//                List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadIds);
//                for (GeoMasterDto item : workByRoad) {
//                    workIds.add(item.getWorkId());
//                }
//                List<VehicleWorkMappingDto> vehicleByWork = workService.getVehicleBywork(workIds);
//                for (VehicleWorkMappingDto item : vehicleByWork) {
//                    vehicleIds.add(item.getVehicleId());
//                }
//                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds);
//                for (VehicleDeviceMappingDto item : device) {
//                    deviceIds.add(item.getDeviceId());
//                }
//            }
//
//            if (deviceIds == null) {
//                response = new RDVTSResponse(0,
//                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                        "Device Id or Vehicle Id is empty",
//                        result);
//            } else {
//                List<DeviceDto> device = deviceService.getDeviceByIds(deviceIds, userId);
//
//                for (DeviceDto item : device) {
//                    if (item.getImeiNo1() != null || !item.getImeiNo1().toString().isEmpty()) {
//                        imei1.add(item.getImeiNo1());
//                        imei2.add(item.getImeiNo2());
//                    }
//                }
//
//
//                //Get and Send Final Data
//                if (imei1.size() > 0 || imei2.size() > 0) {
//                    List<VtuLocationDto> vtuLocationDto = locationService.getLatestRecordByImeiNumber(imei1, imei2);
//                    //result.put("user", vtuLocationDto);
//                    response.setData(vtuLocationDto);
//                    response.setStatus(1);
//                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
//                    response.setMessage("Device Latest Location");
//                } else {
//                    response = new RDVTSResponse(0,
//                            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                            "Device Id or Vehicle Id is empty",
//                            result);
//                }
//            }
//            // Get Final IMEI Number
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response = new RDVTSResponse(0,
//                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                    ex.getMessage(),
//                    result);
//        }
//        return response;
//    }
//
//
//    @PostMapping("/getLocationRecordByDateTime")
//    public RDVTSResponse getLocationRecordByDateTime(@RequestParam(name = "deviceId", required = false) Integer deviceId,
//                                                     @RequestParam(name = "vehicleId", required = false) Integer vehicleId,
//                                                     @RequestParam(name = "userId", required = false) Integer userId,
//                                                     @RequestParam(name = "startTime", required = false) String startTime,
//                                                     @RequestParam(name = "endTime", required = false) String endTime,
//                                                     @RequestParam(name = "WorkId", required = false) Integer workId,
//                                                     @RequestParam(name = "roadId", required = false) Integer roadId) {
//        RDVTSResponse response = new RDVTSResponse();
//        Map<String, Object> result = new HashMap<>();
//        //Get All Device Ids
//        List<Integer> vehicleIds = new ArrayList<>();
//        List<Integer> deviceIds = new ArrayList<>();
//        List<Integer> workIds = new ArrayList<>();
//        List<Integer> roadIds = new ArrayList<>();
//        List<Long> imei1 = new ArrayList<>();
//        List<Long> imei2 = new ArrayList<>();
//        try {
//            if (deviceId != null) {
//                deviceIds.add(deviceId);
//            } else if (vehicleId != null) {
//                VehicleDeviceInfo device = vehicleService.getVehicleDeviceMapping(vehicleId);
//                deviceIds.add(device.getDeviceId());
//            } else if (workId != null) {
//                workIds.add(workId);
//
//
//                List<VehicleWorkMappingDto> vehicleByWork = workService.getVehicleBywork(workIds);
//                for (VehicleWorkMappingDto item : vehicleByWork) {
//                    vehicleIds.add(item.getVehicleId());
//                }
//                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds);
//                for (VehicleDeviceMappingDto item : device) {
//                    deviceIds.add(item.getDeviceId());
//                }
//            } else if (roadId != null) {
//                roadIds.add(roadId);
//                List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadIds);
//
//                for (GeoMasterDto item : workByRoad) {
//                    workIds.add(item.getWorkId());
//
//                }
//
//                List<VehicleWorkMappingDto> vehicleByWork = workService.getVehicleBywork(workIds);
//                for (VehicleWorkMappingDto item : vehicleByWork) {
//                    vehicleIds.add(item.getVehicleId());
//                }
//                List<VehicleDeviceMappingDto> device = vehicleService.getVehicleDeviceMappingList(vehicleIds);
//                for (VehicleDeviceMappingDto item : device) {
//                    deviceIds.add(item.getDeviceId());
//                }
//            }
//
//            if (deviceIds == null) {
//                response = new RDVTSResponse(0,
//                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                        "Device Id or Vehicle Id is empty",
//                        result);
//            } else {
//                List<DeviceDto> device = deviceService.getDeviceByIds(deviceIds, userId);
//
//                for (DeviceDto item : device) {
//                    if (item.getImeiNo1() != null || !item.getImeiNo1().toString().isEmpty()) {
//                        imei1.add(item.getImeiNo1());
//                        imei2.add(item.getImeiNo2());
//                    }
//                }
//
//
//                //Get and Send Final Data
//                if (imei1.size() > 0) {
//
//
//                    //List<VtuLocationDto> vtuLocationDto = locationService.getLatestRecordByImeiNumber(imei1,imei2);
//
//                    if (startTime == null && endTime == null) {
//                        response = new RDVTSResponse(0,
//                                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                                "Start date and End Date is not Found",
//                                result);
//                    } else {
//                        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
//                        Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);
//
//                        List<VtuLocationDto> vtuLocationDtoList = locationService.getLocationRecordByDateTime(imei1, imei2, startDate, endDate);
//
//                        //result.put("user", vtuLocationDto);
//                        response.setData(vtuLocationDtoList);
//                        response.setStatus(1);
//                        response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
//                        response.setMessage("Device Latest Location");
//                    }
//
//                    //result.put("user", vtuLocationDto);
//
//                } else {
//                    response = new RDVTSResponse(0,
//                            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                            "Device Id or Vehicle Id is empty",
//                            result);
//                }
//            }
//            // Get Final IMEI Number
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response = new RDVTSResponse(0,
//                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                    ex.getMessage(),
//                    result);
//        }
//        return response;
//    }


    @PostMapping("/getLocationRecordList")
    public RDVTSResponse getLocationRecordList(@RequestParam(name = "userId", required = false) Integer userId,
                                               @RequestParam(name = "imei1", required = false) List<Long> imei1,
                                               @RequestParam(name = "imei2", required = false) List<Long> imei2,
                                               @RequestParam(name = "deviceId", required = false) List<Integer> deviceId,
                                               @RequestParam(name = "vehicleId", required = false) List<Integer> vehicleId,
                                               @RequestParam(name = "workId", required = false) List<Integer> workId,
                                               @RequestParam(name = "roadId", required = false) List<Integer> roadId,
                                               @RequestParam(name = "contractorId", required = false) List<Integer> contractorId,
                                               @RequestParam(name = "districtId", required = false) List<Integer> districtId,
                                               @RequestParam(name = "blockId", required = false) List<Integer> blockId,
                                               @RequestParam(name = "divisionId", required = false) List<Integer> divisionId,
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

                    for (VehicleDeviceMappingDto deviceObj : getdeviceList) {
                        List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceObj.getDeviceId());
                        //int i = 0;
                        for (DeviceDto imei : getImeiList) {
                            List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, deviceObj.getCreatedOn(), deviceObj.getDeactivationDate());
//                            i++;
                            for (VtuLocationDto vtuobj : vtuLocationDto) {
                                vtuobj.setDeviceId(imei.getId());
                                vtuobj.setVehicleId(deviceObj.getVehicleId());

                            }
                            Map<String, Object> itemVal = new HashMap<>();
                            itemVal.put("imeiNo", imei.getImeiNo1());
                            itemVal.put("vehicleLocation", vtuLocationDto);
                            result.add(itemVal);
                        }
                        // deviceIds.add(vehicleid.getDeviceId());

                    }

                }


            } else if (workId != null) {

                for (Integer workitem : workId) {
                    List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(workitem);
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
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(item.getWorkId());

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


            } else if (contractorId != null) {
                for (Integer contractorid : contractorId) {
                    List<GeoMasterDto> workByContractorIds = roadService.workByContractorIds(contractorid);
                    for (GeoMasterDto workByContractorId : workByContractorIds) {
                        List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(workByContractorId.getRoadId());
                        for (GeoMasterDto item : workByRoad) {
                            // workIds.add(item.getWorkId());
                            List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(item.getWorkId());

                            for (VehicleWorkMappingDto workIditem : vehicleIdList) {
                                //    Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
//                          Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);
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
                                            vtuobj.setContractorId(workByContractorId.getContractorId());

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


                }


            } else if (districtId != null) {
                for (Integer districtitem : districtId) {
                    List<GeoMasterDto> workByDistrictIds = roadService.getworkByDistrictId(districtitem);
                    for (GeoMasterDto WorkObj : workByDistrictIds) {
                        //for (Integer workitem : workId) {
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(WorkObj.getWorkId());
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
                        // }
                    }

                }


            } else if (blockId != null) {


                for (Integer blockObj : blockId) {
                    List<GeoMasterDto> workByBlockId = roadService.getworkByBlockId(blockObj);
                    for (GeoMasterDto  workItem: workByBlockId) {
                        //for (Integer workitem : workId) {
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(workItem.getWorkId());
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
                        // }
                    }

                }

            } else if (divisionId != null) {

                for (Integer divisionObj : divisionId) {
                    List<GeoMasterDto> workByDivisionId = roadService.getworkByDivisionId(divisionObj);
                    for (GeoMasterDto  workItem: workByDivisionId) {
                        //for (Integer workitem : workId) {
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(workItem.getWorkId());
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
                        // }
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
            response.setMessage("All Location Details");


        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getLastLocationRecordList")
    public RDVTSResponse getLastLocationRecordList(@RequestParam(name = "userId", required = false) Integer userId,
                                                   @RequestParam(name = "imei1", required = false) List<Long> imei1,
                                                   @RequestParam(name = "imei2", required = false) List<Long> imei2,
                                                   @RequestParam(name = "deviceId", required = false) List<Integer> deviceId,
                                                   @RequestParam(name = "vehicleId", required = false) List<Integer> vehicleId,
                                                   @RequestParam(name = "workId", required = false) List<Integer> workId,
                                                   @RequestParam(name = "roadId", required = false) List<Integer> roadId,
                                                   @RequestParam(name = "contractorId", required = false) List<Integer> contractorId,
                                                   @RequestParam(name = "districtId", required = false) List<Integer> districtId,
                                                   @RequestParam(name = "blockId", required = false) List<Integer> blockId,
                                                   @RequestParam(name = "divisionId", required = false) List<Integer> divisionId,
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

            if (deviceId != null) {
                for (Integer deviceid : deviceId) {
                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceid);

                    Date createdOn = null;
                    Date deactivationDate = null;
                    for (DeviceDto imei : getImeiList) {
                        List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, createdOn, deactivationDate);

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
                            List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
//                            i++;
                            for (VtuLocationDto vtuobj : vtuLocationDto) {
                                vtuobj.setDeviceId(imei.getId());
                                vtuobj.setVehicleId(vehicleid.getVehicleId());
                                vtuobj.setVehicleNo(vehicleid.getVehicleNo());

                            }
                            Map<String, Object> itemVal = new HashMap<>();
                            itemVal.put("imeiNo", imei.getImeiNo1());
                            itemVal.put("vehicleLocation", vtuLocationDto);
                            result.add(itemVal);
                        }
                        // deviceIds.add(vehicleid.getDeviceId());

                    }

                }

            } else if (workId != null) {

                for (Integer workitem : workId) {
                    List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(workitem);
                    for (VehicleWorkMappingDto workIditem : vehicleIdList) {

                        List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(workIditem.getVehicleId(), vehicleStartDate, vehicleendDate);
                        for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                            List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                            //int i = 0;
                            for (DeviceDto imei : getImeiList) {
                                List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
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
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(item.getWorkId());

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
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
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


            } else if (contractorId != null) {
                for (Integer contractorid : contractorId) {
                    List<GeoMasterDto> workByContractorIds = roadService.workByContractorIds(contractorid);

                    for (GeoMasterDto workByContractorId : workByContractorIds) {


                        List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(workByContractorId.getRoadId());

                        for (GeoMasterDto item : workByRoad) {
                            // workIds.add(item.getWorkId());
                            List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(item.getWorkId());

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
                                        List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                        // i++;
                                        for (VtuLocationDto vtuobj : vtuLocationDto) {
                                            vtuobj.setDeviceId(imei.getId());
                                            vtuobj.setVehicleId(vehicleid.getVehicleId());
                                            vtuobj.setWorkId(workIditem.getWorkId());
                                            vtuobj.setRoadId(item.getRoadId());
                                            vtuobj.setContractorId(workByContractorId.getContractorId());

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


                }


            }

            else if (districtId != null) {
                for (Integer districtitem : districtId) {
                    List<GeoMasterDto> workByDistrictIds = roadService.getworkByDistrictId(districtitem);
                    for (GeoMasterDto WorkObj : workByDistrictIds) {
                        //for (Integer workitem : workId) {
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(WorkObj.getWorkId());
                        for (VehicleWorkMappingDto workIditem : vehicleIdList) {

                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(workIditem.getVehicleId(), vehicleStartDate, vehicleendDate);
                            for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                //int i = 0;
                                for (DeviceDto imei : getImeiList) {
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
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
                        // }
                    }

                }


            } else if (blockId != null) {


                for (Integer blockObj : blockId) {
                    List<GeoMasterDto> workByBlockId = roadService.getworkByBlockId(blockObj);
                    for (GeoMasterDto  workItem: workByBlockId) {
                        //for (Integer workitem : workId) {
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(workItem.getWorkId());
                        for (VehicleWorkMappingDto workIditem : vehicleIdList) {

                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(workIditem.getVehicleId(), vehicleStartDate, vehicleendDate);
                            for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                //int i = 0;
                                for (DeviceDto imei : getImeiList) {
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
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
                        // }
                    }

                }

            } else if (divisionId != null) {

                for (Integer divisionObj : divisionId) {
                    List<GeoMasterDto> workByDivisionId = roadService.getworkByDivisionId(divisionObj);
                    for (GeoMasterDto  workItem: workByDivisionId) {
                        //for (Integer workitem : workId) {
                        List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(workItem.getWorkId());
                        for (VehicleWorkMappingDto workIditem : vehicleIdList) {

                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(workIditem.getVehicleId(), vehicleStartDate, vehicleendDate);
                            for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                //int i = 0;
                                for (DeviceDto imei : getImeiList) {
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
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
                        // }
                    }

                }

            }

            else {
//

                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("No Imei Found");
            }

            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("All Last Location Details");


        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getVehicleListByWorkId")
     public RDVTSResponse  getVehicleListByWorkId(@RequestParam(name = "workIds", required = false) List<Integer> workIds){
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<VehicleMasterDto> vehicleByWork = workService.getVehicleBywork(workIds);
//            result.put("road", road);
            response.setData(vehicleByWork);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Vehicle By workId");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;

    }






}

