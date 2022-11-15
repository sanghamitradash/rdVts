package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.AlertCountDto;
import gov.orsac.RDVTS.repositoryImpl.AlertRepositoryImpl;
import gov.orsac.RDVTS.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private AlertRepositoryImpl alertRepositoryImpl;

    @Override
    public Integer getTotalAlertToday() {
        Integer count = alertRepositoryImpl.getTotalAlertToday();
        return count;
    }
    @Override
    public Integer getTotalAlertWork() {
        return alertRepositoryImpl.getTotalAlertWork();
    }
}
