package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.service.PdfService;
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

@Service
public class PdfServiceImpl implements PdfService {
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
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


    @Override
    public File generatePdf(HttpServletResponse exportResponse, String packageNumber) throws Exception {
        Context context = new Context();
        String html = templateEngine.process("report", context);
        String pdfName = "rdVtsPdf";
        return renderPdf(html, pdfName);
    }
}
