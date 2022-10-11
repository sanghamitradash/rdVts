package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.dto.UserPasswordMasterDto;
import gov.orsac.RDVTS.entities.UserEntity;
import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import gov.orsac.RDVTS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/createUser")
    public RDVTSResponse saveUser(@RequestParam(name = "data") String data) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            UserDto userDto = mapper.readValue(data, UserDto.class);
            UserEntity savedUser = userService.saveUser(userDto);
            result.put("user", savedUser);
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            rdvtsResponse.setMessage("User Created Successfully");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/getUserList")
    public RDVTSResponse getUserList(@RequestBody UserDto userDto) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<UserInfoDto> userListPage = userService.getUserList(userDto);
            List<UserInfoDto> userList = userListPage.getContent();
            result.put("userList", userList);
            result.put("currentPage", userListPage.getNumber());
            result.put("totalItems", userListPage.getTotalElements());
            result.put("totalPages", userListPage.getTotalPages());
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            rdvtsResponse.setMessage("List of Users.");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/updateUser")
    public RDVTSResponse updateUser(@RequestParam Integer id,
                                      @RequestParam(name = "data") String data) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            UserDto userDto = mapper.readValue(data, UserDto.class);
            UserEntity updatedUser = userService.updateUser(id, userDto);
            //userService.deactivateAreaMapping(updatedUser.getId());
            //List<UserAreaMapping> userAreaMappingInfo = userService.saveUserAreaMapping(userUpdateRequest.getUserAreaMapping(), updatedUser.getId());
            result.put("user", updatedUser);
            //result.put("userAreaMappingInfo", userAreaMappingInfo);
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            rdvtsResponse.setMessage("User Updated Successfully");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/saveUserPassword")
    public RDVTSResponse saveUserPassword(@RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        UserPasswordMasterEntity userPassM = new UserPasswordMasterEntity();
        try{
            UserPasswordMasterDto userPasswordMasterDto = objectMapper.readValue(data, UserPasswordMasterDto.class);

            UserPasswordMasterEntity passwordObj = userService.saveUserPassword(userPasswordMasterDto);

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
            UserPasswordMasterDto userPasswordMasterDto = userService.getPasswordByUserId(userId);

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
            UserPasswordMasterDto userPasswordMasterDto = userService.getPasswordById(id);

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
    public RDVTSResponse updateUserPass(@RequestParam Integer userId,
                                    @RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            UserPasswordMasterDto uerPasswordMasterDto = mapper.readValue(data, UserPasswordMasterDto.class);
            UserPasswordMasterEntity updatedPassword = userService.updateUserPass(userId, uerPasswordMasterDto);

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






