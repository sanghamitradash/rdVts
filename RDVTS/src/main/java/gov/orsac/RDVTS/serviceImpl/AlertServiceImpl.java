package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.AlertDto;
import gov.orsac.RDVTS.repositoryImpl.AlertRepositoryImpl;
import gov.orsac.RDVTS.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    public AlertRepositoryImpl alertRepository;

    public AlertDto checkAlertExists(Long imei, Integer noDataAlertId){


        return alertRepository.checkAlertExists(imei, noDataAlertId);
    }

}
