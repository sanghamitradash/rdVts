package gov.orsac.RDVTS.controller;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.RoleMenuInfo;
import gov.orsac.RDVTS.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;
    @PostMapping("/getActiveAndInactiveVehicle")
    public RDVTSResponse getActiveAndInactiveVehicle() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ActiveAndInactiveVehicleDto vehicle = dashboardService.getActiveAndInactiveVehicle();

                result.put("vehicle", vehicle);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("All vehicleData");

        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

}
