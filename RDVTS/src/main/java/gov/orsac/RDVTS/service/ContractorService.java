package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.ContractorDto;
import gov.orsac.RDVTS.dto.ContractorFilterDto;
import gov.orsac.RDVTS.entities.ContractorEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContractorService {

    ContractorEntity createContractor(ContractorEntity contractorEntity);



    ContractorEntity updateContractorById(Integer id, ContractorDto contractorDto);

    List<ContractorDto> getContractById(Integer contractId,Integer userId);

    Page<ContractorDto> getContractorDetails(ContractorFilterDto contractor);
}
