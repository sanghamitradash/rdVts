package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.entities.DesignationEntity;
import gov.orsac.RDVTS.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/master")
public class MasterController {
    @Autowired
    private DesignationService designationService;

    ObjectMapper objectMapper = new ObjectMapper();
    @PostMapping("/saveDesignation")
    public RDVTSResponse saveTender(@RequestBody DesignationEntity designationEntity) throws JsonProcessingException {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try{
            DesignationEntity designationEntity1 = designationService.saveDesignation(designationEntity);
            result.put("designationEntity1",designationEntity1);
            rdvtsResponse.setData(designationEntity1);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setMessage("Designation Entered Successfully");
        }
        catch(Exception e){
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @GetMapping("/getAllDesignation")
    public RDVTSResponse getAllDesignation(){
        return designationService.getAllDesignation();
    }


}
