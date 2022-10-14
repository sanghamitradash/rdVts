package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.service.RoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/road")
public class RoadController {

    @Autowired
    private RoadService roadService;

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
    public RDVTSResponse getRoadById(@RequestParam(name = "roadId", required = false) Integer roadId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            RoadMasterDto road = roadService.getRoadById(roadId);
            result.put("road", road);
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
                                           @RequestParam(name = "start") Integer start,
                                           @RequestParam(name = "length") Integer length,
                                           @RequestParam(name = "draw") Integer draw) {
        RoadFilterDto road = new RoadFilterDto();
        road.setId(id);
        road.setUserId(userId);
        road.setRoadName(roadName);
        road.setRoadLength(roadLength);
        road.setRoadLocation(roadLocation);
        road.setLimit(length);
        road.setOffSet(start);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            Page<RoadMasterDto> roadPageList = roadService.getRoadList(road);
            List<RoadMasterDto> roadList = roadPageList.getContent();
            //result.put("deviceList", deviceList);
            response.setData(roadList);
            response.setMessage("Road List");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(roadPageList.getTotalElements());
            response.setRecordsTotal(roadPageList.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }
        return response;
    }


}
