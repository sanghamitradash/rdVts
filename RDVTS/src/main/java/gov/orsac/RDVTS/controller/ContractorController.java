package gov.orsac.RDVTS.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.ContractorDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
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

    //Contractor View ById, UserId, GContractId

    @PostMapping("/getContractorDetails")
    public RDVTSResponse getContractorDetails(@RequestBody ContractorDto contractorDto) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            Page<ContractorDto> contractorListPage = contractorService.getContractorDetails(contractorDto);
            List<ContractorDto> contractorList = contractorListPage.getContent();
            if (!contractorList.isEmpty() && contractorList.size() > 0) {
                result.put("grievanceDtoList", contractorList);
                result.put("currentPage", contractorListPage.getNumber());
                result.put("totalItems", contractorListPage.getTotalElements());
                result.put("totalPages", contractorListPage.getTotalPages());
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("contractorList", contractorList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
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
