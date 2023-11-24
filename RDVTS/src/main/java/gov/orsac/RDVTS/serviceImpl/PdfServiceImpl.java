package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.PackageDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.repositoryImpl.AlertRepositoryImpl;
import gov.orsac.RDVTS.repositoryImpl.RoadRepositoryImpl;
import gov.orsac.RDVTS.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PdfServiceImpl implements PdfService {
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    AlertRepositoryImpl alertRepositoryImpl;

    @Autowired
    RoadRepositoryImpl roadRepositoryImpl;

    private static final String PDF_RESOURCES = "/pdf-resources/";

    private File renderPdf(String html, String pdfName) throws Exception {
        File file = File.createTempFile(pdfName, ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 18);
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

    public File generatePdf(HttpServletResponse exportResponse, Integer packageId, Integer roadId, Integer activityId,
                            Integer vehicleId, String activityStartDate, String activityendDate) throws Exception {
        Context context = new Context();
        PackageDto packageDto = alertRepositoryImpl.getPackageById(packageId);
        List<RoadMasterDto> roadMasterDtoList = new ArrayList<>();
        List<ActivityDto> activityDtoList = new ArrayList<>();
        List<VehicleMasterDto> vehicleMasterDtoList = new ArrayList<>();

        if (packageDto != null) {
            roadMasterDtoList=roadRepositoryImpl.getRoadNameByPackageIdAndRoadId(packageId,roadId);
        }
        if (roadMasterDtoList != null && !roadMasterDtoList.isEmpty()) {
            for (RoadMasterDto road : roadMasterDtoList) {
                activityDtoList = alertRepositoryImpl.getActivityDetailsByRoadId(packageId, roadId, activityId);
            }
        }
        if(activityDtoList!=null && !activityDtoList.isEmpty()) {
            for (ActivityDto activity : activityDtoList) {
                //,filter.getRoadId()
                vehicleMasterDtoList = alertRepositoryImpl.getVehicleDetailsByActivityId(packageId, activityId, vehicleId, roadId, activityStartDate, activityendDate);
            }
        }
        context.setVariable("package", packageDto);
        context.setVariable("road", roadMasterDtoList);
        context.setVariable("activity", activityDtoList);
        context.setVariable("vehicle", vehicleMasterDtoList);
        String html = templateEngine.process("report", context);
        String pdfName = "report";
        return renderPdf(html, pdfName);
    }

}


