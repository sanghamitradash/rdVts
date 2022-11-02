package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.AlertDto;
import org.springframework.stereotype.Service;


public interface AlertService  {
    AlertDto checkAlertExists(Long imei, Integer noDataAlertId);
}
