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
    public RDVTSResponse getContractById(@RequestParam(name = "contractId", required = false) Integer contractId,
                                         @RequestParam(name = "userId",required = false)Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ContractorDto> contractor = contractorService.getContractById(contractId,userId);
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

    //Contractor Listing

    @PostMapping("/getContractorDetails")
    public RDVTSListResponse getContractorDetails(@RequestParam(name = "userId",required = false) Integer userId,
                                                  @RequestParam(name = "gContractorId",required = false)Integer gContractorId,
                                                  @RequestParam(name = "name",required = false) String name,
                                                  @RequestParam(name = "mobile",required = false)Long mobile,
                                                  @RequestParam(name = "start") Integer start,
                                                  @RequestParam(name = "length") Integer length,
                                                  @RequestParam(name = "draw") Integer draw) {
        ContractorFilterDto contractor = new ContractorFilterDto();
        contractor.setUserId(userId);
        contractor.setGContractorId(gContractorId);
        contractor.setName(name);
        contractor.setMobile(mobile);
        contractor.setLimit(length);
        contractor.setOffSet(start);
        contractor.setDraw(draw);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<ContractorDto> contractorListPage = contractorService.getContractorDetails(contractor);
            List<ContractorDto> contractorList = contractorListPage.getContent();
            Integer start1=start;
            for(int i=0;i<contractorList.size();i++) {
                start1 = start1 + 1;
                contractorList.get(i).setSlNo(start1);
                response.setData(contractorList);
                response.setMessage("contractor List");
                response.setStatus(1);
                response.setDraw(draw);
                response.setRecordsFiltered(Long.valueOf(contractorListPage.getNumberOfElements()));
                response.setRecordsTotal(contractorListPage.getTotalElements());
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    //Contract For DropDown

    @PostMapping("/getContractorDropDown")
    public RDVTSResponse getContractorDropDown() {

        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<ContractorDto> contractor = contractorService.getContractorDropDown();
            result.put("contractor", contractor);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Contractor List");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


}
