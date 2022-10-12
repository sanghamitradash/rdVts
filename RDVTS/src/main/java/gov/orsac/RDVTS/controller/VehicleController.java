package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.service.MasterService;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;
    @PostMapping("/saveVehicle")
    public RDVTSResponse saveVehicle(@RequestBody VehicleMaster vehicle) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            VehicleMaster saveVehicle = vehicleService.saveVehicle(vehicle);
            result.put("saveVehicle", saveVehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/getVehicleByVId")
    public RDVTSResponse getVehicleByVId(@RequestParam Integer vehicleId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            VehicleMasterDto saveVehicle = vehicleService.getVehicleByVId(vehicleId);
            result.put("saveVehicle", saveVehicle);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/updateVehicle")
    public RDVTSResponse updateVehicle(@RequestParam Integer id, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            VehicleMaster updateVehicle = mapper.readValue(data, VehicleMaster.class);
            VehicleMaster vehicle = vehicleService.updateVehicle(id, updateVehicle);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setMessage("Vehicle Update Successfully");
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/getVehicleList")
    public RDVTSResponse getVehicleList(@RequestBody VehicleMasterDto vehicle) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println(vehicle);
            result.put("vehicle", vehicle);
            response.setData(result);
            response.setMessage("Vehicle FilterData");
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

}
