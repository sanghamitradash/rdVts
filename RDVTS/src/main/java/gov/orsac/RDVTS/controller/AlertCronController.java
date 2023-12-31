package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.repository.WorkCronRepository;
import gov.orsac.RDVTS.repositoryImpl.DashboardRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.VehicleRepositoryImpl;
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
    private VehicleRepositoryImpl vehicleRepository;

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
        WorkDto packageID = new WorkDto();
        for (DeviceDto item : device) {
            //get Last location of the Current Date
            Integer vehicleId = deviceService.getvehicleBydevice(item.getId());
            if (vehicleId != null) {
                //package
                //activity Start and End time

                int packageCount = workService.getPackageByvehicleIdCount(vehicleId);
                if (packageCount > 0) {
                    packageID = workService.getPackageByvehicleId(vehicleId);
                    if (packageID != null) {
                        if (packageID.getStartTime() != null && packageID.getEndTime() != null) {
                            VtuLocationDto locationDto = locationService.getLastLocationByImei(item.getImeiNo1(), packageID.getStartTime(), packageID.getEndTime());
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
                                            Double speed = 0.0;
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
                }
            }
        }

    }

    @Scheduled(cron = "0 */5 * * * *")
    public void generateNoMovementAlert() {


//        System.out.println("generateNoMovementAlert");
        AlertTypeEntity alertTypeEntity = alertService.getAlertTypeDetails(NO_MOVEMENT_ALERT_ID);
        //Get Imei
        List<Long> imei = alertService.getImeiForNoMovement(); //get today all imei
        WorkDto packageID = new WorkDto();
        if (imei.size() > 0) {
            for (Long item : imei) {

                Integer deviceId =  deviceService.getDeviceByImei(item);
                if (deviceId != null) {
                    Integer vehicleId = deviceService.getvehicleBydevice(deviceId);
                    if (vehicleId != null) {
                        int packageCount = workService.getPackageByvehicleIdCount(vehicleId);
                        if(packageCount>0) {
                            packageID = workService.getPackageByvehicleId(vehicleId);
                            if (packageID.getStartTime() != null && packageID.getEndTime() != null){

                                Integer recordLimit = NO_MOVEMENT_TIME_GAP * LOCATION_DATA_FREQUENCY;
                                List<VtuLocationDto> vtuLocationDto = alertService.getLocationRecordByFrequency(item, recordLimit, packageID.getStartTime(), packageID.getEndTime());
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
                }



            }

        }


    }

    @Scheduled(cron = "0 */5 * * * *")
    public void generateGeofenceAlert() throws ParseException {
//        System.out.println("generateGeofenceAlert");

        List<WorkDto> workDto = workService.getWorkById(-1);

        Integer userId = 1;
        //Foreach Work Get Vehicle
        //Foreach Vechicle get Device
        //Foreach device get Imei
        //Foreach Imei Get location Record list

        for (WorkDto Work : workDto) {
            //Foreach work get Road Geom
            List<RoadMasterDto> road = roadService.getRoadByWorkId(Work.getId());
            //Foreach Work Get Activities
            if (road.size() > 0 && road.get(0).getGeom() != null) {
                List<GeoMappingEntity> activityDtoList = workService.getActivityDetailsByWorkId(Work.getId());
                for (GeoMappingEntity activityId : activityDtoList) {
                    //Each activity get vehicles
                    List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getId(),
                            userId, activityId.getActualActivityStartDate(), activityId.getActualActivityCompletionDate());

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

                                    List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(), startDate, endDate,
                                            vehicleid.getCreatedOn(), vehicleid.getDeactivationDate(), recordLimit);
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



            //Foreach Vehicle get Device
            //Foreach device get Imei
            //Foreach Imei Get location Record list

        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void generateOverSpeedAlert() throws ParseException {
//        System.out.println("generateOverSpeedAlert");

        List<AlertDto> alertDto = alertService.getAllDeviceByVehicle();
        WorkDto packageID = new WorkDto();
        for (AlertDto alertDtoItem : alertDto) {

            Integer deviceId =  deviceService.getDeviceByImei(alertDtoItem.getImei());
            if (deviceId != null) {
                Integer vehicleId = deviceService.getvehicleBydevice(deviceId);
                if (vehicleId != null) {
                    int packageCount = workService.getPackageByvehicleIdCount(vehicleId);
                    if (packageCount > 0) {
                        packageID = workService.getPackageByvehicleId(vehicleId);
                        if (packageID.getStartTime() != null && packageID.getEndTime() != null) {


                            List<VtuLocationDto> vtuLocationDto = alertService.getAlertLocationOverSpeed(alertDtoItem.getImei(), alertDtoItem.getSpeedLimit(), packageID.getStartTime(), packageID.getEndTime());
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

    @Scheduled(cron = "0 */1 * * * *")
    public RDVTSResponse getActiveAndInactiveVehicleCron() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            int userId = 1;
            List<DashboardCronEntity> vehicle = dashboardService.getActiveAndInactiveVehicleCron(userId);


            // System.out.println("TRUE");

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

    // WorkByIdCron
    @Scheduled(cron = "0 */1 * * * *")
    public RDVTSResponse getVehicleLocationCornForWork() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            int data = 0;
            List<WorkCronEntity> workList = new ArrayList<>();

            List<WorkDto> workDto = workService.getWorkById(270);
            //System.out.println(workDto.size());

            for (WorkDto workitem : workDto) {
                Date startDate1 = null;
                Date endDate1 = null;
                Date vehicleStartDate = null;
                Date vehicleEndDate = null;
                Double todayDistance = 0.0;
                Double totalDistance = 0.0;
                Double totalSpeedWork = 0.0;
                Double avgSpeedToday = 0.0;
                Integer totalActiveVehicle = 0;
                WorkCronEntity work = new WorkCronEntity();
                //Activity By WorkId
                List<ActivityDto> activityDtoList = workService.getActivityByWorkId(workitem.getPackageId());
                if(activityDtoList != null && activityDtoList.size() > 0){
                    for (ActivityDto activityId : activityDtoList) {
                        //Vehicle By ActivityId
                        List<VehicleActivityMappingDto> veActMapDto = vehicleService.getVehicleByActivityId(activityId.getGeoMappingId(), -1, activityId.getActivityStartDate(), activityId.getActivityCompletionDate());
                        if(veActMapDto != null && veActMapDto.size() > 0){
                            for (VehicleActivityMappingDto vehicleList : veActMapDto) {
                                //Device By VehicleId
                                List<VehicleDeviceMappingDto> getDeviceList = vehicleService.getdeviceListByVehicleId(vehicleList.getVehicleId(), vehicleList.getStartTime(), vehicleList.getEndTime(), null);
                                if(getDeviceList != null && getDeviceList.size() > 0){
                                    for (VehicleDeviceMappingDto deviceListItem : getDeviceList) {
                                        //Imei By DeviceId
                                        List<DeviceDto> getImeiList = deviceService.getImeiListByDeviceId(deviceListItem.getDeviceId());
                                        if(getImeiList != null && getImeiList.size() > 0){
                                            for (DeviceDto imei : getImeiList) {
                                                List<VtuLocationDto> vtuLocationDto = locationService.getLocationrecordList(imei.getImeiNo1(), imei.getImeiNo2(),
                                                        vehicleList.getStartDate(), vehicleList.getEndDate(), deviceListItem.getCreatedOn(), deviceListItem.getDeactivationDate());
                                                totalActiveVehicle += locationService.getActiveVehicle(imei.getImeiNo1(), imei.getImeiNo2(),
                                                        vehicleList.getStartDate(), vehicleList.getEndDate(), deviceListItem.getCreatedOn(), deviceListItem.getDeactivationDate());
                                                totalDistance += locationService.getDistance(imei.getImeiNo1(), imei.getImeiNo2(),
                                                        vehicleList.getStartDate(), vehicleList.getEndDate(), deviceListItem.getCreatedOn(), deviceListItem.getDeactivationDate());
                                                todayDistance += locationService.getTodayDistance(imei.getImeiNo1(), imei.getImeiNo2(),
                                                        vehicleList.getStartDate(), vehicleList.getEndDate(), deviceListItem.getCreatedOn(), deviceListItem.getDeactivationDate());
                                                List<VtuLocationDto> vtuAvgSpeedToday = locationService.getAvgSpeedToday(imei.getImeiNo1(), imei.getImeiNo2(),
                                                        vehicleList.getStartDate(), vehicleList.getEndDate(), deviceListItem.getCreatedOn(), deviceListItem.getDeactivationDate());
                                                int i = 0;
                                                for (VtuLocationDto vtuobj : vtuLocationDto) {
                                                    if(vtuobj.getSpeedOfVehicle() != null){
                                                        i++;
                                                        if (Double.isNaN(Double.valueOf(vtuobj.getSpeedOfVehicle()))) {
                                                            totalSpeedWork += 0.0;
                                                        } else {
                                                            totalSpeedWork += vtuobj.getSpeedOfVehicle();
                                                        }
                                                    }
                                                }
                                                totalSpeedWork = totalSpeedWork / i;
                                                int j = 0;
                                                for (VtuLocationDto vtuTodaySpeedObj : vtuAvgSpeedToday) {
                                                    j++;
                                                    if (Double.isNaN(Double.valueOf(vtuTodaySpeedObj.getSpeed()))) {
                                                        avgSpeedToday += 0.0;
                                                    } else {
                                                        avgSpeedToday += Double.parseDouble(vtuTodaySpeedObj.getSpeed());
                                                    }
                                                }
                                                avgSpeedToday = avgSpeedToday / j;

                                            }
                                        }

                                    }
                                }

                            }
                        }


                    }
                }

                work.setWorkId(workitem.getId());
                work.setTodayDistance(todayDistance);
                if (Double.isNaN(totalSpeedWork)) {
                    work.setTotalSpeedWork(0.0);
                } else {
                    work.setTotalSpeedWork(totalSpeedWork);
                }
                if (Double.isNaN(totalSpeedWork)) {
                    work.setAvgSpeedToday(0.0);
                } else {
                    work.setAvgSpeedToday(avgSpeedToday);
                }


                work.setTotalActiveVehicle(totalActiveVehicle);

                work.setTotalDistance(totalDistance);
                workList.add(work);
            }

            alertService.save(workList);

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


    @Scheduled(cron = "0 */5 * * * *")
    public RDVTSResponse setPoolingStatus() {
        try {
            vehicleRepository.saveVehiclePoolingStatus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public Integer test() {
     return  68;

    }

//    @PostMapping("/test")
@Scheduled(cron = "0 0 */24 * * *")
    public RDVTSResponse saveVehicleTypeWiseHourlyReport() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Integer insert = dashboardService.saveVehicleTypeWiseHourlyReport();
            // System.out.println("TRUE");
            result.put("insert", insert);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage(insert+"Data Inserted");

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



