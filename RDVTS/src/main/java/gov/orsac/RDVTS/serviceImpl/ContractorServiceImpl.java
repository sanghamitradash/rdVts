package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.ContractorDto;
import gov.orsac.RDVTS.dto.ContractorFilterDto;
import gov.orsac.RDVTS.entities.ContractorEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.ContractorMasterRepository;
import gov.orsac.RDVTS.repository.ContractorRepository;
import gov.orsac.RDVTS.repositoryImpl.ContractorRepositoryImpl;
import gov.orsac.RDVTS.service.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ContractorServiceImpl implements ContractorService {


    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private ContractorMasterRepository contractorMasterRepository;

    @Autowired
    private ContractorRepositoryImpl contractorRepositoryImpl;


    @Override
    public ContractorEntity createContractor(ContractorEntity contractorEntity) {
        return contractorMasterRepository.save(contractorEntity);
    }



    @Override
    public ContractorEntity updateContractorById(Integer id, ContractorDto contractorDto) {
        ContractorEntity existingContract = contractorMasterRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("contractId", "id", id));
        existingContract.setName(contractorDto.getName());
        existingContract.setAddress(contractorDto.getAddress());
        existingContract.setGContractorId(contractorDto.getGContractorId());
        existingContract.setMobile(contractorDto.getMobile());
        ContractorEntity save = contractorMasterRepository.save(existingContract);
        return save;
    }

    @Override
    public ContractorDto getContractById(Integer contractId) {
        return contractorRepository.getContractById(contractId);
    }

    @Override
    public List<ContractorDto> getContractorDetails(ContractorFilterDto contractor) {
        return contractorRepository.getContractorDetails(contractor);
    }
}
