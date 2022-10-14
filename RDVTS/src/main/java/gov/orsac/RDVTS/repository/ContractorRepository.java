package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.ContractorDto;
import gov.orsac.RDVTS.dto.ContractorFilterDto;
import gov.orsac.RDVTS.entities.ContractorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractorRepository {

    ContractorDto getContractById(Integer contractId,Integer userId);

    Page<ContractorDto> getContractorDetails(ContractorFilterDto contractor);
}
