package gov.orsac.RDVTS.controller;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.service.DashboardService;
import gov.orsac.RDVTS.serviceImpl.DashboardServiceImpl;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;

    @Autowired
    DashboardServiceImpl dashboardServiceImpl;

    @PostMapping("/getActiveAndInactiveVehicle")
    public RDVTSResponse getActiveAndInactiveVehicle(@RequestParam(name = "userId")Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            ActiveAndInactiveVehicleDto vehicle = dashboardService.getActiveAndInactiveVehicle(userId);

                result.put("vehicle", vehicle);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("All vehicleData");

        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/getStatusWiseWorkCount")
    public RDVTSResponse getStatusWiseWorkCount(@RequestParam(name = "userId")Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            CompletedAndNotCompletedWorkDto work = dashboardService.getStatusWiseWorkCount(userId);
            result.put("work", work);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("All workData");

        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getStatusWiseRoadCount")
    public RDVTSResponse getStatusWiseRoadCount(@RequestParam(name = "userId")Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            CompletedAndNotCompletedRoadDto road = dashboardService.getStatusWiseRoadCount(userId);
            ActiveAndInactiveVehicleDto vehicle = dashboardService.getActiveAndInactiveVehicle(userId);
            result.put("vehicle", vehicle);
            result.put("road", road);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("All Road Data");

        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getDistrictWiseVehicleCount")
    public RDVTSResponse getDistrictWiseVehicleCount(@RequestParam(name = "userId")Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<String> districtName=new ArrayList<>();
        List<Integer> active=new ArrayList<>();
        List<Integer> inActive=new ArrayList<>();
        List<Integer> count=new ArrayList<>();
       // List<String>geom = new ArrayList<>();
;
        try {
            List<DistrictWiseVehicleDto> vehicle = dashboardService.getDistrictWiseVehicleCount(userId);
            for(DistrictWiseVehicleDto vehicle1:vehicle){
                districtName.add(vehicle1.getDistrictName());
                active.add(vehicle1.getActive());
                inActive.add(vehicle1.getInActive());
                count.add(vehicle1.getActive()+vehicle1.getInActive());
               // geom.add(vehicle1.getGeom());
            }
            //List<Integer>sortedList=vehicle.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            result.put("districtName", districtName);
            //result.put("geom",geom);
            result.put("active", active);
            result.put("inActive", inActive);
            result.put("count",count);
            result.put("vehicle",vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("All DistrictWise Vehicle Count");

        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getDivisionWiseVehicleCount")
    public RDVTSResponse getDivisionWiseVehicleCount(@RequestParam(name = "userId")Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<String> divName=new ArrayList<>();
        List<Integer> active=new ArrayList<>();
        List<Integer> inActive=new ArrayList<>();
        List<Integer> count=new ArrayList<>();
        // List<String>geom = new ArrayList<>();
        ;
        try {
            List<DivisionWiseVehicleDto> vehicle = dashboardService.getDivisionWiseVehicleCount(userId);
            for(DivisionWiseVehicleDto vehicle1:vehicle){
                divName.add(vehicle1.getDivName());
                active.add(vehicle1.getActive());
                inActive.add(vehicle1.getInActive());
                count.add(vehicle1.getActive()+vehicle1.getInActive());
                // geom.add(vehicle1.getGeom());
            }
            result.put("divName", divName);
            //result.put("geom",geom);
            result.put("active", active);
            result.put("inActive", inActive);
            result.put("count",count);
            result.put("vehicle",vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("All Division Vehicle Count");

        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getRoadLengthByDistIdOrPackageId")
    public RDVTSResponse getRoadLengthByDistIdOrPackageId(@RequestParam(required = false)Integer distId,
                                                                                  @RequestParam(required = false)Integer packageId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoadLengthDto> roadLength = dashboardServiceImpl.getRoadLengthByDistIdOrPackageId(distId,packageId);
            result.put("packageWiseRoadLength", roadLength);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("package Wise Road Length ");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getHourlyReportVehicleTypeWise")
    public RDVTSResponse getRoadLengthByDistIdOrPackageId() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<HourlyReportDto> hourlyReport = dashboardServiceImpl.getHourlyReportVehicleTypeWise();
            List<String> type=new ArrayList<>();
            for(int i=0;i<hourlyReport.size();i++){
                if(!type.contains(hourlyReport.get(i).getName())){
                    type.add(hourlyReport.get(i).getName());
                }
            }
            for(int i=0;i<type.size();i++){
                String name=type.get(i);
                List<HourlyReportDto> test=new ArrayList<>();
                for(int j=0; j < hourlyReport.size(); j++){
                    if(name.equals(hourlyReport.get(j).getName())){
                        test.add(hourlyReport.get(j));
                    }
                }
                result.put(name,test);

            }
            System.out.println(result);

            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Vehicle Type wise Hourly Report ");
//            Calendar cal = Calendar.getInstance();
//            System.out.println("Today : " + cal.getTime());
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String format = formatter.format(cal.getTime());
//            System.out.println("New_Today : " + format);
//            // Substract 30 days from the calendar
//            cal.add(Calendar.DATE, -5);
//            System.out.println("5 days ago: " + cal.getTime());
//            String format1 = formatter.format(cal.getTime());
//            System.out.println("New_5days ago : " + format1);
//
//            LocalDate start = LocalDate.parse(format1);
//            LocalDate end = LocalDate.parse(format);
//            List<LocalDate> totalDates = new ArrayList<>();
//            while (!start.isAfter(end)) {
//                totalDates.add(start);
//                start = start.plusDays(-5);
//            }
//            System.out.println("Date Range : " + start);

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }


}
