package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.ContractorDto;
import gov.orsac.RDVTS.entities.ContractorEntity;
import org.springframework.data.domain.Page;

public interface ContractorService {

    ContractorEntity createContractor(ContractorEntity contractorEntity);

    Page<ContractorDto> getContractorDetails(ContractorDto contractorDto);

    ContractorEntity updateContractorById(Integer id, ContractorDto contractorDto);
}
