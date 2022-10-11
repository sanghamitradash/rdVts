package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
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




    @PostMapping("/login")
    public RDVTSResponse loginUser(@RequestBody UserInfoDto request) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        try {
            Map<String, Object> result = new HashMap<>();
            UserEntity  dbUser = userService.findUserByMobile(request.getMobile1());
            if (dbUser != null && !dbUser.getEmail().isEmpty()) {
                //UserPasswordMasterEntity userpassword = getPasswordByUserId(request.getMobile1());

                //boolean verifyuserpassword = encoder.matches(request.getPassword(), userpassword.getPassword());
                boolean verifyuserpassword = true;
                if (verifyuserpassword == true) {

                    UserDto userDto = new UserDto();
                    userDto.setId(dbUser.getId());

                    result.put("user", userDto);
                   // result.put("menuList", parentMenuList);
                    rdvtsResponse.setData(result);
                    rdvtsResponse.setStatus(1);
                    rdvtsResponse.setMessage("Login successfully !!!");
                    rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                } else {
                    rdvtsResponse.setStatus(0);
                    rdvtsResponse.setMessage("Wrong Password");
                    rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                }
            } else {
                rdvtsResponse.setStatus(0);
                rdvtsResponse.setMessage("User Not Found !!!");
                rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }

        } catch (Exception e) {
            rdvtsResponse.setStatus(0);
            rdvtsResponse.setMessage("");
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return rdvtsResponse;
    }



}






