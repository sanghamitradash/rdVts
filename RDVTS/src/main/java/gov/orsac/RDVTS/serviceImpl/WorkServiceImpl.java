package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.WorkDto;
import gov.orsac.RDVTS.entities.WorkEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.WorkRepository;
import gov.orsac.RDVTS.repositoryImpl.WorkRepositoryImpl;
import gov.orsac.RDVTS.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class WorkServiceImpl implements WorkService {
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private WorkRepositoryImpl workRepositoryImpl;

    RDVTSResponse rdvtsResponse = new RDVTSResponse();
    NamedParameterJdbcTemplate namedJdbc;

    @Override
    public WorkEntity addWork(WorkEntity workEntity){
        return workRepository.save(workEntity);
    }

    @Override
    public Page<WorkDto> getWorkList(WorkDto workDto) {
        Page<WorkDto> workDtoPage = workRepositoryImpl.getWorkList(workDto);
        return workDtoPage;
    }

    @Override
    public WorkEntity getWorkById(int id) {
        return workRepository.findById(id);
    }

    @Override
    public WorkEntity updateWork(int id, WorkDto workDto) {
        WorkEntity existingWork = workRepository.findById(id);
        if (existingWork == null) {
            throw new RecordNotFoundException("WorkEntity", "id", id);
        }
        existingWork.setWorkId(workDto.getWorkId());
        existingWork.setWorkName(workDto.getWorkName());
        existingWork.setUpdatedBy(workDto.getUpdatedBy());
        existingWork.setCreatedBy(workDto.getCreatedBy());


        WorkEntity save = workRepository.save(existingWork);
        return save;
    }

}
