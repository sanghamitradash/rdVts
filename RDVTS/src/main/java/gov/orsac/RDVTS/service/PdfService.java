package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.AlertFilterDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Service
public interface PdfService {
    File generatePdf(HttpServletResponse exportResponse, AlertFilterDto filter) throws Exception;
}
