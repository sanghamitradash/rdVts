package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.entities.VTUVendorMasterEntity;
import gov.orsac.RDVTS.service.RoadService;
import org.springframework.beans.factory.annotation.Autowired;
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
                ObjectMapper mapper = new ObjectMapper();
                RoadMasterDto roadMasterDto = mapper.readValue(data, RoadMasterDto.class);
                RoadEntity roadEntity = new RoadEntity();

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
            RoadMasterDto road = roadService.getDeviceById(roadId);
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


}
