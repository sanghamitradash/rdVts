package gov.orsac.RDVTS.controller;

import gov.orsac.RDVTS.dto.AlertFilterDto;
import gov.orsac.RDVTS.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/pdf")

public class PdfContoller {
    @Autowired
    private PdfService pdfService;
    @PostMapping("/createPdf")
    public void generatePdf(HttpServletResponse exportResponse, @RequestParam(value = "packageNumber",required = false)Integer packageNumber,
                            @RequestParam(value = "roadId",required = false)Integer roadId,
                            @RequestParam(value = "activityId",required = false)Integer activityId,
                            @RequestParam(value = "vehicleId",required = false)Integer vehicleId,
                            @RequestParam(value = "activityStartDate",required = false)String activityStartDate,
                            @RequestParam(value = "activityEndDate",required = false)String activityEndDate)
    {
        try {
            Path file = Paths.get(pdfService.generatePdf(exportResponse,packageNumber, roadId,activityId, vehicleId, activityStartDate,activityEndDate ).getAbsolutePath());
            if(Files.exists(file)){
                exportResponse.setContentType("application/pdf");
                exportResponse.addHeader("Content-Disposition", "attachment; filename"+ file.getFileName());
                Files.copy(file, exportResponse.getOutputStream());
                exportResponse.getOutputStream().flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
