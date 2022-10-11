package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ContractorEntity;
import gov.orsac.RDVTS.entities.UserAreaMappingEntity;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/createUser")
    public RDVTSResponse saveUser(@RequestParam(name = "data") String data) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        if(data!=null && !data.isEmpty()){
            Map<String, Object> result = new HashMap<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                UserDto userDto = mapper.readValue(data, UserDto.class);
                if(userDto.getMobile1()==null || userDto.getEmail()== null
                ||userDto.getDesignationId()==null || userDto.getRoleId()==null
                ||userDto.getFirstName()==null || userDto.getUserLevelId()==null){
                    UserEntity savedUser = userService.saveUser(userDto);
                    result.put("user", savedUser);
                    rdvtsResponse.setData(result);
                    rdvtsResponse.setStatus(1);
                    rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
                    rdvtsResponse.setMessage("User Created Successfully!!");
                }else {
                    rdvtsResponse = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.OK),
                            "User First Name, Mobile Number, Email, Designation, User Level, User Role are mandatory!!",
                            result);
                }
            } catch (Exception e) {
                rdvtsResponse = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.OK),
                        e.getMessage(),
                        result);
            }
        }else {
            Map<String, Object> result = new HashMap<>();
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.OK),
                    "No Data found!!",
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/getUserList")
    public RDVTSResponse getUserList(@RequestBody UserDto userDto) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<UserDto> userList = userService.getUserList(userDto);
            result.put("userList", userList);
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

    @PostMapping("/createUserAreaMapping")
    public RDVTSResponse createUserAreaMapping(@RequestBody List<UserAreaMappingEntity> userAreaMapping) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<UserAreaMappingEntity>userArea = userService.createUserAreaMapping(userAreaMapping);
            result.put("userArea", userArea);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage(" User Mapping Created Successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getUserMappingAreaDetails")
    public RDVTSResponse getUserMappingAreaDetails(@RequestBody UserAreaMappingDto userAreaMapping) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {

            Page<UserAreaMappingDto> userAreaMappingList = userService.getUserMappingAreaDetails(userAreaMapping);
            List<UserAreaMappingDto> userAreaList = userAreaMappingList.getContent();
            if (!userAreaList.isEmpty() && userAreaList.size() > 0) {
                result.put("UserAreaList", userAreaList);
                result.put("currentPage", userAreaMappingList.getNumber());
                result.put("totalItems", userAreaMappingList.getTotalElements());
                result.put("totalPages", userAreaMappingList.getTotalPages());
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("UserAreaList", userAreaList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }




    @PostMapping("/login")
    public RDVTSResponse loginUser(@RequestBody UserInfoDto request) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        if (request.getMobile1() != null && request.getPassword() != null && !request.getMobile1().toString().isEmpty() &&
                !request.getPassword().isEmpty()) {
            rdvtsResponse.setStatus(0);
            rdvtsResponse.setMessage("Wrong input provided!!");
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } else {
            try {
                Map<String, Object> result = new HashMap<>();
                UserEntity dbUser = userService.findUserByMobile(request.getMobile1());
                if (dbUser != null && !dbUser.getEmail().isEmpty()) {
                    UserPasswordMasterDto userPasswordMasterDto = userService.getPasswordByUserId(dbUser.getId());
                    boolean verifyuserpassword = encoder.matches(request.getPassword(), userPasswordMasterDto.getPassword());
                    if (verifyuserpassword == true) {

                        UserDto userDto = new UserDto();
                        userDto.setId(dbUser.getId());
                        userDto.setMobile1(dbUser.getMobile1());
                        userDto.setFirstName(dbUser.getFirstName());
                        userDto.setRoleId(dbUser.getRoleId());
                        userDto.setDesignationId(dbUser.getDesignationId());
                        userDto.setContractorId(dbUser.getContractorId());
                        // result.put("user", userDto);
                        // result.put("menuList", parentMenuList);
                        rdvtsResponse.setData(userDto);
                        rdvtsResponse.setStatus(1);
                        rdvtsResponse.setMessage("Login successfully!!");
                        rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    } else {
                        rdvtsResponse.setStatus(0);
                        rdvtsResponse.setMessage("Wrong Password Entered!!");
                        rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    }
                } else {
                    rdvtsResponse.setStatus(0);
                    rdvtsResponse.setMessage("User Not Found!!");
                    rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                }

            } catch (Exception e) {
                rdvtsResponse.setStatus(0);
                rdvtsResponse.setMessage("Something went wrong!! We are working on it!!");
                rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            }
        }
        return rdvtsResponse;
    }


    @PostMapping("/saveUserPassword")
    public RDVTSResponse saveUserPassword(@RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        UserPasswordMasterEntity userPassM = new UserPasswordMasterEntity();
        try {
            UserPasswordMasterDto userPasswordMasterDto = objectMapper.readValue(data, UserPasswordMasterDto.class);

            UserPasswordMasterEntity passwordObj = userService.saveUserPassword(userPasswordMasterDto);

            result.put("saveUser", passwordObj);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Password Created Successfully.");
        } catch (Exception e) {
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

        try {
            UserPasswordMasterDto userPasswordMasterDto = userService.getPasswordByUserId(userId);

            result.put("getPasswordByUserId", userPasswordMasterDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
        } catch (Exception e) {
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

        try {
            UserPasswordMasterDto userPasswordMasterDto = userService.getPasswordById(id);

            result.put("getPasswordByUserId", userPasswordMasterDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
        } catch (Exception e) {
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






