package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.AlertDto;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.LocationDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.service.AlertService;
import gov.orsac.RDVTS.service.DeviceService;
import gov.orsac.RDVTS.service.LocationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/generateNoDataAlert")
    public RDVTSResponse generateNoDataAlert(@RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultabc = new ArrayList<>();
        final Integer noDataAlertTimeSpan = 60; //in minutes
        final Integer NO_DATA_ALERT_ID=3;//NO_DATA_ALERT_ID

        try {
            Integer deviceId = -1;
            List<DeviceDto> device = deviceService.getAllDeviceDD(deviceId, userId);
            Map<String,Integer> map =new HashMap<>();
            for (DeviceDto item : device) {


                LocationDto locationDto = locationService.getLastLocationByImei(item.getImeiNo1());
                if (locationDto !=null){
                    Integer noDataAlertStatus = 0;
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();

                    String currDtTime = dateFormat.format(date);


                    if (locationDto.getDateTime() != null && !locationDto.getDateTime().toString().isEmpty()) {
                        String lastLocTime = dateFormat.format(locationDto.getDateTime());

                        Date d1 = null;
                        Date d2 = null;
                        try {
                            d1 = dateFormat.parse(currDtTime);
                            d2 = dateFormat.parse(lastLocTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long diff =  d1.getTime()-d2.getTime();
                        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                        //long diffMinutes = diff / (60 * 1000) % 60;
                        if (diffInMinutes > noDataAlertTimeSpan) {
                            noDataAlertStatus = 1;
                        }
                      // System.out.println(noDataAlertStatus);
                        if(noDataAlertStatus==1){
                            AlertDto alertExists=alertService.checkAlertExists(item.getImeiNo1(),NO_DATA_ALERT_ID);
                            if (alertExists==null){
                                System.out.println("hi");

                            }

//                            $alertExists = $this->checkAlertExists($vmmId, CronMaster::NO_DATA_ALERT_ID);
//                            if($alertExists == ''){
//                                //Generate NoDataAlert and store in db
//                                $alertId    = CronMaster::NO_DATA_ALERT_ID;
//                                $alertSql   = "INSERT INTO  ".CronMaster::DBSCHEMA.".alert_data (vmm_id, alert_id, gps_dtm, longitude, latitude, altitude, accuracy, speed)
//                                Values ('".$vmmId."', '".$alertId."', '".$curr_dateTime."', '".$locRes->longitude."','".$locRes->latitude."','".$locRes->altitude."','".$locRes->accuracy."','".$locRes->speed."') ";
//                                $alertStmt  = $this->db->prepare($alertSql);
//                                $alertStmt->execute();
//                                $lastAlertId  = $this->db->lastInsertId();
//                                error_log("\n ".date('Y-m-d H:i:s').":NODATA: Alert Inserted Id :".$lastAlertId,3,"../error.log");
//                                //return true;
//                            }else{
//                                error_log("\n ".date('Y-m-d H:i:s').":NODATA:alert found",3,"../error.log");
//                            }
                        }

                        else{
//                            $query  = "UPDATE ".CronMaster::DBSCHEMA.".alert_data SET is_resolve = true
//                            WHERE alert_id = ".CronMaster::NO_DATA_ALERT_ID." AND vmm_id = ".$vmmId. " AND is_resolve = false";
//                            $stmt   = $this->db->prepare($query);
//                            $status = $stmt->execute();
//                            if($status){
//                                error_log("\n ".date('Y-m-d H:i:s').":NODATA: No-data alert for VMMID:".$vmmId." is resolved.",3,"../error.log");
//                            }
                            //return true;
                        }



                    }

                }

            }

            result.put("device", device);
            result.put("hh",resultabc);
            response.setData(result);
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



