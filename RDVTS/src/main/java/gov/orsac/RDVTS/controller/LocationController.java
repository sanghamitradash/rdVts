package gov.orsac.RDVTS.controller;


import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.UserAreaMappingDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/location")
public class LocationController {

    private LocationService locationService;

    @PostMapping("/getLatestLocationRecord")
    public RDVTSResponse getLatestLocationRecord(@RequestParam(name = "deviceId", required = false) Integer deviceId,
                                                 @RequestParam(name = "vehicleId", required = false) Integer vehicleId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            if (deviceId !=null){





            } else if (vehicleId !=null) {



            } else if (deviceId !=null && vehicleId!=null) {



            }
            else {



            }



            response.setMessage("User By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }



}
