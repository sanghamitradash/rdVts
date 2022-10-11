package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.UserPasswordMasterDto;
import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import gov.orsac.RDVTS.service.UserPaswordMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/usersPassM")
public class UserPaswordMasterController {

    @Autowired
    private UserPaswordMasterService userPaswordMasterService;

    @PostMapping("/saveUserPassword")
    public RDVTSResponse saveUserPassword(@RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        UserPasswordMasterEntity userPassM = new UserPasswordMasterEntity();
        try{
            UserPasswordMasterDto userPasswordMasterDto = objectMapper.readValue(data, UserPasswordMasterDto.class);

            UserPasswordMasterEntity passwordObj = userPaswordMasterService.saveUserPassword(userPasswordMasterDto);

            result.put("saveUser", passwordObj);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Password Created Successfully.");
        } catch(Exception e){
        e.printStackTrace();
        response = new RDVTSResponse(0,
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                e.getMessage(),
                result);
        }
        return response;
    }

    @PostMapping("/getPasswordByUserId")
    public RDVTSResponse getPasswordByUserId(@RequestParam Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            UserPasswordMasterDto userPasswordMasterDto = userPaswordMasterService.getPasswordByUserId(userId);

            result.put("getPasswordByUserId", userPasswordMasterDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
        } catch(Exception e){
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getPasswordById")
    public RDVTSResponse getPasswordById(@RequestParam Integer id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            UserPasswordMasterDto userPasswordMasterDto = userPaswordMasterService.getPasswordById(id);

            result.put("getPasswordByUserId", userPasswordMasterDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
        } catch(Exception e){
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/updatePassword")
    public RDVTSResponse updateUser(@RequestParam Integer userId,
                                    @RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            UserPasswordMasterDto uerPasswordMasterDto = mapper.readValue(data, UserPasswordMasterDto.class);
            UserPasswordMasterEntity updatedPassword = userPaswordMasterService.updateUser(userId, uerPasswordMasterDto);

            result.put("updatePassword", updatedPassword);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Password Updated Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }


}
