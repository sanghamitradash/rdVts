package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DesignationEntity;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.WorkEntity;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.service.ContractorService;
import gov.orsac.RDVTS.service.VehicleService;
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
@RequestMapping("/api/v1/work")
public class WorkController {
    @Autowired
    private WorkService workService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ContractorService contractorService;

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
    public RDVTSListResponse getWorkList(@RequestParam (name = "id", required = false)Integer id,
                                         @RequestParam (name = "userId", required = false)Integer userId,
                                         @RequestParam (name = "workId", required = false)Integer workId,
                                         @RequestParam(name = "start") Integer start,
                                         @RequestParam(name = "length") Integer length,
                                         @RequestParam(name = "draw") Integer draw) {
        WorkDto workDto = new WorkDto();
        workDto.setId(id);
        workDto.setWorkId(workId);
        workDto.setUserId(userId);
        workDto.setOffSet(start);
        workDto.setLimit(length);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<WorkDto> workDtoPage = workService.getWorkList(workDto);
            List<WorkDto> workDtoList = workDtoPage.getContent();
            Integer start1=start;
            for(int i = 0; i < workDtoList.size(); i++){
                start1=start1+1;
                workDtoList.get(i).setSlNo(start1);
            }
            result.put("WorkDtoList", workDtoList);
            response.setData(workDtoList);
            response.setMessage("List of Work.");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(workDtoPage.getTotalElements());
            response.setRecordsTotal(workDtoPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSListResponse(0,
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
            List<WorkDto> workDto = workService.getWorkById(id);
            List<VehicleMasterDto> vehicle = vehicleService.getVehicleHistoryList(id);
            List<LocationDto> location=vehicleService.getLocationArray(id);
            List<AlertDto> alertDtoList=vehicleService.getAlertArray(id);
            List<RoadMasterDto> roadMasterDtoList = vehicleService.getRoadArray(id);
            List<ContractorDto> contractorDtoList = contractorService.getContractorByWorkId(id);

                result.put("contractorDto", contractorDtoList );
                result.put("workDto", workDto);
                result.put("vehicleArray", vehicle);
                result.put("locationArray", location);
                result.put("roadArray", roadMasterDtoList);
                result.put("alertArray", alertDtoList);
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

    @PostMapping("/getWorkDDById")
    public RDVTSResponse getWorkDDById(@RequestParam int id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<WorkDto> workDto = workService.getWorkById(id);

            result.put("workDto", workDto);
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

    @PostMapping("/getActivityByWorkId")
    public RDVTSResponse getActivityByWorkId(@RequestParam int id){
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ActivityDto> activityDtoList = workService.getActivityByWorkId(id);
            result.put("activityDtoList", activityDtoList);
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


}
