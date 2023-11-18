package gov.orsac.RDVTS.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Service
public interface PdfService {
    File generatePdf(HttpServletResponse exportResponse, String packageNumber) throws Exception;
}
