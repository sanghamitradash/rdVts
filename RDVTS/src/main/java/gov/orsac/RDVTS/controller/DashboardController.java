package gov.orsac.RDVTS.controller;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.service.DashboardService;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;
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


}
