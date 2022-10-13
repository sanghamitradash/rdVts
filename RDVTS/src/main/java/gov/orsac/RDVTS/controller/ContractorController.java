package gov.orsac.RDVTS.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ContractorEntity;
import gov.orsac.RDVTS.service.ContractorService;
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
@RequestMapping("/api/v1/contractor")
public class ContractorController {

    @Autowired
    private ContractorService contractorService;


    //Save Contractor

    @PostMapping("/createContractor")
    public RDVTSResponse createContractor(@RequestBody ContractorEntity contractorEntity) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ContractorEntity contractor = contractorService.createContractor(contractorEntity);
            result.put("contractor", contractor);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage(" Contractor Created Successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getContractById")
    public RDVTSResponse getContractById(@RequestParam(name = "contractId", required = false) Integer contractId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ContractorDto contractor = contractorService.getContractById(contractId);
            result.put("contractor", contractor);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Contractor By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    //Contractor View ById, UserId, GContractId

    @PostMapping("/getContractorDetails")
    public RDVTSListResponse getContractorDetails(@RequestParam(name = "userId",required = false) Integer userId,
                                              @RequestParam(name = "gContractorId",required = false)Integer gContractorId,
                                              @RequestParam(name = "start") Integer start,
                                              @RequestParam(name = "length") Integer length,
                                              @RequestParam(name = "draw") Integer draw) {
        ContractorFilterDto contractor = new ContractorFilterDto();
        contractor.setUserId(userId);
        contractor.setGContractorId(gContractorId);
        contractor.setLimit(length);
        contractor.setOffSet(start);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<ContractorDto> contractorListPage = contractorService.getContractorDetails(contractor);
            List<ContractorDto> contractorList = contractorListPage.getContent();
            response.setData(contractorList);
            response.setMessage("contractor List");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(contractorListPage.getTotalElements());
            response.setRecordsTotal(contractorListPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage(), result);
        }
        return response;
    }

    //Update Contract By ID

    @PostMapping("/updateContractorById")
    public RDVTSResponse updateContractorById(@RequestParam Integer id, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ContractorDto contractorDto = objectMapper.readValue(data, ContractorDto.class);
            ContractorEntity contractorEntity = contractorService.updateContractorById(id, contractorDto);
            result.put("contractorEntity", contractorEntity);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Contractor Updated successfully.");
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
