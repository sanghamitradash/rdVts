package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.repository.*;

import gov.orsac.RDVTS.repositoryImpl.CronRepositoryImpl;
import gov.orsac.RDVTS.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/alert")
public class AlertController {

    @Autowired
    public DeviceService deviceService;

    @Autowired
    public LocationService locationService;

    @Autowired
    public AlertService alertService;

    @Autowired
    public RoadService roadService;

    @Autowired
    public WorkService workService;

    @Autowired
    public RoadMasterRepository roadMasterRepository;

    @Autowired
    public GeoMasterLogRepository geoMasterLogRepository;

    @Autowired
    public GeoDistrictMasterRepository geoDistrictMasterRepository;

    @Autowired
    public ActivityMasterRepository activityMasterRepository;


    @Autowired
    public PiuRepository piuRepository;

    @Autowired
    public PackageMasterRepository packageMasterRepository;

    @Autowired
    public CronRepositoryImpl cronRepository;

    @Autowired
    public RoadRepository roadRepository;

    @Autowired
    public WorkRepository workRepository;


    @Autowired
    public ContractorMasterRepository contractorMasterRepository;

    @Autowired GeoMappingRepository geoMappingRepository;


    @Autowired
    public VehicleService vehicleService;
    final Integer OVER_SPEED_ALERT_ID = 1;//NO_DATA_ALERT_ID For Alert TYpe Stored in DB
    final Integer NO_DATA_ALERT_ID = 2;//NO_DATA_ALERT_ID For Alert TYpe Stored in DB
    final Integer NO_MOVEMENT_ALERT_ID = 3;
    final Integer GEO_FENCE_ALERT_ID = 4;
    final Integer noDataAlertTimeSpan = 60; //in minutes Alert Time Span
    final Integer NO_MOVEMENT_TIME_GAP = 15; //in minutes
    final Integer LOCATION_DATA_FREQUENCY = 6; //per minute 6 locations are saved
    final Integer OUTSIDE_POINT_COUNT = 5;


    @RequestMapping("/generateNoDataAlert")

    public RDVTSResponse generateNoDataAlert(@RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultabc = new ArrayList<>();
//        final Integer noDataAlertTimeSpan = 60; //in minutes Alert Time Span
        // final Integer NO_DATA_ALERT_ID = 3;//NO_DATA_ALERT_ID For Alert TYpe Stored in DB

        try {
            Integer deviceId = -1; //fro getting all device
            //get all device
            List<DeviceDto> device = deviceService.getAllDeviceDD(deviceId, null);
            Map<String, Integer> map = new HashMap<>();
            for (DeviceDto item : device) {
                //get Last location of the Current Date
                VtuLocationDto locationDto = locationService.getLastLocationByImei(item.getImeiNo1());
                if (locationDto != null) {
                    Integer noDataAlertStatus = 0;
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String currDtTime = dateFormat.format(date);//Date to String Convert
                    if (locationDto.getDateTime() != null && !locationDto.getDateTime().toString().isEmpty()) {
                        String lastLocTime = dateFormat.format(locationDto.getDateTime());

                        Date currDtTimeParsed = null;
                        Date lastLocTimeParsed = null;
                        try {
                            currDtTimeParsed = dateFormat.parse(currDtTime);//String To date Convert
                            lastLocTimeParsed = dateFormat.parse(lastLocTime);//String To date Convert
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff = currDtTimeParsed.getTime() - lastLocTimeParsed.getTime();//get difference of last Location Date and current date
                        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);//Convert the Difference in minutes
                        //long diffMinutes = diff / (60 * 1000) % 60;
                        if (diffInMinutes > noDataAlertTimeSpan) {
                            noDataAlertStatus = 1;
                        }
//                        System.out.println(noDataAlertStatus);
                        if (noDataAlertStatus == 1) {
                            Boolean alertExists = alertService.checkAlertExists(item.getImeiNo1(), NO_DATA_ALERT_ID); //Check If alert Exist Or Not
                            if (!alertExists) {

                                AlertEntity alertEntity = new AlertEntity();
                                alertEntity.setImei(locationDto.getImei());
                                alertEntity.setAlertTypeId(NO_DATA_ALERT_ID);
                                if (locationDto.getLatitude() != null) {
                                    alertEntity.setLatitude(Double.parseDouble(locationDto.getLatitude()));
                                }
                                if (locationDto.getLongitude() != null) {
                                    alertEntity.setLongitude(Double.parseDouble(locationDto.getLongitude()));
                                }
                                if (locationDto.getAltitude() != null) {
                                    alertEntity.setAltitude(Double.parseDouble(locationDto.getAltitude()));
                                }
                                if (locationDto.getAccuracy() != null) {
                                    alertEntity.setAccuracy(Double.parseDouble(locationDto.getAccuracy()));
                                }

                                alertEntity.setSpeed(Double.parseDouble(locationDto.getSpeed()));
                                alertEntity.setGpsDtm(currDtTimeParsed);

                                AlertEntity alertEntity1 = alertService.saveAlert(alertEntity);//If Not exist save alert in Alert Table
                                Map<String, Object> itemVal = new HashMap<>();
                                List<AlertEntity> alertEntityList = new ArrayList<>();
                                alertEntityList.add(alertEntity1);
                                itemVal.put("key", alertEntityList);

                                resultabc.add(itemVal);

                            }
                        } else {
                            alertService.updateResolve(item.getImeiNo1(), NO_DATA_ALERT_ID);//set is_resolve True
                        }

                    }

                }

            }

            result.put("device", device);
            result.put("model", resultabc);
            response.setData(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), ex.getMessage(), result);
        }


