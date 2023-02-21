package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.repository.WorkCronRepository;
import gov.orsac.RDVTS.repositoryImpl.DashboardRepositoryImpl;
import gov.orsac.RDVTS.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/cron")
public class AlertCronController {

    @Autowired
    public DeviceService deviceService;
    @Autowired
    DashboardService dashboardService;
    @Autowired
    private DashboardRepositoryImpl dashboardRepositoryImpl;

    @Autowired
    public LocationService locationService;

    @Autowired
    public AlertService alertService;

    @Autowired
    public WorkCronRepository workCronRepository;

    @Autowired
    public WorkService workService;

    @Autowired
    public VehicleService vehicleService;
    @Autowired
    public RoadService roadService;

    final Integer OVER_SPEED_ALERT_ID = 1;//NO_DATA_ALERT_ID For Alert TYpe Stored in DB
    final Integer NO_DATA_ALERT_ID = 2;//NO_DATA_ALERT_ID For Alert TYpe Stored in DB
    final Integer NO_MOVEMENT_ALERT_ID = 3;
    final Integer GEO_FENCE_ALERT_ID = 4;
    final Integer noDataAlertTimeSpan = 60; //in minutes Alert Time Span
    final Integer NO_MOVEMENT_TIME_GAP = 15; //in minutes
    final Integer LOCATION_DATA_FREQUENCY = 6; //per minute 6 locations are saved
    final Integer OUTSIDE_POINT_COUNT = 5;
    final Integer OVER_SPEED_TIME = 15; //in minutes


    @Scheduled(cron = "0 */5 * * * *")
    public void generateNoDataAlert() {
//       System.out.println("nomove");
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
                            Double speed=0.0;
                            try {
                                speed = Double.parseDouble(locationDto.getSpeed());
                            } catch (NumberFormatException e) {
                                speed = 0.0;
                            }
                            alertEntity.setSpeed(speed);
                            alertEntity.setGpsDtm(currDtTimeParsed);

                            AlertEntity alertEntity1 = alertService.saveAlert(alertEntity);//If Not exist save alert in Alert Table


                        }
                    } else {
                        alertService.updateResolve(item.getImeiNo1(), NO_DATA_ALERT_ID);//set is_resolve True
                    }

                }

            }

        }



    }

    @Scheduled(cron = "0 */5 * * * *")
    public void generateNoMovementAlert() {


//        System.out.println("generateNoMovementAlert");


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


            }

        }



    }

    @Scheduled(cron = "0 */5 * * * *")
    public void generateGeofenceAlert() throws ParseException {
//        System.out.println("generateGeofenceAlert");

        List<WorkDto> workDto = workService.getWorkById(-1);

        Integer userId=1;
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


    }

    @Scheduled(cron = "0 */5 * * * *")
    public void generateOverSpeedAlert() throws ParseException {
//        System.out.println("generateOverSpeedAlert");

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

   @Scheduled(cron = "0 */5 * * * *")
    public RDVTSResponse getActiveAndInactiveVehicleCron() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
             int userId=1;
            List<DashboardCronEntity>  vehicle = dashboardService.getActiveAndInactiveVehicleCron(userId);
            System.out.println("TRUE");

            result.put("vehicle", vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("All vehicleData");

        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }
/*@PostMapping("/getDashboardData")
public RDVTSResponse getDashboardData(@RequestParam(name = "typeId")Integer typeId) {
    RDVTSResponse response = new RDVTSResponse();
    Map<String, Object> result = new HashMap<>();

    try {
        List<DashboardDto> dashboardData = dashboardService.getDashboardData(typeId);
        Integer active=dashboardRepositoryImpl.getActiveData();
        Integer inActive=dashboardRepositoryImpl.getInActiveData();
        Integer total=dashboardRepositoryImpl.getTotalData();
        Double activePercentage=(Double.valueOf(active)/Double.valueOf(total))*100;
        Double inActivePercentage=(Double.valueOf(inActive)/Double.valueOf(total))*100;


        result.put("dashboardData",dashboardData);
        result.put("active",active);
        result.put("inActive",inActive);
        result.put("total",total);
        result.put("activePercentage",activePercentage);
        result.put("inActivePercentage",inActivePercentage);

        response.setData(result);
        response.setStatus(1);
        response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        response.setMessage("Dashboard Data");

    } catch (Exception ex) {
        ex.printStackTrace();
        response = new RDVTSResponse(0,
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                ex.getMessage(),
                result);
    }
    return response;
}*/


    @Scheduled(cron = "0 */1 * * * *")
    public RDVTSResponse getVehicleLocationCornForWork() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            int userId=1;
            Date startDate1 = null;
            Date endDate1 = null;
            Date vehicleStartDate = null;
            Date vehicleEndDate = null;



            List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(-1);
            List<WorkCronEntity> workList=new ArrayList<>();
                //int i = 0;
                for (DeviceDto imei : getImeiList) {
                    Double todayDistance=0.0;
                    Double totalDistance = 0.0;
                    Double totalSpeedWork=0.0;
                    Double avgSpeedToday=0.0;
                    Integer totalActiveVehicle=0;
                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate1, endDate1,null, null);
                    totalActiveVehicle+=locationService.getActiveVehicle(imei.getImeiNo1(), imei.getImeiNo2(), startDate1, endDate1, null, null);
                    totalDistance += locationService.getDistance(imei.getImeiNo1(), imei.getImeiNo2(), startDate1, endDate1, null, null);
                    todayDistance += locationService.getTodayDistance(imei.getImeiNo1(), imei.getImeiNo2(), startDate1, endDate1, null, null);
                    // totalSpeed += locationService.getspeed(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate, vehicleid.getCreatedOn(), vehicleid.getDeactivationDate());
                    List<VtuLocationDto> vtuAvgSpeedToday=locationService.getAvgSpeedToday(imei.getImeiNo1(), imei.getImeiNo2(), startDate1, endDate1, null, null);
                    int i=0;
                    for (VtuLocationDto vtuobj : vtuLocationDto) {
                        i++;
                        try{
                            totalSpeedWork+= Double.parseDouble(vtuobj.getSpeed()) ;

                        }catch(Exception e){
                            totalSpeedWork+= 0.0 ;
                        }


                    }
                    totalSpeedWork=totalSpeedWork/i;
                    int j=0;
                    for (VtuLocationDto vtuTodaySpeedObj:vtuAvgSpeedToday) {
                        j++;
                        avgSpeedToday+=Double.parseDouble(vtuTodaySpeedObj.getSpeed()) ;
                    }
                    avgSpeedToday=avgSpeedToday/j;
                    WorkCronEntity work=new WorkCronEntity();
                    work.setTotalSpeedWork(totalSpeedWork);
                    if(avgSpeedToday.isNaN()){
                        work.setAvgSpeedToday(0.0);
                    }
                    else {
                        work.setAvgSpeedToday(avgSpeedToday);
                    }
                    work.setImeiNo(imei.getImeiNo1());
                    work.setTotalActiveVehicle(totalActiveVehicle);
                    if(todayDistance.isNaN()){
                        work.setTodayDistance(0.0);
                    }
                    else {
                        work.setTodayDistance(totalDistance);
                    }
                    if(todayDistance.isNaN()){
                        work.setTodayDistance(0.0);
                    }
                    else {
                        work.setTodayDistance(todayDistance);
                    }

                    workList.add(work);
                }
             workCronRepository.saveAll(workList);

            System.out.println("TRUE Work");

            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("All WorkData");

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
