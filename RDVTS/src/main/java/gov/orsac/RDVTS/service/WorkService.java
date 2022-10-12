package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.WorkDto;
import gov.orsac.RDVTS.entities.WorkEntity;
import org.springframework.data.domain.Page;

public interface WorkService {
    WorkEntity addWork (WorkEntity work);
    Page<WorkDto> getWorkList(WorkDto workDto);
    WorkEntity getWorkById(int id);
    WorkEntity updateWork(int id, WorkDto workDto);

}
