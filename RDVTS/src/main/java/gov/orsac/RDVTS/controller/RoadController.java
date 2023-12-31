package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.entities.RoadLocationEntity;
import gov.orsac.RDVTS.service.RoadService;
import gov.orsac.RDVTS.service.VehicleService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/road")
public class RoadController {

    @Autowired
    private RoadService roadService;

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/addRoad")
    public RDVTSResponse saveVTUVendor(@RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
            try {
                RoadEntity roadEntity = new RoadEntity();
                ObjectMapper mapper = new ObjectMapper();
                RoadMasterDto roadMasterDto = mapper.readValue(data, RoadMasterDto.class);

                roadEntity = roadService.saveRoad(roadMasterDto);
                result.put("saveRoad", roadEntity);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
                response.setMessage("Road Created Successfully!!");
            } catch (Exception e) {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        e.getMessage(),
                        result);
            }
            return response;
    }

    @PostMapping("/getRoadById")
    public RDVTSResponse getRoadById(@RequestParam(name = "roadId", required = false) Integer roadId,
                                     @RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoadMasterDto> road = roadService.getRoadById(roadId, userId);
            List<AlertCountDto> alertCount = roadService.getAlert(roadId);
            result.put("road", road);
            result.put("alertList", alertCount);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }
//Swarup
    @PostMapping("/getRoadByWorkId")
    public RDVTSResponse getRoadByWorkId(@RequestParam(name = "workId") Integer workId,
                                         @RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoadMasterDto> road = roadService.getRoadByWorkId(workId);
//            result.put("road", road);
            response.setData(road);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road By Work Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    //update completion road status & update
    @PostMapping("/updateRoad")
    public RDVTSResponse updateRoad(@RequestParam Integer id,
                                    @RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            RoadMasterDto roadMasterDto = mapper.readValue(data, RoadMasterDto.class);
            RoadEntity updateRoad = roadService.updateRoad(id, roadMasterDto);

            result.put("updateRoad", updateRoad);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road Updated Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getRoadList")
    public RDVTSListResponse getRoadList(@RequestParam(name = "id",required = false) Integer id,
                                           @RequestParam(name = "userId",required = false) Integer userId,
                                           @RequestParam(name = "roadName",required = false)String roadName,
                                           @RequestParam(name = "roadLength",required = false) Double roadLength,
                                           @RequestParam(name = "roadLocation",required = false)Double roadLocation,
                                           @RequestParam(name = "workIds", required = false) List<Integer> workIds,
                                           @RequestParam(name = "packageId", required = false) List<Integer> packageId,
                                           @RequestParam(name = "contractIds", required = false) List<Integer> contractIds,
                                           @RequestParam(name = "activityids", required = false) List<Integer> activityids,
                                           @RequestParam(name = "divisionIds", required = false) List<Integer> divisionIds,
                                           @RequestParam(name = "distIds", required = false) List<Integer> distIds,
                                           @RequestParam(name = "start") Integer start,
                                           @RequestParam(name = "length") Integer length,
                                           @RequestParam(name = "draw") Integer draw) {
        RoadFilterDto roadFilterDto = new RoadFilterDto();
        roadFilterDto.setId(id);
        roadFilterDto.setUserId(userId);
        roadFilterDto.setRoadName(roadName);
        roadFilterDto.setRoadLength(roadLength);
        roadFilterDto.setRoadLocation(roadLocation);
        roadFilterDto.setWorkIds(workIds);
        roadFilterDto.setPackageIds(packageId);
        roadFilterDto.setContractIds(contractIds);
        roadFilterDto.setActivityIds(activityids);
        roadFilterDto.setDistIds(distIds);
        roadFilterDto.setDivisionIds(divisionIds);
        roadFilterDto.setLimit(length);
        roadFilterDto.setOffSet(start);
        roadFilterDto.setDraw(draw);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<RoadMasterDto> roadPageList = roadService.getRoadList(roadFilterDto);
            List<RoadMasterDto> roadList = roadPageList.getContent();
            List<RoadMasterDto> finalRoadList=new ArrayList<>();
            Integer start1=start;
//            for(RoadMasterDto rd:roadList){
//                start1=start1+1;
//                rd.setSlNo(start1);
//                finalRoadList.add(rd);
//            }

            for(int i=0;i<roadList.size();i++){
                start1=start1+1;
                roadList.get(i).setSlNo(start1);
            }
            //result.put("deviceList", deviceList);
            response.setData(roadList);
            response.setMessage("Road List");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(roadPageList.getTotalElements());
            response.setRecordsTotal(roadPageList.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }
        return response;
    }

    @PostMapping("/getGeomByRoadId")
    public RDVTSResponse getGeomByRoadId(@RequestParam(name = "roadId", required = false) Integer roadId,
                                         @RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoadMasterDto> road = roadService.getGeomByRoadId(roadId, userId);
            result.put("road", road);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Geom By roadId");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getWorkDetailsByRoadId")
    public RDVTSResponse getWorkDetailsByRoadId(@RequestParam(name = "roadId", required = false) Integer roadId,
                                                @RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoadWorkMappingDto> road = roadService.getWorkDetailsByRoadId(roadId, userId);
//            result.put("road", road);
            response.setData(road);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("WorkDetails By roadId");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getRoadByRoadIds")
    public RDVTSResponse getRoadByRoadIds(@RequestParam(name = "roadIds", required = false) List<Integer> id,
                                          @RequestParam(name = "workIds", required = false) List<Integer> workIds,
                                          @RequestParam(name = "distIds", required = false) List<Integer> distIds,
                                          @RequestParam(name = "blockIds", required = false) List<Integer> blockIds,
                                          @RequestParam(name = "vehicleIds", required = false) List<Integer> vehicleIds,
                                          @RequestParam(name = "activityIds", required = false) List<Integer> activityIds,
                                          @RequestParam(name = "deviceIds", required = false) List<Integer> deviceIds,
                                          @RequestParam(name = "packageId",required = false) List<Integer>  packageId,
                                          @RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoadMasterDto> road = roadService.getRoadByRoadIds(id, workIds, distIds, blockIds, vehicleIds, activityIds, deviceIds, userId, packageId);
//            result.put("road", road);
            response.setData(road);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road By roadIds");
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getRoadStatusDD")
    public RDVTSResponse getRoadStatusDD(@RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            RoadStatusDropDownDto roadDD = roadService.getRoadStatusDD(userId);
//            result.put("road", road);
            response.setData(roadDD);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road Dropdown");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/updateGeom")
    public RDVTSResponse updateGeom(@RequestParam Integer roadId,
                                    @RequestParam String geom,
                                    @RequestParam(name = "userId") Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            int updateGeom = roadService.updateGeom(roadId, (new JSONObject(geom)).get("geometry").toString(), userId);
            result.put("updateGeom", updateGeom);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road Updated Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/unassignedRoadDD")
    public RDVTSResponse unassignedActivity(@RequestParam(name = "userId") Integer userId){
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try{
            List<UnassignedRoadDDDto> roadDD = roadService.unassignedRoadDD(userId);
            response.setData(roadDD);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Unassigned Road Dropdown");
        } catch (Exception e){
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/addRoadLocation")
    public RDVTSResponse addRoadLocation(@RequestBody RoadLocationDto roadLocationDto){
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try{
            ObjectMapper mapper = new ObjectMapper();

            List<RoadLocationEntity> roadLocationEntities = roadService.addRoadLocation(roadLocationDto.getRoadId(), roadLocationDto.getRoadLocation(),
                    roadLocationDto.getUserId());

            Integer saveGeom = roadService.saveGeom(roadLocationDto.getRoadId(), roadLocationDto.getRoadLocation(),roadLocationDto.getUserId());

            Integer saveLength = roadService.saveLength(roadLocationDto.getRoadId(), roadLocationDto.getRoadLocation(),roadLocationDto.getUserId());

            result.put("saveRoadLocation", roadLocationEntities);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road Location Saved Successfully!");
        } catch(Exception e){
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getRoadAndVehicleByPackageId")
    public RDVTSResponse getRoadAndVehicleByPackageId(@RequestParam(name = "packageId") Integer packageId,
                                                @RequestParam(name = "userId", required = false) Integer userId)
       /* @RequestParam(name="id", required = false) Integer id)*/
    {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoadMasterDto> road = roadService.getRoadByPackageId(packageId);
            List<RoadMasterDto> vehicle = vehicleService.getVehicleByPackageId(packageId);
            result.put("roadByPackage", road);
            result.put("vehicleByPackage", vehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Road And Vehicle By Package Id");
        }
        catch (Exception ex)
        {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getVehicleByRoadId")
    public RDVTSResponse getVehicleByRoadId(@RequestParam(name = "id") Integer id,
                                            @RequestParam(name="packageId") Integer packageId)
    {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try
        {
            List<RoadMasterDto> vehicleByRoadId = vehicleService.getVehicleByRoadId(id, packageId);
//            result.put("road", road);
            response.setData(vehicleByRoadId);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Vehicle Details By roadId");
        }
        catch (Exception e)
        {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(), result);
        }
        return response;
    }

}