        return response;

    }

    @RequestMapping("/generateRotationAlert")
//incomplete
    public RDVTSResponse generateRotationAlert() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<WorkDto> workDto = workService.getWorkById(62);//get All Work

            for (WorkDto Work : workDto) {
                List<RoadMasterDto> road = roadService.getRoadByWorkId(Work.getId()); //get Road Details By WorkId
                if (road.size() > 0 && road.get(0).getGeom() != null) {
                    List<ActivityWorkMapping> activityDtoList = workService.getActivityDetailsByWorkId(Work.getId()); //Get Activity BY WorkId
                    for (ActivityWorkMapping activityId : activityDtoList) {
                        //Get Vehicle By Activity
                        List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(), null, activityId.getActualActivityStartDate(), activityId.getActualActivityCompletionDate());
                        for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                            //get Device BY vehicle ID
                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), null);
                            if (getdeviceList.size() > 0) {
                                for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                    //get Imei By Device
                                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                    for (DeviceDto imei : getImeiList) {
                                        //get Location By Imei
                                        List<VtuLocationDto> vtuLocationDto = locationService.getLocationRecordListWithGeofence(imei.getImeiNo1(), imei.getImeiNo2(), vehicleid.getCreatedOn(), vehicleid.getDeactivationDate(), road.get(0).getId());
                                        Integer rotate = 0;
                                        Double distance = 0.0;

                                        for (int i = 0; i <= vtuLocationDto.size(); i++) {
                                            if (i + 1 < vtuLocationDto.size()) {
                                                AlertDegreeDistanceDto degreeDistanceDto = locationService.getRotationDetails(vtuLocationDto.get(i).getLongitude(), vtuLocationDto.get(i).getLatitude(), vtuLocationDto.get(i + 1).getLongitude(), vtuLocationDto.get(i + 1).getLatitude());
                                                distance += degreeDistanceDto.getStDistance();
                                                if (degreeDistanceDto.getDegrees() != null && degreeDistanceDto.getDegrees() > 90.0) {
                                                    rotate++;
                                                    // distance+=degreeDistanceDto.getStDistance();
                                                }
                                            }


                                        }
                                        System.out.println(imei.getImeiNo1());
                                        System.out.println(rotate);
                                        System.out.println(distance);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), ex.getMessage(), result);
        }


        return response;

    }


    @RequestMapping("/generateNoMovementAlert")
    public RDVTSResponse generateNoMovementAlert(@RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultabc = new ArrayList<>();


//        final Integer NO_MOVEMENT_ALERT_ID = 2;
//        final Integer NO_MOVEMENT_TIME_GAP = 15; //in minutes
//        final Integer LOCATION_DATA_FREQUENCY = 6; //per minute 6 locations are saved
//        final Integer SEEDING_GAP = 3; //taking point by 3 record gap
//        final Integer OUTSIDE_POINT_COUNT = 5;

        try {
            AlertTypeEntity alertTypeEntity = alertService.getAlertTypeDetails(NO_MOVEMENT_ALERT_ID);
            //Get Imei
            List<Long> imei = alertService.getImeiForNoMovement(); //get today all imei
            if (imei.size() > 0) {
                for (Long item : imei) {
                    Integer recordLimit = NO_MOVEMENT_TIME_GAP * LOCATION_DATA_FREQUENCY;
                    List<VtuLocationDto> vtuLocationDto = alertService.getLocationRecordByFrequency(item, recordLimit);
                    //Create buffer of First First Point
                    Integer outsideCount = 0;
                    for (VtuLocationDto vtuItem : vtuLocationDto) {
                        Boolean b = alertService.checkIntersected(vtuLocationDto.get(0).getLongitude(), vtuLocationDto.get(0).getLatitude(), vtuItem.getLongitude(), vtuItem.getLatitude());
                        if (b == false) {
                            outsideCount++;
                        }


                    }

                    if (outsideCount >= OUTSIDE_POINT_COUNT) {
                        //resolve if there is any unresolve no-movement alert present

                        Boolean updateResolve = alertService.updateResolve(item, NO_MOVEMENT_ALERT_ID);
                        //break;
//                        if (!updateResolve) {
//                            break;
////                            return true;
//                        }

                    }

                    if (outsideCount < OUTSIDE_POINT_COUNT) {
                        Boolean alertExists = alertService.checkAlertExists(item, NO_MOVEMENT_ALERT_ID); //Check If alert Exist Or Not
                        if (!alertExists) {
                            AlertEntity alertEntity = new AlertEntity();
                            alertEntity.setImei(item);
                            alertEntity.setAlertTypeId(NO_MOVEMENT_ALERT_ID);
                            if (vtuLocationDto.get(0).getLatitude() != null) {
                                alertEntity.setLatitude(Double.parseDouble(vtuLocationDto.get(0).getLatitude()));
                            }
                            if (vtuLocationDto.get(0).getLongitude() != null) {
                                alertEntity.setLongitude(Double.parseDouble(vtuLocationDto.get(0).getLongitude()));
                            }
                            if (vtuLocationDto.get(0).getAltitude() != null) {
                                alertEntity.setAltitude(Double.parseDouble(vtuLocationDto.get(0).getAltitude()));
                            }
                            if (vtuLocationDto.get(0).getAccuracy() != null) {
                                alertEntity.setAccuracy(Double.parseDouble(vtuLocationDto.get(0).getAccuracy()));
                            }

                            alertEntity.setSpeed(Double.parseDouble(vtuLocationDto.get(0).getSpeed()));
                            alertEntity.setGpsDtm(new Date());

                            AlertEntity alertEntity1 = alertService.saveAlert(alertEntity);//If Not exist save alert in Alert Table

                        }

                    }
//                    List<BufferDto> bufferDto = alertService.getBuffer(item);

//                    for (BufferDto bfd : bufferDto) {
//                        //Check if the last location of imei comes under any road
//                        if (bfd.getRoadBuffer() != null) {
//                            Boolean b = alertService.checkIntersected(bfd.getRoadBuffer(), vtuLocationDto.get(0).getLatitude(), vtuLocationDto.get(0).getLongitude());
//                            if (b) break;
//                        }
//                    }
                    //creating the a 100mt buffer of the 90th point backward and check the points
//                    Integer bufferPointIndex = recordLimit - 1;
//                    String longitude = vtuLocationDto.get(bufferPointIndex).getLongitude();
//                    String latitude = vtuLocationDto.get(bufferPointIndex).getLatitude();
//
//                    Integer outsideCount = 0;
//                    for (int i = bufferPointIndex; i >= 0; i -= SEEDING_GAP) {
//                        Boolean bufferQuery = alertService.bufferQuery(longitude, latitude, vtuLocationDto.get(i).getLongitude(), vtuLocationDto.get(i).getLatitude());
//                        if (!bufferQuery) {
//                            outsideCount++;
//                        }
//                    }

//                    if (outsideCount >= OUTSIDE_POINT_COUNT) {
//                        //resolve if there is any unresolve no-movement alert present
//
//                        Boolean updateResolve=  alertService.updateResolve(item, NO_MOVEMENT_ALERT_ID);
//
//                        if (!updateResolve) {
//                            break;
////                            return true;
//                        }
//
//                    }
//
//                    if(outsideCount < OUTSIDE_POINT_COUNT){
//                        AlertDto alertExists = alertService.checkAlertExists(item, NO_MOVEMENT_ALERT_ID); //Check If alert Exist Or Not
//                        if (alertExists == null) {
//                            AlertEntity alertEntity = new AlertEntity();
//                            alertEntity.setImei(item);
//                            alertEntity.setAlertTypeId(NO_MOVEMENT_ALERT_ID);
//                            if (vtuLocationDto.get(0).getLatitude() != null) {
//                                alertEntity.setLatitude(Double.parseDouble(vtuLocationDto.get(0).getLatitude()));
//                            }
//                            if (vtuLocationDto.get(0).getLongitude() != null) {
//                                alertEntity.setLongitude(Double.parseDouble(vtuLocationDto.get(0).getLongitude()));
//                            }
//                            if (vtuLocationDto.get(0).getAltitude() != null) {
//                                alertEntity.setAltitude(Double.parseDouble(vtuLocationDto.get(0).getAltitude()));
//                            }
//                            if (vtuLocationDto.get(0).getAccuracy() != null) {
//                                alertEntity.setAccuracy(Double.parseDouble(vtuLocationDto.get(0).getAccuracy()));
//                            }
//
//                            alertEntity.setSpeed(Double.parseDouble(vtuLocationDto.get(0).getSpeed()));
//                            alertEntity.setGpsDtm(new Date());
//
//                            AlertEntity alertEntity1 = alertService.saveAlert(alertEntity);//If Not exist save alert in Alert Table
//
//                        }
//
//                    }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), ex.getMessage(), result);
        }


        return response;

    }

    @RequestMapping("/generateGeofenceAlert")
    public RDVTSResponse generateGeofenceAlert(@RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultabc = new ArrayList<>();

//        final Integer GEO_FENCE_ALERT_ID = 4;
//        final Integer NO_MOVEMENT_TIME_GAP = 15; //in minutes
//        final Integer LOCATION_DATA_FREQUENCY = 6; //per minute 6 locations are saved
//        final Integer SEEDING_GAP = 3; //taking point by 3 record gap
//        final Integer OUTSIDE_POINT_COUNT = 5;

        try {
            //Get All Work
            List<WorkDto> workDto = workService.getWorkById(-1);


            //Foreach Work Get Vehicle
            //Foreach Vechicle get Device
            //Foreach device get Imei
            //Foreach Imei Get location Record list

            for (WorkDto Work : workDto) {
                //Foreach work get Road Geom
                List<RoadMasterDto> road = roadService.getRoadByWorkId(Work.getId());
                //Foreach Work Get Vehicle
                //Foreach Vechicle get Device
                //Foreach device get Imei
                //Foreach Imei Get location Record list
                if (road.size() > 0 && road.get(0).getGeom() != null) {
                    List<ActivityWorkMapping> activityDtoList = workService.getActivityDetailsByWorkId(Work.getId());
                    for (ActivityWorkMapping activityId : activityDtoList) {
                        List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getActivityId(), userId, activityId.getActualActivityStartDate(), activityId.getActualActivityCompletionDate());
                        for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                            List<VehicleDeviceMappingDto> getdeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), userId);
                            if (getdeviceList.size() > 0) {


                                for (VehicleDeviceMappingDto vehicleid : getdeviceList) {
                                    List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(vehicleid.getDeviceId());
                                    //int i = 0;
                                    for (DeviceDto imei : getImeiList) {
                                        Date startDate = null;
                                        Date endDate = null;
                                        Integer recordLimit = NO_MOVEMENT_TIME_GAP * LOCATION_DATA_FREQUENCY;

                                        List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate(), recordLimit);
                                        // Integer outsideCount=0;
                                        for (VtuLocationDto vtuItem : vtuLocationDto) {
//                                            if (road.size() > 0 && road.get(0).getGeom() != null) {

                                            Boolean b = alertService.checkGeoFenceIntersected(road.get(0).getGeom(), vtuItem.getLongitude(), vtuItem.getLatitude());
                                            if (b == false) {
                                                Boolean alertExists = alertService.checkAlertExists(vtuItem.getImei(), GEO_FENCE_ALERT_ID); //Check If alert Exist Or Not
                                                if (!alertExists) {
                                                    AlertEntity alertEntity = new AlertEntity();
                                                    alertEntity.setImei(vtuItem.getImei());
                                                    alertEntity.setAlertTypeId(GEO_FENCE_ALERT_ID);
                                                    if (vtuItem.getLatitude() != null) {
                                                        alertEntity.setLatitude(Double.parseDouble(vtuItem.getLatitude()));
                                                    }
                                                    if (vtuItem.getLongitude() != null) {
                                                        alertEntity.setLongitude(Double.parseDouble(vtuItem.getLongitude()));
                                                    }
                                                    if (vtuItem.getAltitude() != null) {
                                                        alertEntity.setAltitude(Double.parseDouble(vtuItem.getAltitude()));
                                                    }
                                                    if (vtuItem.getAccuracy() != null) {
                                                        alertEntity.setAccuracy(Double.parseDouble(vtuItem.getAccuracy()));
                                                    }

                                                    alertEntity.setSpeed(Double.parseDouble(vtuItem.getSpeed()));
                                                    alertEntity.setGpsDtm(new Date());

                                                    AlertEntity alertEntity1 = alertService.saveAlert(alertEntity);//If Not exist save alert in Alert Table

                                                }
                                                //  outsideCount++;

                                            } else {
                                                Boolean updateResolve = alertService.updateResolve(vtuItem.getImei(), GEO_FENCE_ALERT_ID);

                                            }


//                                            }


                                        }


                                    }


                                }
                            }
                        }

                    }
                }
            }


            //Foreach work get Road Geom

            //Foreach Work Get Vehicle
            //Foreach Vechicle get Device
            //Foreach device get Imei
            //Foreach Imei Get location Record list
            //Check each Location By Road geom
            //if intersect
            //if not intersect


        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), ex.getMessage(), result);
        }


        return response;

    }


    @RequestMapping("/generateOverSpeedAlert")
    public RDVTSResponse generateOverSpeedAlert(@RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultabc = new ArrayList<>();
        //final Integer noDataAlertTimeSpan = 60; //in minutes Alert Time Span
        final Integer OVER_SPEED_ALERT_ID = 5;//NO_DATA_ALERT_ID For Alert TYpe Stored in DB
        final Integer OVER_SPEED_TIME = 15; //in minutes
        final Integer LOCATION_DATA_FREQUENCY = 6; //per minute 6 locations are saved
        Integer recordLimit = OVER_SPEED_TIME * LOCATION_DATA_FREQUENCY;

        try {

            List<AlertDto> alertDto = alertService.getAllDeviceByVehicle();
            for (AlertDto alertDtoItem : alertDto) {
                List<VtuLocationDto> vtuLocationDto = alertService.getAlertLocationOverSpeed(alertDtoItem.getImei(), alertDtoItem.getSpeedLimit());
                if (vtuLocationDto != null) {

                    if (vtuLocationDto.size() > 0) {
                        Boolean checkSpeedStatus = false;
                        for (VtuLocationDto item : vtuLocationDto) {
                            Boolean checkIsNumeric = isNumeric(item.getSpeed());
                            if (checkIsNumeric) {
                                if (Double.parseDouble(item.getSpeed()) > alertDtoItem.getSpeedLimit()) {
                                    Boolean alertExists = alertService.checkAlertExists(item.getImei(), OVER_SPEED_ALERT_ID); //Check If alert Exist Or Not
                                    if (!alertExists) {

                                        AlertEntity alertEntity = new AlertEntity();
                                        alertEntity.setImei(item.getImei());
                                        alertEntity.setAlertTypeId(OVER_SPEED_ALERT_ID);
                                        if (item.getLatitude() != null) {
                                            alertEntity.setLatitude(Double.parseDouble(item.getLatitude()));
                                        }
                                        if (item.getLongitude() != null) {
                                            alertEntity.setLongitude(Double.parseDouble(item.getLongitude()));
                                        }
                                        if (item.getAltitude() != null) {
                                            alertEntity.setAltitude(Double.parseDouble(item.getAltitude()));
                                        }
                                        if (item.getAccuracy() != null) {
                                            alertEntity.setAccuracy(Double.parseDouble(item.getAccuracy()));
                                        }

                                        alertEntity.setSpeed(Double.parseDouble(item.getSpeed()));
                                        alertEntity.setGpsDtm(new Date());

                                        AlertEntity alertEntity1 = alertService.saveAlert(alertEntity);//If Not exist save alert in Alert Table


                                    }
                                    checkSpeedStatus = true;
                                    break;
                                }
                            }

                        }
                        if (checkSpeedStatus == false) {
                            Boolean updateResolve = alertService.updateResolve(vtuLocationDto.get(0).getImei(), OVER_SPEED_ALERT_ID);
                        }

                    }


                }

            }


        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), ex.getMessage(), result);
        }


        return response;

    }

    @RequestMapping("/getAlert")
    public RDVTSAlertResponse getAlert(@RequestParam(name = "userId") Integer userId,
                                       @RequestParam(name = "roadId", required = false) Integer roadId,
                                       @RequestParam(name = "vehicleId", required = false) Integer vehicleId,
                                       @RequestParam(name = "deviceId", required = false) Integer deviceId,
                                       @RequestParam(name = "activityId", required = false) Integer activityId,
                                       @RequestParam(name = "workId", required = false) Integer workId,
                                       @RequestParam(name = "startDate", required = false) String startDate,
                                       @RequestParam(name = "endDate", required = false) String endDate,
                                       @RequestParam(name = "alertTypeId", required = false) Integer alertTypeId,
                                       @RequestParam(name = "circleId", required = false) Integer circleId,
                                       @RequestParam(name = "distId", required = false) Integer distId,
                                       @RequestParam(name = "divisionId", required = false) Integer divisionId,
                                       @RequestParam(name = "start", required = false) Integer start,
                                       @RequestParam(name = "length", required = false) Integer length,
                                       @RequestParam(name = "draw", required = false) Integer draw,
                                       @RequestParam(name = "alertId", required = false) Integer alertId/*,
                                       @RequestParam(name = "", required = false) Integer blockId*/) {
        AlertFilterDto filterDto = new AlertFilterDto();
        filterDto.setUserId(userId);
        filterDto.setRoadId(roadId);
        filterDto.setVehicleId(vehicleId);
        filterDto.setDeviceId(deviceId);
        filterDto.setActivityId(activityId);
        filterDto.setWorkId(workId);
        filterDto.setStartDate(startDate);
        filterDto.setEndDate(endDate);
        filterDto.setAlertTypeId(alertTypeId);
        filterDto.setCircleId(circleId);
        filterDto.setDistId(distId);
        filterDto.setDivisionId(divisionId);
        filterDto.setOffSet(start);
        filterDto.setLimit(length);
        filterDto.setDraw(draw);
        filterDto.setAlertId(alertId);
//        filterDto.setBlockId(blockId);

        RDVTSAlertResponse response = new RDVTSAlertResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            Page<AlertCountDto> alertListToday = alertService.getAlertToday(filterDto);
            Page<AlertCountDto> alertListTotal = alertService.getWorkAlertTotal(filterDto);
            Page<AlertCountDto> vehicleAlertList = alertService.getVehicleAlert(filterDto);
            Page<AlertCountDto> roadAlertList = alertService.getRoadAlert(filterDto);

            List<AlertCountDto> alertList1 = alertListToday.getContent();
            Integer start1 = start;
            for (int i = 0; i < alertList1.size(); i++) {
                start1 = start1 + 1;
                alertList1.get(i).setSlNo(start1);
            }
            List<AlertCountDto> alertList2 = alertListTotal.getContent();
            Integer start2 = start;
            for (int i = 0; i < alertList2.size(); i++) {
                start2 = start2 + 1;
                alertList2.get(i).setSlNo(start2);
            }
            List<AlertCountDto> alertList3 = vehicleAlertList.getContent();
            Integer start3 = start;
            for (int i = 0; i < alertList3.size(); i++) {
                start3 = start3 + 1;
                alertList3.get(i).setSlNo(start3);
            }
            List<AlertCountDto> alertList4 = roadAlertList.getContent();
            Integer start4 = start;
            for (int i = 0; i < alertList4.size(); i++) {
                start4 = start4 + 1;
                alertList4.get(i).setSlNo(start4);
            }

//            result.put("totalAlertToday", alertList1);
            result.put("totalAlertWork", alertList2);
            result.put("vehicleAlert", alertList3);
            result.put("roadAlert", alertList4);
            response.setData(result);
            response.setStatus(1);
            response.setDraw(draw);
//            response.setWorkRecordsFiltered(alertListToday.getTotalElements());
//            response.setWorkRecordsTotal(alertListToday.getTotalElements());

//            response.setWorkRecordsFiltered(alertListTotal.getTotalElements());
//            response.setWorkRecordsTotal(alertListTotal.getTotalElements());
//
//            response.setVehicleRecordsFiltered(vehicleAlertList.getTotalElements());
//            response.setVehicleRecordsTotal(vehicleAlertList.getTotalElements());

//            response.setRoadRecordsFiltered(roadAlertList.getTotalElements());
//            response.setRoadRecordsTotal(roadAlertList.getTotalElements());

            if (alertListTotal != null) {
                response.setRecordsFiltered(alertListTotal.getTotalElements());
                response.setRecordsTotal(alertListTotal.getTotalElements());
            }
            if (vehicleAlertList != null) {
                response.setRecordsFiltered(vehicleAlertList.getTotalElements());
                response.setRecordsTotal(vehicleAlertList.getTotalElements());
            }
            if (roadAlertList != null) {
                response.setRecordsFiltered(roadAlertList.getTotalElements());
                response.setRecordsTotal(roadAlertList.getTotalElements());
            }

            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSAlertResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }
        return response;


    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    @GetMapping("/makeApiCall")
    public Object makeApiCall() throws IOException {
        ResponseEntity<String> responseEntity = null;
        try {
            final String uri = "http://omms.nic.in//api/VTS/VTSAPI";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders header = new HttpHeaders();
            header.set("key", "b3ce5bed5effb926301302b76d3bd98bb22bfc5d6da18b2e5c7017138ca431e9");
            header.set("value", "2ae3ca745c75ba0ac2bc00ef4dfbf709bf281fd6edd846e5536e4f9cad02162d");
            header.set("state", "26");

            HttpEntity<String> requestEntity = new HttpEntity<String>("body", header);
            ResponseEntity<List<ResponseDto>> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<ResponseDto>>() {
            });

            List<ResponseDto> result = response.getBody();




            //Get Unique RoadNAmes
            Set<String> uniqueRoadName = result.stream().map(a -> a.getRoadName()).collect(Collectors.toSet());
            final List<ResponseDto> uniqueRName = distinctList(result, ResponseDto::getRoadName);
            final List<ResponseDto> uniqueRCode = distinctList(result, ResponseDto::getRoadCode);
            final List<ResponseDto> uniqueSanctionDate = distinctList(result, ResponseDto::getSanctionDate);

            //Save Distinct In Road Master
            final List<ResponseDto> distinctRoadList = distinctList(result, ResponseDto::getRoadCode, ResponseDto::getRoadName);
            for (ResponseDto a : distinctRoadList) {
                RoadMasterEntity roadMasterEntity = new RoadMasterEntity();
                RoadMasterEntity roadMasterEntity1 = roadMasterRepository.findByRoadName(a.getRoadName());
                roadMasterEntity.setRoadName(a.getRoadName());
                roadMasterEntity.setRoadCode(a.getRoadCode());
                roadMasterEntity.setSanctionDate(a.getSanctionDate() == "" ? null : convertDateFormat(a.getSanctionDate()));
                roadMasterEntity.setSanctionLength(a.getSanctionLength());
                if (roadMasterEntity1 != null) {
                    roadMasterEntity.setId(roadMasterEntity1.getId());
                }
                roadMasterRepository.save(roadMasterEntity);
            }
////
//            //Save Distinct District In District Master
            Set<String> uniqueDistrictNames = result.stream().map(a -> a.getDistrictName()).collect(Collectors.toSet());
            for (String items : uniqueDistrictNames) {
                GeoDistrictMasterEntity geoDistrictMasterEntity = new GeoDistrictMasterEntity();
                GeoDistrictMasterEntity geoDistrictMasterEntity1 = geoDistrictMasterRepository.existsBygDistrictName(items);
                geoDistrictMasterEntity.setGDistrictName(items);
                if (geoDistrictMasterEntity1 != null) {
                    geoDistrictMasterEntity.setId(geoDistrictMasterEntity1.getId());
                }
                geoDistrictMasterRepository.save(geoDistrictMasterEntity);
            }
//
//           // Save Contractor
            //commented bcz of duploicate data
//            Set<String> uniqueContractorNames = result.stream().map(a -> a.getContractorName()).collect(Collectors.toSet());
//            for (String items : uniqueContractorNames) {
//                ContractorEntity contractorEntity = new ContractorEntity();
//                ContractorEntity contractorEntity1 = contractorMasterRepository.findByName(items);
//                if (contractorEntity1 != null) {
//                    contractorEntity.setId(contractorEntity1.getId());
//                }
//                contractorEntity.setName(items);
//                contractorMasterRepository.save(contractorEntity);
//            }
//
            Set<String> uniquePackageNos = result.stream().map(a -> a.getPackageNo()).collect(Collectors.toSet());
            for (String items : uniquePackageNos) {
                PackageMasterEntity packageMasterEntity = new PackageMasterEntity();
                PackageMasterEntity packageMasterEntity1 = packageMasterRepository.findByPackageNo(items);
                if (packageMasterEntity1 != null) {
                    packageMasterEntity.setId(packageMasterEntity1.getId());
                }
                packageMasterEntity.setPackageNo(items);
                packageMasterRepository.save(packageMasterEntity);
            }
//
//
            Set<String> uniquePiuNames = result.stream().map(a -> a.getPiuName()).collect(Collectors.toSet());
            for (String items : uniquePiuNames) {
                PiuEntity piuEntity = new PiuEntity();
                PiuEntity piuEntity1 = piuRepository.findByName(items);
                if (piuEntity1 != null) {
                    piuEntity.setId(piuEntity1.getId());
                }
                piuEntity.setName(items);
                piuRepository.save(piuEntity);
            }
////
       Set<String> uniqueActivityNames = result.stream().map(a -> a.getActivityName()).collect(Collectors.toSet());
            for (String items : uniqueActivityNames) {
                ActivityEntity activityEntity = new ActivityEntity();
                ActivityEntity activityEntity1 = activityMasterRepository.findByActivityName(items);

                if (activityEntity1 != null) {
                    activityEntity.setId(activityEntity1.getId());
                }
                activityEntity.setActivityName(items);
                activityMasterRepository.save(activityEntity);
            }



            //Trucate before insert
//            geoMappingRepository.truncateMyTable();

            //insert IN Geo mapping
            for (ResponseDto res:result) {
                GeoMappingEntity geoMappingEntity=new GeoMappingEntity();

                geoMappingEntity.setPackageId(packageMasterRepository.findByPackageNo(res.getPackageNo()).getId());
                geoMappingEntity.setPiuId(piuRepository.findByName(res.getPiuName()).getId());
                geoMappingEntity.setRoadId(roadMasterRepository.findByRoadName(res.getRoadName()).getId());
//                geoMappingEntity.setContractorId(contractorMasterRepository.findByName(res.getContractorName()).getId());
                geoMappingEntity.setDistId(geoDistrictMasterRepository.existsBygDistrictName(res.getDistrictName()).getId());
                geoMappingEntity.setActivityId(activityMasterRepository.findByActivityName(res.getActivityName()).getId());
                geoMappingEntity.setActivityQuantity(res.getActivityQuantity());
                geoMappingEntity.setActivityStartDate(res.getActivityStartDate().equals("") ? null : convertDateFormat(res.getActivityStartDate()));
                geoMappingEntity.setActivityCompletionDate(res.getActivityCompletionDate().equals("") ? null : convertDateFormat(res.getActivityCompletionDate()));
                geoMappingEntity.setActualActivityStartDate(res.getActualActivityStartDate().equals("") ? null : convertDateFormat(res.getActualActivityStartDate()));
                geoMappingEntity.setActualActivityCompletionDate(res.getActualActivityCompletionDate().equals("") ? null : convertDateFormat(res.getActualActivityCompletionDate()));
                geoMappingEntity.setActualActivityCompletionDate(res.getActualActivityCompletionDate().equals("") ? null : convertDateFormat(res.getActualActivityCompletionDate()));
                geoMappingEntity.setExecutedQuantity(res.getExecutedQuantity());

                geoMappingEntity.setActivityStatus(1);
                geoMappingEntity.setCompletedRoadLength(res.getCompletedRoadLength());
                geoMappingEntity.setCompletionDate(res.getCompletionDate().equals("") ? null : convertDateFormat(res.getCompletionDate()));
                geoMappingEntity.setPmisFinalizeDate(res.getPMisFinalizeDate().equals("") ? null : convertDateFormat(res.getPMisFinalizeDate()));
                geoMappingEntity.setAwardDate(res.getAwardDate().equals("") ? null : convertDateFormat(res.getAwardDate()));
                geoMappingEntity.setSanctionDate(res.getSanctionDate().equals("") ? null : convertDateFormat(res.getSanctionDate()));
                geoMappingEntity.setStateId(1);
                geoMappingRepository.save(geoMappingEntity);

            }


            return null;


        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<Object>("Please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public Date convertDateFormat(String mydate) throws ParseException {
        Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(mydate);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String parsedDate = formatter.format(initDate);
        Date finalDate = new SimpleDateFormat("yyyy-MM-dd").parse(parsedDate);
        return finalDate;
    }


    public static <T> List<T> distinctList(List<T> list, Function<? super T, ?>... keyExtractors) {

        return list
                .stream()
                .filter(distinctByKeys(keyExtractors))
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {

        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t -> {

            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return seen.putIfAbsent(keys, Boolean.TRUE) == null;

        };

    }


}




