package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ActivityWorkMapping;
import gov.orsac.RDVTS.entities.AlertEntity;
import gov.orsac.RDVTS.entities.AlertTypeEntity;
import gov.orsac.RDVTS.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public RDVTSResponse generateNoDataAlert(@RequestParam(name = "userId" ) Integer userId) {
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

    @RequestMapping("/generateNoMovementAlert")
    public RDVTSResponse generateNoMovementAlert(@RequestParam(name = "userId" ) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultabc = new ArrayList<>();


//        final Integer NO_MOVEMENT_ALERT_ID = 2;
//        final Integer NO_MOVEMENT_TIME_GAP = 15; //in minutes
//        final Integer LOCATION_DATA_FREQUENCY = 6; //per minute 6 locations are saved
//        final Integer SEEDING_GAP = 3; //taking point by 3 record gap
//        final Integer OUTSIDE_POINT_COUNT = 5;

        try {
            AlertTypeEntity alertTypeEntity=alertService.getAlertTypeDetails(NO_MOVEMENT_ALERT_ID);
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
    public RDVTSResponse generateGeofenceAlert(@RequestParam(name = "userId" ) Integer userId) {
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
                if ( road.size()>0 && road.get(0).getGeom() != null) {
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
    public RDVTSResponse generateOverSpeedAlert(@RequestParam(name = "userId" ) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultabc = new ArrayList<>();
        //final Integer noDataAlertTimeSpan = 60; //in minutes Alert Time Span
        final Integer OVER_SPEED_ALERT_ID = 5;//NO_DATA_ALERT_ID For Alert TYpe Stored in DB
        final Integer OVER_SPEED_TIME = 15; //in minutes
        final Integer LOCATION_DATA_FREQUENCY = 6; //per minute 6 locations are saved
        Integer recordLimit = OVER_SPEED_TIME * LOCATION_DATA_FREQUENCY;

        try {

           List<AlertDto> alertDto= alertService.getAllDeviceByVehicle();
            for (AlertDto alertDtoItem : alertDto) {
                List<VtuLocationDto> vtuLocationDto=alertService.getAlertLocationOverSpeed(alertDtoItem.getImei(),alertDtoItem.getSpeedLimit());
                if (vtuLocationDto !=null){

                    if (vtuLocationDto.size()>0){
                        Boolean checkSpeedStatus=false;
                        for (VtuLocationDto item: vtuLocationDto) {
                            Boolean checkIsNumeric=isNumeric(item.getSpeed());
                            if (checkIsNumeric){
                                if (Double.parseDouble(item.getSpeed())>alertDtoItem.getSpeedLimit()){
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
                                    checkSpeedStatus=true;
                                    break;
                                }
                            }

                        }
                        if (checkSpeedStatus==false){
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
    public RDVTSAlertResponse getAlert(@RequestParam(name = "userId" ) Integer userId,
                                      @RequestParam(name = "roadId", required = false) Integer roadId,
                                      @RequestParam(name = "vehicleId", required = false) Integer vehicleId,
                                      @RequestParam(name = "deviceId", required = false) Integer deviceId,
                                      @RequestParam(name = "activityId", required = false) Integer activityId,
                                      @RequestParam(name = "workId", required = false) Integer workId,
                                      @RequestParam(name = "startDate", required = false) String startDate,
                                      @RequestParam (name = "endDate", required = false) String endDate,
                                      @RequestParam(name = "alertTypeId", required = false) Integer alertTypeId,
                                      @RequestParam(name = "circleId", required = false) Integer circleId,
                                      @RequestParam(name = "distId", required = false) Integer distId,
                                      @RequestParam(name = "divisionId", required = false) Integer divisionId,
                                      @RequestParam(name = "start", required = false) Integer start,
                                      @RequestParam(name = "length", required = false) Integer length,
                                      @RequestParam(name = "draw", required = false) Integer draw,
                                       @RequestParam(name = "alertId", required = false) Integer alertId){
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

        RDVTSAlertResponse response = new RDVTSAlertResponse();
        Map<String, Object> result = new HashMap<>();

        try {
            Page<AlertCountDto> alertListToday = alertService.getAlertToday(filterDto);
            Page<AlertCountDto> alertListTotal = alertService.getWorkAlertTotal(filterDto);
            Page<AlertCountDto> vehicleAlertList=alertService.getVehicleAlert(filterDto);
            Page<AlertCountDto> roadAlertList = alertService.getRoadAlert(filterDto);

            List<AlertCountDto> alertList1 = alertListToday.getContent();
            Integer start1=start;
            for(int i=0;i<alertList1.size();i++){
                start1=start1+1;
                alertList1.get(i).setSlNo(start1);
            }
            List<AlertCountDto> alertList2 = alertListTotal.getContent();
            Integer start2=start;
            for(int i=0;i<alertList2.size();i++){
                start2=start2+1;
                alertList2.get(i).setSlNo(start2);
            }
            List<AlertCountDto> alertList3 = vehicleAlertList.getContent();
            Integer start3=start;
            for(int i=0;i<alertList3.size();i++){
                start3=start3+1;
                alertList3.get(i).setSlNo(start3);
            }
            List<AlertCountDto> alertList4 = roadAlertList.getContent();
            Integer start4=start;
            for(int i=0;i<alertList4.size();i++){
                start4=start4+1;
                alertList4.get(i).setSlNo(start4);
            }

            result.put("totalAlertToday", alertList1);
            result.put("totalAlertWork", alertList2);
            result.put("vehicleAlert", alertList3);
            result.put("roadAlert", alertList4);
            response.setData(result);
            response.setStatus(1);
            response.setDraw(draw);
            response.setWorkRecordsFiltered(alertListToday.getTotalElements());
            response.setWorkRecordsTotal(alertListToday.getTotalElements());
            response.setWorkRecordsFiltered(alertListTotal.getTotalElements());
            response.setWorkRecordsTotal(alertListTotal.getTotalElements());

            response.setVehicleRecordsFiltered(vehicleAlertList.getTotalElements());
            response.setVehicleRecordsTotal(vehicleAlertList.getTotalElements());

            response.setRoadRecordsFiltered(roadAlertList.getTotalElements());
            response.setRoadRecordsTotal(roadAlertList.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSAlertResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
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
}




