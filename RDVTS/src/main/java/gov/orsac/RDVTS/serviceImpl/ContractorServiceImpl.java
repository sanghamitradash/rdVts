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

import java.util.ArrayList;
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
        existingContract.setIsActive(contractorDto.getIsActive());
        ContractorEntity save = contractorMasterRepository.save(existingContract);
        return save;
    }

    @Override
    public List<ContractorDto> getContractById(Integer contractId,Integer userId) {
        return contractorRepository.getContractById(contractId,userId);
    }

    @Override
    public List<ContractorDto> getContractorByWorkId(Integer workId) {
//        List<ContractorDto> contractorDtoList = new ArrayList<>();
        return contractorRepository.getContractorByWorkId(workId);
    }

    @Override
    public Page<ContractorDto> getContractorDetails(ContractorFilterDto contractor) {
        return contractorRepository.getContractorDetails(contractor);
    }

    @Override
    public List<ContractorDto> getContractorDropDown(Integer userId) {
        return contractorRepository.getContractorDropDown(userId);
    }
}
