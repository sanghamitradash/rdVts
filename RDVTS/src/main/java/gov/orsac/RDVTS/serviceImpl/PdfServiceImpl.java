package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
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





    public File generatePdf(HttpServletResponse exportResponse, AlertFilterDto filter) throws Exception {
        Context context = new Context();
        PackageDto packageDto = alertRepositoryImpl.getPackageById(filter);

        String html = "";
        html += "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns:th=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title>PMGSY VTS - REPORT</title>\n" +
                "<!--    <link rel=\"shortcut icon\" href=\"../assets/images/favicon.ico\">-->\n" +
                "\n" +
                "<!--    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css'/>-->\n" +
                "<!--    <link rel=\"stylesheet\" href=\"../assets/css/style-pdf.css\"/>-->\n" +
                "\n" +
                "    <!-- <link href=\"../assets/css/style.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "    <link href=\"../assets/css/icons.min.css\" rel=\"stylesheet\" type=\"text/css\" /> -->\n" +
                "    <!-- <link href=\"../assets/css/app-dark.min.css\" rel=\"stylesheet\" type=\"text/css\" id=\"dark-style\" /> -->\n" +
                "    <!-- <link href=\"../assets/css/app.min.css\" rel=\"stylesheet\" type=\"text/css\" id=\"light-style\" />  -->\n" +
                "    <style>\n" +
                "         .roadcl{\n" +
                "            border: 1px solid #b9b0b0;\n" +
                "            background-color: lightgrey;\n" +
                "            padding: 4px;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        .package{\n" +
                "        background-color: rgb(181, 180, 180);\n" +
                "    text-align: center;\n" +
                "    padding: 2px;\n" +
                "    margin-bottom: 10px;\n" +
                "  }\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "  .table {\n" +
                "    width: 100%;\n" +
                "    margin-bottom: 20px\n" +
                "    margin-right:2\n" +
                "}\n" +
                "\n" +
                ".table th,\n" +
                ".table td {\n" +
                "    padding: 3px;\n" +
                "    line-height: 20px;\n" +
                "    text-align: center;\n" +
                "    vertical-align: top;\n" +
                "    border-top: 1px solid #ddd\n" +
                "    border: 1px solid black;\n" +
                "    font-family:sans-serif\n" +
                "    font-size: x-small;\n" +
                "}\n" +
                ".table tr:nth-child(even) {\n" +
                "  background-color: #f2f2f2;\n" +
                "}\n" +


                ".table {\n" +
                "            -fs-table-paginate: paginate;\n" +
                "        }\n" +

                "@page {\n" +
                "  @bottom-right {\n" +
                "    content: \"Page \" counter(page) \" of \" counter(pages);\n" +
                "  }\n" +
                "}" +


//                "@page {\n" +
//                "        @bottom-right {\n" +
//                "            content: element(footer);\n" +
//                "        }\n" +
//                "    }"+


                "\n" +
                "\n" +

                ".page-break {\n" +
                "        page-break-after: always;\n" +
                "    }"+
                "    </style>\n" +
                "</head>\n";

        html += "<body>\n" +
                "<div id=\"loadingDiv\" style=\"display: none;\"></div>\n" +
                "<div class=\"container\">\n" +
                "\n" +
                "    <br />\n" +
                "    <div id=\"element-to-print\">\n" +
//                "        <table>\n" +
//                "            <tr>\n" +
//                "                <td style=\"text-align:left;border-style: none;text-align:center;\"><img src=\"../resources/image/logo-pdf.png\" /></td>\n" +
//                "            </tr>\n" +
//                "        </table>\n" +
                "        <div style=\"text-align:center;\">\n" +
                "            <img src=\"../pdf-resources/images/logo-pdf.png\" />\n" +
                "        </div> " +
                "\n" +
                "       <div id=\"package_details\">\n" +
                "            <div class=\"heading package\" ><span><strong>Package Details</strong></span></div>\n" +
                "           <table class=\"table2\" style=\"width:100%;!important\">\n" +
                "\n" +
                "               <tr>\n" +
                "                   <td><strong>Package Name</strong></td>\n" +
                "                   <td>:" + packageDto.getPackageNo() + "</td>\n" +
                "                   <td><strong>PIU Name</strong></td>\n" +
                "                   <td>:" + packageDto.getPiuName() + "</td>\n" +
                "               </tr>\n" +
                "\n" +
                "               <tr >\n" +
                "                   <td><strong>Award Date</strong></td>\n" +
                "                   <td>:" + packageDto.getAwardDateStr() + "</td>\n" +
                "                   <td><strong>PMIS Finalize Date</strong></td>\n" +
                "                   <td>:" + packageDto.getPmisFinalizeDateStr() + "</td>\n" +
                "               </tr>\n" +
                "\n" +
                "               <tr class=\"test\">\n" +
                "                   <td><strong>Work Status</strong></td>\n" +
                "                   <td>:" + packageDto.getWorkStatusName() + "</td>\n" +
                "               </tr>\n" +
                "           </table>\n" +
                "\n" +
                "       </div>\n" +
                "        <br/>\n" +
                "\n";

        boolean vehiclesFound = false;

        html += " <div class=\"heading package\" ><span><strong>Road Details</strong></span></div>\n" +
                "\n" +
                "            <table class=\"table table-striped\" style=\"width:100%;\">\n" +
                "                <thead style=\"background-color: #c3c3c3;\">\n" +
                "\n" +
                "<tr>\n" +
                "                    <th style=\"width:4% ;\">Sl No</th>\n" +
                "                    <th style=\"width:8% ;\">Road Name</th>\n" +
                "                    <th style=\"width:11% ;\">Sanction Date</th>\n" +
                "                    <th style=\"width:12% ;\">Road Length</th>\n" +
                "</tr>\n" +
                "                </thead>\n" +
                "<tbody>\n" +
                "\n";

        List<RoadMasterDto> road = roadRepositoryImpl.getRoadNameByPackageIdAndRoadId(filter.getPackageId(),filter.getRoadId());
        int i = 1;

        for (RoadMasterDto data1 : road) {
            String length = String.valueOf(data1.getSanctionLength());
            if(data1.getSanctionLength() == null){
                length= "N/A";
            }else {
                length = length + " KM";
            }
            html += "<tr>\n" +
                    "                    <td>" + i++ + "</td>\n" +
                    "                    <td style=\"text-align:left;\">" + data1.getRoadName() + "</td>\n" +
                    "                    <td style = \"text-align:left\">" + data1.getSanctionDateStr() + "</td>\n" +
                    "                    <td style=\"text-align:left;\">" + length+ "</td>\n" +
                    "</tr>\n";

        }

        html += "                </tbody>\n" +
                "            </table>\n" +
                "        <br/>\n" +
                "\n" +
                "\n";


        for (RoadMasterDto data1 : road) {
            List<ActivityDto> activity = alertRepositoryImpl.getActivityDetailsByRoadId(packageDto.getPackageId(), data1.getId(), filter.getActivityId());
            html += "  <div class=\"heading package\" ><span><strong>Road Name: " + data1.getRoadName() + "</strong></span></div>\n" +
                    "            <table style=\"width:100%;!important\">\n" +
                    "\n" +
                    "                <tr>\n" +
                    "                    <td><strong>Road Name</strong></td>\n" +
                    "                    <td>:" + data1.getRoadName() + "</td>\n" +
                    "                    <td><strong>Sanction Date</strong></td>\n" +
                    "                    <td>:" + data1.getSanctionDateStr() + "</td>\n" +
                    "                </tr>\n" +
                    "\n" +
                    "                <tr>\n" +
                    "                    <td><strong>Sanction Length</strong></td>\n" +
                    "                    <td>:" + data1.getSanctionLength() + "</td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "        <br/>\n" +
                    "\n" +
                    "\n";

            html += "            <div class=\"heading package\"><span><strong>Activity Details</strong></span></div>\n" +
                    "            <table class=\"table table-striped\" style=\"width:100%;\">\n" +
                    "                <thead style=\"background-color: #c3c3c3;\">\n" +
                    "\n" +
                    "                <tr>\n" +
                    "                    <th style=\"width:4% ;\">Sl No</th>\n" +
                    "                    <th style=\"width:8% ;\">Activity Name</th>\n" +
                    "                    <th style=\"width:11% ;\">Start Date</th>\n" +
                    "                    <th style=\"width:12% ;\">End Date</th>\n" +
                    "                    <th style=\"width:12% ;\">Status</th>\n" +
                    "\n" +
                    "                </tr>\n" +
                    "                </thead>\n" +
                    "                <tbody>\n" +
                    "\n";

            int j = 1;
            for (ActivityDto data : activity) {
                if (data.getActivityStartDateStr() == null) {
                    data.setActivityStartDateStr("N/A");
                }

                if (data.getActivityCompletionDateStr() == null) {
                    data.setActivityCompletionDateStr("N/A");
                }

                html += "<tr>\n" +
                        "                    <td style=\"text-align:left;\">" + j++ + "</td>\n" +
                        "                    <td style=\"text-align:left;\">" + data.getActivityName() + "</td>\n" +
                        "                    <td style = \"text-align:left\">" + data.getActivityStartDateStr() + "</td>\n" +
                        "                    <td style = \"text-align:left\">" + data.getActivityCompletionDateStr() + "</td>\n" +
                        "                    <td style=\"text-align:left;\">" + data.getStatus() + "</td>\n" +
                        "                </tr>\n";
            }


            html += "</tbody>\n" +
                    "</table>\n" +
                    "<br/>\n" +
                    "\n" +
                    "\n";

            for (ActivityDto data : activity) {
                List<VehicleMasterDto> vehicleMasterDtoList = alertRepositoryImpl.getVehicleDetailsByActivityId(packageDto.getPackageId(), data.getId(), data1.getRoadId(), filter.getVehicleId(), data.getStartDate(), data.getEndDate());
//                html += "<div id=\"vehicle_details\">\n" +
//                        "    <div class=\"heading package\"><span><strong>Vehicle Details</strong></span></div>\n"+
//                        "</div>\n" ;

                for (VehicleMasterDto vehicle : vehicleMasterDtoList) {

                    html += "    <div class=\"heading package\"><span><strong>Vehicle Details</strong></span></div>\n" +
                            "    <table style=\"width:100%;\">\n" +
                            "        <tr>\n" +
                            "            <td><strong>Vehicle No</strong></td>\n" +
                            "            <td>:" + vehicle.getVehicleNo() + "</td>\n" +
                            "            <td><strong>Vehicle Type</strong></td>\n" +
                            "            <td>:" + vehicle.getVehicleTypeName() + "</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "            <td><strong>Owner Name</strong></td>\n" +
                            "            <td>:" + vehicle.getOwnerName() + "</td>\n" +
                            "            <td><strong>Speed Limit</strong></td>\n" +
                            "            <td>:" + vehicle.getSpeedLimit() + "</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "            <td><strong>Chassis No</strong></td>\n" +
                            "            <td>:" + vehicle.getChassisNo() + "</td>\n" +
                            "            <td><strong>Engine No</strong></td>\n" +
                            "            <td>:" + vehicle.getEngineNo() + "</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "            <td><strong>IMEI</strong></td>\n" +
                            "            <td>:" + vehicle.getImeiNo1() + "</td>\n" +
                            "        </tr>\n" +
                            "    </table>\n" +
//                                "</div>\n" +
                            "<br/>\n";
                    // Add a page break after the Vehicle Details section
                    html += "<div class=\"page-break\"></div>\n"; // This will create a page break


                    html += " <div class=\"heading package\"><span><strong>Alert Details</strong></span></div>\n" +
                            "                <table class=\"table table-striped\" style=\"width:100%;\">\n" +
                            "                    <thead style=\"background-color: #c3c3c3;\">\n" +
                            "\n" +
                            "                    <tr>\n" +
                            "                        <th style=\"width:4% ;\">Sl No</th>\n" +
                            "                        <th style=\"width:8% ;\">IMEI</th>\n" +
                            "                        <th style=\"width:11% ;\">Alert Type</th>\n" +
                            "                        <th style=\"width:12% ;\">Date</th>\n" +
                            "                        <th style=\"width:12% ;\">Latitude</th>\n" +
                            "                        <th style=\"width:12% ;\">Longitude</th>\n" +
                            "\n" +
                            "                </tr>\n" +
                            "                </thead>\n" +
                            "                <tbody>\n" +
                            "\n";

                    List<AlertDto> alert = alertRepositoryImpl.getAlertDetailsByVehicleId(vehicle.getVehicleId(), filter.getAlertTypeId());
                    int k = 1;
                    for (AlertDto alerts : alert) {

                        html += "<tr>\n" +
                                "    <td style=\"text-align:left;\">" + k++ + "</td>\n" +
                                "    <td style=\"text-align:left;\">" + alerts.getImei() + "</td>\n" +
                                "    <td style=\"text-align:left;\">" + alerts.getAlertTypeName() + "</td>\n" +
                                "    <td style = \"text-align:left\">" + alerts.getGpsDtmStr() + "</td>\n" +
                                "    <td style=\"text-align:left;\">" + alerts.getLatitude() + "</td>\n" +
                                "    <td style=\"text-align:left;\">" + alerts.getLongitude() + "</td>\n" +
                                "    </tr>\n";
                    }

                    html += "</tbody>\n" +
                            "</table>\n" +
                            "<br/>\n" +
                            "\n" +
                            "\n";

                    // Add a page break after the Vehicle Details section
                    html += "<div class=\"page-break\"></div>\n"; // This will create a page break

                }
            }
        }


        html += "    </div>\n" +
                " </div>\n" +
                "</body>\n" +
                "</html>\n";

//    context.setVariable("package", packageDto);
//        context.setVariable("road", roadMasterDtoList);
//        context.setVariable("activity", activityDtoList);
//        context.setVariable("vehicle", vehicleMasterDtoList);
        // String html = templateEngine.process("report", context);
        String pdfName = "report";
        return renderPdf(html, pdfName);
    }
}

