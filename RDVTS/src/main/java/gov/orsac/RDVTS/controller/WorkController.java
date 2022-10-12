package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.WorkDto;
import gov.orsac.RDVTS.entities.DesignationEntity;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.WorkEntity;
import gov.orsac.RDVTS.service.WorkService;
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
@RequestMapping("/api/v1/master")
public class WorkController {
    @Autowired
    private WorkService workService;

    //Work Master
    @PostMapping("/addWork")
    public RDVTSResponse addWork(@RequestBody WorkEntity work) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            WorkEntity workEntity = workService.addWork(work);

            result.put("workEntity", workEntity);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getWorkList")
    public RDVTSResponse getWorkList(@RequestBody WorkDto workDto) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<WorkDto> workDtoPage = workService.getWorkList(workDto);
            List<WorkDto> workDtoList = workDtoPage.getContent();
            result.put("WorkDtoList", workDtoList);
            result.put("currentPage", workDtoPage.getNumber());
            result.put("totalItems", workDtoPage.getTotalElements());
            result.put("totalPages", workDtoPage.getTotalPages());
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("List of Work.");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getWorkById")
    public RDVTSResponse getWorkById(@RequestParam int id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            WorkEntity workEntity = workService.getWorkById(id);
            if (workEntity != null) {
                result.put("workEntity", workEntity);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("workList", workEntity);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;

    }

    @PostMapping("/updateWork")
    public RDVTSResponse updateDesignation(@RequestParam int id,
                                           @RequestParam(name = "data") String data) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            WorkDto updateWorkDto = mapper.readValue(data, WorkDto.class);
            WorkEntity updateWorkEntity = workService.updateWork(id, updateWorkDto);
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            rdvtsResponse.setMessage("Work Updated Successfully");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }


}
