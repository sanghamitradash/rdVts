package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ActivityWorkMapping;
import gov.orsac.RDVTS.entities.GeoMappingEntity;
import gov.orsac.RDVTS.service.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
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
    public RDVTSResponse getLocationRecordList(@RequestParam(name = "userId") Integer userId,
                                               @RequestParam(name = "imei1", required = false) List<Long> imei1,
                                               @RequestParam(name = "imei2", required = false) List<Long> imei2,
                                               @RequestParam(name = "deviceId", required = false) List<Integer> deviceId,
                                               @RequestParam(name = "vehicleId", required = false) List<Integer> vehicleId,
                                               @RequestParam(name = "activityIds", required = false) List<Integer> activityIds,
                                               @RequestParam(name = "workId", required = false) List<Integer> workId,
                                               @RequestParam(name = "roadId", required = false) List<Integer> roadId,
                                               @RequestParam(name = "contractorId", required = false) List<Integer> contractorId,
                                               @RequestParam(name = "districtId", required = false) List<Integer> districtId,
                                               @RequestParam(name = "blockId", required = false) List<Integer> blockId,
                                               @RequestParam(name = "divisionId", required = false) List<Integer> divisionId,
                                               @RequestParam(name = "startTime", required = false) String startTime,
                                               @RequestParam(name = "endTime", required = false) String endTime) throws ParseException {
        RDVTSResponse response = new RDVTSResponse();
        if (userId != null) {
            List<Map<String, Object>> result = new ArrayList<>();
//        List<>
            // Map<String, Object> result = new HashMap<>();
            Date startDate = null;
            Date endDate = null;
            Date vehicleStartDate = null;
            Date vehicleendDate = null;
            if (startTime != null && endTime != null) {
                startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
                endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
            }

            try {


                if (deviceId != null && !deviceId.isEmpty()) {
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


                }
                else if (vehicleId != null && !vehicleId.isEmpty()) {
                    if (locationService.getLocationExistOrNot(vehicleId,startDate,endDate)){
                        for (Integer vehicleIds : vehicleId) {
                            List<RoadMasterDto> roadDetailByVehicleId = vehicleService.getRoadDetailByVehicleId(vehicleIds);
                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleIds, null, null, userId);
                            for (VehicleDeviceMappingDto deviceObj : getdeviceList) {
                                List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceObj.getDeviceId());
                                for (DeviceDto imei : getImeiList) {
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, deviceObj.getCreatedOn(), deviceObj.getDeactivationDate());
                                    for (VtuLocationDto vtuobj : vtuLocationDto) {
                                        vtuobj.setDeviceId(imei.getId());
                                        vtuobj.setVehicleId(deviceObj.getVehicleId());

                                    }
                                    Map<String, Object> itemVal = new HashMap<>();
                                    itemVal.put("vtuLocation", vtuLocationDto);
                                    itemVal.put("road", roadDetailByVehicleId);
                                    result.add(itemVal);
//                            Map<String, Object> itemVal = new HashMap<>();
//                            itemVal.put("imeiNo", imei.getImeiNo1());
//                            itemVal.put("vehicleLocation", vtuLocationDto);
//                            result.add(itemVal);
                                }
                                // deviceIds.add(vehicleid.getDeviceId());

                            }

                        }
                    }
                else {
                        Map<String, Object> itemVal = new HashMap<>();
                        itemVal.put("vtuLocation", new ArrayList<>());
                        result.add(itemVal);
                        response.setData(result);
                        response.setStatus(1);
                        response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                        response.setMessage("All Location Details");
                        return response;
                    }



                }
                else if (activityIds != null && !activityIds.isEmpty()) {
                    for (Integer activityItem : activityIds) {
                        List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityItem, userId);
                        for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                            List<RoadMasterDto> road = vehicleService.getRoadDetailByVehicleId(vehicleList.getVehicleId());
                            for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                //int i = 0;
                                for (DeviceDto imei : getImeiList) {
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                    // i++;
                                    for (VtuLocationDto vtuobj : vtuLocationDto) {
                                        vtuobj.setDeviceId(imei.getId());
                                        vtuobj.setVehicleId(vehicleid.getVehicleId());
                                        //vtuobj.setWorkId(activityItem.getWorkId());
                                    }
                                    Map<String, Object> itemVal = new HashMap<>();
                                    //itemVal.put("imeiNo", imei.getImeiNo1());
                                    itemVal.put("vehicleLocation", vtuLocationDto);
                                    itemVal.put("road", road);

                                    result.add(itemVal);
                                }
                                // deviceIds.add(vehicleid.getDeviceId());

                            }
                        }
                    }

                }
                else if (workId != null && !workId.isEmpty()) {

                    for (Integer workitem : workId) {
                        List<GeoMappingEntity> activityDtoList = workService.getActivityDetailsByWorkId(workitem);
                        for (GeoMappingEntity activityId : activityDtoList) {
                            List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId, activityId.getActivityStartDate(), activityId.getActivityCompletionDate());
                            for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                //road Details By vehicle
                                List<RoadMasterDto> road = vehicleService.getRoadDetailByVehicleId(vehicleList.getVehicleId());

                                List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                    //int i = 0;
                                    for (DeviceDto imei : getImeiList) {
                                        List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                        // i++;
                                        for (VtuLocationDto vtuobj : vtuLocationDto) {
                                            vtuobj.setDeviceId(imei.getId());
                                            vtuobj.setVehicleId(vehicleid.getVehicleId());
                                            vtuobj.setWorkId(activityId.getPackageId());
                                        }
                                        Map<String, Object> itemVal = new HashMap<>();
//                                    itemVal.put("imeiNo", imei.getImeiNo1());
                                        itemVal.put("road", road);
                                        itemVal.put("vehicleLocation", vtuLocationDto);
                                        result.add(itemVal);
                                    }
                                    // deviceIds.add(vehicleid.getDeviceId());

                                }
                            }

                        }
                    }


                }
                else if (roadId != null && !roadId.isEmpty()) {
                    for (Integer roadid : roadId) {
                        List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadid);

                        for (GeoMasterDto item : workByRoad) {
                            List<GeoMappingEntity> activityDtoList = workService.getActivityDetailsByWorkId(item.getWorkId());
                            for (GeoMappingEntity activityId : activityDtoList) {
                                List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId, activityId.getActivityStartDate(), activityId.getActivityCompletionDate());
                                for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                    //road Details By vehicle
                                    List<RoadMasterDto> road = vehicleService.getRoadDetailByVehicleId(vehicleList.getVehicleId());

                                    List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                    for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                        List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                        //int i = 0;
                                        for (DeviceDto imei : getImeiList) {
                                            List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                            // i++;
                                            for (VtuLocationDto vtuobj : vtuLocationDto) {
                                                vtuobj.setDeviceId(imei.getId());
                                                vtuobj.setVehicleId(vehicleid.getVehicleId());
                                                vtuobj.setWorkId(activityId.getPackageId());
                                            }
                                            Map<String, Object> itemVal = new HashMap<>();
//                                    itemVal.put("imeiNo", imei.getImeiNo1());
                                            itemVal.put("road", road);
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
                else if (contractorId != null && !contractorId.isEmpty()) {
                    for (Integer contractorid : contractorId) {
                        List<GeoMasterDto> workByContractorIds = roadService.workByContractorIds(contractorid);
                        for (GeoMasterDto workByContractorId : workByContractorIds) {
                            List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(workByContractorId.getRoadId());
                            for (GeoMasterDto item : workByRoad) {
                                List<ActivityDto> activityDtoList = workService.getActivityByWorkId(item.getWorkId());
                                for (ActivityDto activityId : activityDtoList) {
                                    List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId);
                                    for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                        List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                        for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                            List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                            //int i = 0;
                                            for (DeviceDto imei : getImeiList) {
                                                List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                                // i++;
                                                for (VtuLocationDto vtuobj : vtuLocationDto) {
                                                    vtuobj.setDeviceId(imei.getId());
                                                    vtuobj.setVehicleId(vehicleid.getVehicleId());
                                                    vtuobj.setWorkId(activityId.getWorkId());
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


                }
                else if (districtId != null && !districtId.isEmpty()) {
                    for (Integer districtitem : districtId) {
                        List<GeoMasterDto> workByDistrictIds = roadService.getworkByDistrictId(districtitem);
                        for (GeoMasterDto WorkObj : workByDistrictIds) {
                            List<GeoMappingEntity> activityDtoList = workService.getActivityDetailsByWorkId(WorkObj.getWorkId());
                            for (GeoMappingEntity activityId : activityDtoList) {
                                List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId, activityId.getActivityStartDate(), activityId.getActivityCompletionDate());
                                for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                    //road Details By vehicle
                                    List<RoadMasterDto> road = vehicleService.getRoadDetailByVehicleId(vehicleList.getVehicleId());

                                    List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                    for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                        List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                        //int i = 0;
                                        for (DeviceDto imei : getImeiList) {
                                            List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                            // i++;
                                            for (VtuLocationDto vtuobj : vtuLocationDto) {
                                                vtuobj.setDeviceId(imei.getId());
                                                vtuobj.setVehicleId(vehicleid.getVehicleId());
                                                vtuobj.setWorkId(activityId.getPackageId());
                                            }
                                            Map<String, Object> itemVal = new HashMap<>();
//                                    itemVal.put("imeiNo", imei.getImeiNo1());
                                            itemVal.put("road", road);
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
                else if (blockId != null && !blockId.isEmpty()) {


                    for (Integer blockObj : blockId) {
                        List<GeoMasterDto> workByBlockId = roadService.getworkByBlockId(blockObj);
                        for (GeoMasterDto workItem : workByBlockId) {
                            List<GeoMappingEntity> activityDtoList = workService.getActivityDetailsByWorkId(workItem.getWorkId());
                            for (GeoMappingEntity activityId : activityDtoList) {
                                List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId, activityId.getActivityStartDate(), activityId.getActivityCompletionDate());
                                for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                    //road Details By vehicle
                                    List<RoadMasterDto> road = vehicleService.getRoadDetailByVehicleId(vehicleList.getVehicleId());

                                    List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                    for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                        List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                        //int i = 0;
                                        for (DeviceDto imei : getImeiList) {
                                            List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                            // i++;
                                            for (VtuLocationDto vtuobj : vtuLocationDto) {
                                                vtuobj.setDeviceId(imei.getId());
                                                vtuobj.setVehicleId(vehicleid.getVehicleId());
                                                vtuobj.setWorkId(activityId.getPackageId());
                                            }
                                            Map<String, Object> itemVal = new HashMap<>();
//                                    itemVal.put("imeiNo", imei.getImeiNo1());
                                            itemVal.put("road", road);
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
                else if (divisionId != null && !divisionId.isEmpty()) {

                    for (Integer divisionObj : divisionId) {
                        List<GeoMasterDto> workByDivisionId = roadService.getworkByDivisionId(divisionObj);
                        for (GeoMasterDto workItem : workByDivisionId) {
                            List<GeoMappingEntity> activityDtoList = workService.getActivityDetailsByWorkId(workItem.getWorkId());
                            for (GeoMappingEntity activityId : activityDtoList) {
                                List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId, activityId.getActivityStartDate(), activityId.getActivityCompletionDate());
                                for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                    //road Details By vehicle
                                    List<RoadMasterDto> road = vehicleService.getRoadDetailByVehicleId(vehicleList.getVehicleId());

                                    List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                    for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                        List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                        //int i = 0;
                                        for (DeviceDto imei : getImeiList) {
                                            List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                                            // i++;
                                            for (VtuLocationDto vtuobj : vtuLocationDto) {
                                                vtuobj.setDeviceId(imei.getId());
                                                vtuobj.setVehicleId(vehicleid.getVehicleId());
                                                vtuobj.setWorkId(activityId.getPackageId());
                                            }
                                            Map<String, Object> itemVal = new HashMap<>();
//                                    itemVal.put("imeiNo", imei.getImeiNo1());
                                            itemVal.put("road", road);
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
                else {
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
        } else {
            response.setStatus(0);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
            response.setMessage("No UserId Found");
        }

        return response;
    }


    @PostMapping("/getLastLocationRecordList")
    public RDVTSResponse getLastLocationRecordList(@RequestParam(name = "userId") Integer userId,
                                                   @RequestParam(name = "imei1", required = false) List<Long> imei1,
                                                   @RequestParam(name = "imei2", required = false) List<Long> imei2,
                                                   @RequestParam(name = "deviceId", required = false) List<Integer> deviceId,
                                                   @RequestParam(name = "vehicleId", required = false) List<Integer> vehicleId,
                                                   @RequestParam(name = "activityId", required = false) List<Integer> activityIds,
                                                   @RequestParam(name = "workId", required = false) List<Integer> workId,
                                                   @RequestParam(name = "roadId", required = false) List<Integer> roadId,
                                                   @RequestParam(name = "contractorId", required = false) List<Integer> contractorId,
                                                   @RequestParam(name = "districtId", required = false) List<Integer> districtId,
                                                   @RequestParam(name = "blockId", required = false) List<Integer> blockId,
                                                   @RequestParam(name = "divisionId", required = false) List<Integer> divisionId,
                                                   @RequestParam(name = "circleId", required = false) List<Integer> circleId,
                                                   @RequestParam(name = "startTime", required = false) String startTime,
                                                   @RequestParam(name = "endTime", required = false) String endTime) throws ParseException {
        RDVTSResponse response = new RDVTSResponse();
        if (userId != null) {

            List<Map<String, Object>> result = new ArrayList<>();
            Map<Integer, Object> device = new HashMap<>();
            //Get All Device Ids
            Date startDate = null;
            Date endDate = null;
            Date vehicleStartDate = null;
            Date vehicleendDate = null;
            if (startTime != null && endTime != null) {
                startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime);
                endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime);
            }

            try {
                //Get all Vehicles
//                if(deviceId != null && !deviceId.isEmpty() && vehicleId != null && !vehicleId.isEmpty()
//                && workId != null && !workId.isEmpty() && roadId != null && !roadId.isEmpty() &&
//                        contractorId != null && !contractorId.isEmpty() && districtId != null && !districtId.isEmpty()
//                && blockId != null && !blockId.isEmpty() && divisionId != null && !divisionId.isEmpty())

                if (deviceId != null && !deviceId.isEmpty()) {
                    List<Integer> deviceIdList = new ArrayList<>();
                    for (Integer deviceIdKey : deviceId) {
                        deviceIdList.add(deviceIdKey);
                    }


                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationByDeviceId(deviceIdList, null);
                    Map<String, Object> itemVal = new HashMap<>();
                    itemVal.put("vehicleLocation", vtuLocationDto);
                    result.add(itemVal);
                } else if (vehicleId != null && !vehicleId.isEmpty()) {

                    for (Integer vehicleitem : vehicleId) {
                        List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleitem, null, null, userId);
                        if (getdeviceList.size() > 0) {
                            List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationRecordList(getdeviceList, startDate, endDate);
                            Map<String, Object> itemVal = new HashMap<>();
                            itemVal.put("vehicleLocation", vtuLocationDto);
                            result.add(itemVal);
                        }


                    }
                } else if (activityIds != null && !activityIds.isEmpty()) {
                    for (Integer activityItem : activityIds) {
                        List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityItem, userId);
                        for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                            if (getdeviceList.size() > 0) {
                                List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationRecordList(getdeviceList, startDate, endDate);
                                Map<String, Object> itemVal = new HashMap<>();
                                itemVal.put("vehicleLocation", vtuLocationDto);
                                result.add(itemVal);
                            }
                        }
                    }

                } else if (workId != null && !workId.isEmpty()) {

                    for (Integer workitem : workId) {
                        List<ActivityDto> activityDtoList = workService.getActivityByWorkId(workitem);
                        for (ActivityDto activityId : activityDtoList) {
                            List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId);
                            for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                if (getdeviceList.size() > 0) {
                                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationRecordList(getdeviceList, startDate, endDate);
                                    Map<String, Object> itemVal = new HashMap<>();
                                    itemVal.put("vehicleLocation", vtuLocationDto);
                                    result.add(itemVal);
                                }
                            }

                        }
//                    List<VehicleWorkMappingDto> vehicleIdList = workService.getVehicleListByWorkId(workitem);
//                    for (VehicleWorkMappingDto workIditem : vehicleIdList) {
//
//
//
//                    }
                    }

                } else if (roadId != null && !roadId.isEmpty()) {
                    for (Integer roadid : roadId) {
                        List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(roadid);

                        for (GeoMasterDto item : workByRoad) {

                            List<ActivityDto> activityDtoList = workService.getActivityByWorkId(item.getWorkId());
                            for (ActivityDto activityId : activityDtoList) {
                                List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId);
                                for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                    List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                    if (getdeviceList.size() > 0) {
                                        List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationRecordList(getdeviceList, startDate, endDate);
                                        Map<String, Object> itemVal = new HashMap<>();
                                        itemVal.put("vehicleLocation", vtuLocationDto);
                                        result.add(itemVal);
                                    }
                                }

                            }


                        }

                    }


                } else if (contractorId != null && !contractorId.isEmpty()) {
                    for (Integer contractorid : contractorId) {
                        List<GeoMasterDto> workByContractorIds = roadService.workByContractorIds(contractorid);

                        for (GeoMasterDto workByContractorId : workByContractorIds) {


                            List<GeoMasterDto> workByRoad = roadService.getWorkByroadIds(workByContractorId.getRoadId());

                            for (GeoMasterDto item : workByRoad) {
                                // workIds.add(item.getWorkId());
                                List<ActivityDto> activityDtoList = workService.getActivityByWorkId(item.getWorkId());
                                for (ActivityDto activityId : activityDtoList) {
                                    List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), userId);
                                    for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                        List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                                        if (getdeviceList.size() > 0) {
                                            List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationRecordList(getdeviceList, startDate, endDate);
                                            Map<String, Object> itemVal = new HashMap<>();
                                            itemVal.put("vehicleLocation", vtuLocationDto);
                                            result.add(itemVal);
                                        }
                                    }

                                }
                            }

                        }


                    }


                } else if (blockId != null && !blockId.isEmpty()) {
                    final Integer CheckArea = 2;////For Block Search
                    List<Integer> IdList = new ArrayList<>();
                    for (Integer item : blockId) {
                        IdList.add(item);
                    }
                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationByDeviceId(IdList, CheckArea);
                    Map<String, Object> itemVal = new HashMap<>();
                    itemVal.put("vehicleLocation", vtuLocationDto);
                    result.add(itemVal);
                } else if (divisionId != null && !divisionId.isEmpty()) {
                    final Integer CheckArea = 3; //For Division Search
                    List<Integer> IdList = new ArrayList<>();
                    for (Integer item : divisionId) {
                        IdList.add(item);
                    }
                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationByDeviceId(IdList, CheckArea);
                    Map<String, Object> itemVal = new HashMap<>();
                    itemVal.put("vehicleLocation", vtuLocationDto);
                    result.add(itemVal);
                } else if (districtId != null && !districtId.isEmpty()) {
                    final Integer CheckArea = 1;
                    List<Integer> IdList = new ArrayList<>();
                    for (Integer item : districtId) {
                        IdList.add(item);
                    }
                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationByDeviceId(IdList, CheckArea);
                    Map<String, Object> itemVal = new HashMap<>();
                    itemVal.put("vehicleLocation", vtuLocationDto);
                    result.add(itemVal);
                } else if (circleId != null && !circleId.isEmpty()) {
                    final Integer CheckArea = 4; //For Circle
                    List<Integer> IdList = new ArrayList<>();
                    for (Integer item : circleId) {
                        IdList.add(item);
                    }
                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationByDeviceId(IdList, CheckArea);
                    Map<String, Object> itemVal = new HashMap<>();
                    itemVal.put("vehicleLocation", vtuLocationDto);
                    result.add(itemVal);
                } else {
                    List<Integer> IdList = new ArrayList<>();
                    IdList.add(-1);
                    List<VtuLocationDto> vtuLocationDto = locationService.getLastLocationByDeviceId(IdList, null);
                    Map<String, Object> itemVal = new HashMap<>();
                    itemVal.put("vehicleLocation", vtuLocationDto);
                    result.add(itemVal);
//                    response.setStatus(1);
//                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
//                    response.setMessage("No Imei Found");
                }


                if (result.size() > 0) {
                    response.setData(result);
                    response.setStatus(1);
                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    response.setMessage("All Last Location Details");

                } else {
                    response.setStatus(0);
                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    Map<String, Object> itemVal = new HashMap<>();
                    itemVal.put("vehicleLocation", new ArrayList<>());
                    response.setMessage("No  Record Found");
                    result.add(itemVal);
                    response.setData(result);
                }


            } catch (Exception ex) {
                ex.printStackTrace();
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        ex.getMessage(),
                        result);
            }
        } else {

            response.setStatus(0);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
            response.setMessage("No UserId Found");

        }

        return response;
    }


    @PostMapping("/getVehicleListByWorkId")
    public RDVTSResponse getVehicleListByWorkId(@RequestParam(name = "workIds", required = false) List<Integer> workIds,
                                                @RequestParam(name = "userId") Integer userId) {
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

