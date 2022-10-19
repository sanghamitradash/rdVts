package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import gov.orsac.RDVTS.repository.UserPaswordMasterRepo;
import gov.orsac.RDVTS.service.MasterService;
import gov.orsac.RDVTS.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private MasterService masterService;

    @Autowired
    private UserPaswordMasterRepo userPaswordMasterRepo;

    @PostMapping("/createUser")
    public RDVTSResponse saveUser(@RequestParam(name = "userData") String data,
                                  @RequestParam(name = "password") String password,
                                  @RequestParam(name = "userArea",required = false) String userAreaData,
                                  @RequestParam(name = "userId",required = false)Integer userId) {

//        List<UserAreaMappingEntity> userArea;

//        List<UserAreaMappingEntity> userArea;


        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        if (data != null && !data.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                UserDto userDto = mapper.readValue(data, UserDto.class);

                List<UserAreaMappingRequestDTO> userArea = mapper.readValue(userAreaData, mapper.getTypeFactory().constructCollectionType(List.class, UserAreaMappingRequestDTO.class));

                UserPasswordMasterDto userPasswordMasterDto = new UserPasswordMasterDto();


                if (userDto.getMobile1() != null || userDto.getEmail() != null
                        || userDto.getDesignationId() != null || userDto.getRoleId() != null
                        || userDto.getFirstName() != null || userDto.getUserLevelId() != null || password != null ||
                        !userDto.getMobile1().toString().isEmpty()
                        || !userDto.getDesignationId().toString().isEmpty() || !userDto.getRoleId().toString().isEmpty() ||
                        !userDto.getUserLevelId().toString().isEmpty()
                        || !userDto.getEmail().isEmpty() || !userDto.getFirstName().isEmpty() || !password.isEmpty()) {

                    if (userDto.getMobile1().toString().length() != 10) {
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter proper mobile number 1!!",
                                result);
                    } else if (userDto.getMobile2().toString().length() != 10) {
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter proper mobile number 2!!",
                                result);
                    } else if (userArea == null || userArea.size() <= 0) {
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user area!!",
                                result);
                    }
                    else if (userDto.getUserLevelId() == 1
                            && userArea.get(0).getStateId() == null
                            && userArea.get(0).getStateId().toString().isEmpty())
                    {
                        //For State

                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user state!!",
                                result);
                    } else if (userDto.getUserLevelId() == 2
                            && userArea.get(0).getDistId() == null
                            && userArea.get(0).getDistId().toString().isEmpty()) {
                        //District

                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user district!!",
                                result);
                    }
                    else if (userDto.getUserLevelId() == 3
                            && userArea.get(0).getBlockId() == null
                            && userArea.get(0).getBlockId().toString().isEmpty())
                    {

                        //Block
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user block!!",
                                result);
                    }
                    else if (userDto.getUserLevelId() == 4
                            && userArea.get(0).getDivisionId() == null
                            && userArea.get(0).getDivisionId().toString().isEmpty()) {

                        //Division
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user Division!!",
                                result);
                    }
                    else {
                        //Validate Email

                        UserEntity savedUser = userService.saveUser(userDto);

                        userDto.setCreatedBy(savedUser.getId());
                        userDto.setUpdatedBy(savedUser.getId());
                        userService.updateUser(savedUser.getId(), userDto);

                        //Save Password
                        userPasswordMasterDto.setUserId(savedUser.getId());
                        userPasswordMasterDto.setCreatedBy(savedUser.getId());
                        userPasswordMasterDto.setUpdatedBy(savedUser.getId());
                        userPasswordMasterDto.setIsActive(true);
                        userPasswordMasterDto.setPassword(password);
                        UserPasswordMasterEntity passwordObj = userService.saveUserPassword(userPasswordMasterDto);

                        if ( userArea != null && userArea.size() > 0) {
                            //Save User Area Mapping
                            List<UserAreaMappingEntity> userAreaMapping = new ArrayList<>();
                            for (UserAreaMappingRequestDTO item : userArea) {
                                UserAreaMappingEntity umEt = new UserAreaMappingEntity();
                                umEt.setStateId(item.getStateId());
                                umEt.setGStateId(item.getGStateId());
                                umEt.setGDistId(item.getGDistId());
                                umEt.setDistId(item.getDistId());
                                umEt.setGBlockId(item.getGBlockId());
                                umEt.setBlockId(item.getBlockId());
                                umEt.setUserId(savedUser.getId());
                                umEt.setDivisionId(item.getDivisionId());
                                umEt.setCreatedBy(savedUser.getId());
                                umEt.setUpdatedBy(savedUser.getId());
                                umEt.setIsActive(true);
                                userAreaMapping.add(umEt);
                            }


                            List<UserAreaMappingEntity> areaObj = userService.saveUserAreaMapping(savedUser.userLevelId, savedUser.getId(), userAreaMapping);

                            //Return Data
                            UserDto returnDTO = new UserDto();
                            returnDTO.setUserId(savedUser.getId());
                            returnDTO.setDesignationId(savedUser.getDesignationId());
                            returnDTO.setRoleId(savedUser.getRoleId());
                            returnDTO.setUserLevelId(savedUser.getUserLevelId());
                            returnDTO.setFirstName(savedUser.getFirstName());
                            returnDTO.setMiddleName(savedUser.getMiddleName());
                            returnDTO.setLastName(savedUser.getLastName());
                            returnDTO.setMobile1(savedUser.getMobile1());
                            returnDTO.setMobile2(savedUser.getMobile2());
                            returnDTO.setEmail(savedUser.getEmail());
                            returnDTO.setContractorId(savedUser.getContractorId());
                            result.put("user", returnDTO);
                            result.put("UserAreaMapping", areaObj);
                        }
                            rdvtsResponse.setData(result);
                            rdvtsResponse.setStatus(1);
                            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
                            rdvtsResponse.setMessage("User Created Successfully!!");
                        }


                } else {
                    rdvtsResponse = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.OK),
                            "User First Name, Mobile Number, Email, Designation, User Level, User Role, Password are mandatory!!",
                            result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                rdvtsResponse = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.OK),
                        e.getMessage(),
                        result);
            }
        } else {
            Map<String, Object> result = new HashMap<>();
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.OK),
                    "No Data found!!",
                    result);
        }
        return rdvtsResponse;
    }

    //get Function

    @PostMapping("/getUserById")
    public RDVTSResponse getContractById(@RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            UserInfoDto userInfoDto = userService.getUserByUserId(userId);
            List<UserAreaMappingDto> userArea = userService.getUserAreaMappingByUserId(userId);
            result.put("user", userInfoDto);
            result.put("userArea", userArea);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("User By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getUserByMobile")
    public RDVTSResponse getUserByMobile(@RequestParam(name = "mobile", required = false) Long mobile,@RequestParam(name = "userId",required = false)Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            UserDto userDto = userService.getUserBymobile(mobile);
            result.put("user", userDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("User By Id");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getUserList")


    public RDVTSListResponse getUserList(@RequestParam(name = "userId", required = false) Integer userId,
                                         @RequestParam(name = "designationId", required = false) Integer designationId,
                                         @RequestParam(name = "roleId", required = false) Integer roleId,
                                         @RequestParam(name = "contractorId", required = false) Integer contractorId,
                                         @RequestParam(name = "email", required = false) String email,
                                         @RequestParam(name = "mobile1", required = false) Long mobile1,
                                         @RequestParam(name = "start") Integer start,
                                         @RequestParam(name = "length") Integer length,
                                         @RequestParam(name = "draw") Integer draw) {
        RDVTSListResponse response = new RDVTSListResponse();

        UserListRequest userListDto = new UserListRequest();

        userListDto.setUserId(userId);
        userListDto.setDesignationId(designationId);
        userListDto.setRoleId(roleId);
        userListDto.setContractorId(contractorId);
        userListDto.setEmail(email);
        userListDto.setMobile1(mobile1);

        userListDto.setLimit(length);
        userListDto.setOffSet(start);
        userListDto.setDraw(draw);


        Map<String, Object> result = new HashMap<>();
        try {
            Page<UserInfoDto> userInfoDtos = userService.getUserList(userListDto);
            List<UserInfoDto> userList = userInfoDtos.getContent();
            List<UserInfoDto> finalUserList=new ArrayList<>();
            Integer start1=start;
            for(UserInfoDto user:userList){

                start1=start1+1;
                user.setSlNo(start1);
                finalUserList.add(user);
            }
           // result.put("userList", userList);
            response.setData(userList);
            response.setMessage("User List");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(userInfoDtos.getTotalElements());
            response.setRecordsTotal(userInfoDtos.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e) {
            response = new RDVTSListResponse(0,
                    new ResponseEntity<>(HttpStatus.OK),
                    e.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/updateUser")
    public RDVTSResponse updateUser(@RequestParam(name = "userData") String data,
                                    @RequestParam(name = "userArea", required = false) String userAreaData,
    @RequestParam(name = "userId",required = false)Integer userId) {
//        RDVTSResponse rdvtsResponse = new RDVTSResponse();
//        Map<String, Object> result = new HashMap<>();
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            UserDto userDto = mapper.readValue(data, UserDto.class);
//            UserEntity updatedUser = userService.updateUser(id, userDto);
//            //userService.deactivateAreaMapping(updatedUser.getId());
//            //List<UserAreaMapping> userAreaMappingInfo = userService.saveUserAreaMapping(userUpdateRequest.getUserAreaMapping(), updatedUser.getId());
//            result.put("user", updatedUser);
//            //result.put("userAreaMappingInfo", userAreaMappingInfo);
//            rdvtsResponse.setData(result);
//            rdvtsResponse.setStatus(1);
//            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
//            rdvtsResponse.setMessage("User Updated Successfully");
//        } catch (Exception e) {
//            rdvtsResponse = new RDVTSResponse(0,
//                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                    e.getMessage(),
//                    result);
//        }
//        return rdvtsResponse;


        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        if (data != null && !data.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                UserDto userDto = mapper.readValue(data, UserDto.class);

                List<UserAreaMappingRequestDTO> userArea = mapper.readValue(userAreaData, mapper.getTypeFactory().constructCollectionType(List.class, UserAreaMappingRequestDTO.class));

//                UserPasswordMasterDto userPasswordMasterDto = new UserPasswordMasterDto();


                if (userDto.getMobile1() != null || userDto.getEmail() != null
                        || userDto.getDesignationId() != null || userDto.getRoleId() != null
                        || userDto.getFirstName() != null || userDto.getUserLevelId() != null ||
                        !userDto.getMobile1().toString().isEmpty()
                        || !userDto.getDesignationId().toString().isEmpty() || !userDto.getRoleId().toString().isEmpty() ||
                        !userDto.getUserLevelId().toString().isEmpty()
                        || !userDto.getEmail().isEmpty() || !userDto.getFirstName().isEmpty()) {

                    if (userDto.getMobile1().toString().length() != 10) {
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter proper mobile number 1!!",
                                result);
                    } else if (userDto.getMobile2().toString().length() != 10) {
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter proper mobile number 2!!",
                                result);
                    } else if (userArea == null || userArea.size() <= 0) {
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user area!!",
                                result);
                    } else if (userDto.getUserLevelId() == 1
                            && userArea.get(0).getGStateId() == null
                            && userArea.get(0).getGStateId().toString().isEmpty()) {
                        //For State

                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user state!!",
                                result);
                    } else if (userDto.getUserLevelId() == 2
                            && userArea.get(0).getGStateId() == null
                            && userArea.get(0).getGStateId().toString().isEmpty()
                            && userArea.get(0).getGDistId() == null
                            && userArea.get(0).getGDistId().toString().isEmpty()) {
                        //District

                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user district!!",
                                result);
                    } else if (userDto.getUserLevelId() == 3
                            && userArea.get(0).getGStateId() == null
                            && userArea.get(0).getGStateId().toString().isEmpty()
                            && userArea.get(0).getGDistId() == null
                            && userArea.get(0).getGDistId().toString().isEmpty()
                            && userArea.get(0).getGBlockId() == null
                            && userArea.get(0).getGBlockId().toString().isEmpty()) {

                        //Block
                        rdvtsResponse = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter user block!!",
                                result);
                    } else {
                        //Validate Email

//                        UserEntity savedUser = userService.saveUser(userDto);
//
                        userDto.setCreatedBy(userDto.getId());
                        userDto.setUpdatedBy(userDto.getId());
                        UserEntity savedUser = userService.updateUser(userDto.getId(), userDto);
                        userService.deactivateAreaMapping(savedUser.getId());
                        //Save Password
//                        userPasswordMasterDto.setUserId(savedUser.getId());
//                        userPasswordMasterDto.setCreatedBy(savedUser.getId());
//                        userPasswordMasterDto.setUpdatedBy(savedUser.getId());
//                        userPasswordMasterDto.setIsActive(true);
//                        userPasswordMasterDto.setPassword(password);
                        //  UserPasswordMasterEntity passwordObj = userService.saveUserPassword(userPasswordMasterDto);

                        //Save User Area Mapping


                        List<UserAreaMappingEntity> userAreaMappingEntities = new ArrayList<>();
                        for (UserAreaMappingRequestDTO item : userArea) {
                            UserAreaMappingEntity umEt = new UserAreaMappingEntity();
                            umEt.setStateId(item.getStateId());
                            umEt.setGStateId(item.getGStateId());
                            umEt.setGDistId(item.getGDistId());
                            umEt.setDistId(item.getDistId());
                            umEt.setGBlockId(item.getGBlockId());
                            umEt.setBlockId(item.getBlockId());
                            umEt.setUserId(savedUser.getId());
                            umEt.setCreatedBy(savedUser.getId());
                            umEt.setUpdatedBy(savedUser.getId());
                            umEt.setIsActive(true);
                            userAreaMappingEntities.add(umEt);
                        }

                        List<UserAreaMappingEntity> areaObj = userService.createUserAreaMapping(userAreaMappingEntities);

                        //Return Data
                        UserDto returnDTO = new UserDto();
                        returnDTO.setUserId(savedUser.getId());
                        returnDTO.setDesignationId(savedUser.getDesignationId());
                        returnDTO.setRoleId(savedUser.getRoleId());
                        returnDTO.setUserLevelId(savedUser.getUserLevelId());
                        returnDTO.setFirstName(savedUser.getFirstName());
                        returnDTO.setMiddleName(savedUser.getMiddleName());
                        returnDTO.setLastName(savedUser.getLastName());
                        returnDTO.setMobile1(savedUser.getMobile1());
                        returnDTO.setMobile2(savedUser.getMobile2());
                        returnDTO.setEmail(savedUser.getEmail());
                        result.put("user", returnDTO);
                        result.put("UserAreaMapping", areaObj);

                        rdvtsResponse.setData(result);
                        rdvtsResponse.setStatus(1);
                        rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
                        rdvtsResponse.setMessage("User Created Successfully!!");
                    }

                } else {
                    rdvtsResponse = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.OK),
                            "User First Name, Mobile Number, Email, Designation, User Level, User Role, Password are mandatory!!",
                            result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                rdvtsResponse = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.OK),
                        e.getMessage(),
                        result);
            }
        } else {
            Map<String, Object> result = new HashMap<>();
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.OK),
                    "No Data found!!",
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/createUserAreaMapping")
    public RDVTSResponse createUserAreaMapping(@RequestBody List<UserAreaMappingEntity> userAreaMapping) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<UserAreaMappingEntity> userArea = userService.createUserAreaMapping(userAreaMapping);
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

    @PostMapping("/getUserAreaMappingByUserId")
    public RDVTSResponse getUserAreaMappingByUserId(@RequestParam(name = "userId", required = false) Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<UserAreaMappingDto> userArea = userService.getUserAreaMappingByUserId(userId);
            result.put("userArea", userArea);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("UserArea By userId");
        } catch (Exception ex) {
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


    @PostMapping("/updateUserAreaMappingByUserId")
    public RDVTSResponse updateUserAreaMappingByUserId(@RequestParam Integer userId, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserAreaMappingDto userAreaMapping = objectMapper.readValue(data, UserAreaMappingDto.class);
            UserAreaMappingEntity userAreaMappingEntity = userService.updateUserAreaMappingByUserId(userId, userAreaMapping);
            result.put("userAreaMappingEntity", userAreaMappingEntity);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("UserArea Updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/login")
    public RDVTSResponse loginUser(@RequestBody UserInfoDto request) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        if (request.getMobile1() == null && request.getPassword() == null && !request.getMobile1().toString().isEmpty() &&
                !request.getPassword().isEmpty()) {
            rdvtsResponse.setStatus(0);
            rdvtsResponse.setMessage("Wrong input provided!!");
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } else {
            try {
                Map<String, Object> result = new HashMap<>();
                UserEntity dbUser = userService.findUserByMobile(request.getMobile1());
                if (dbUser.isIsactive() == false) {
                    rdvtsResponse.setStatus(0);
                    rdvtsResponse.setMessage("User Is Not Active!!");
                    rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                } else {
                    if (dbUser != null && !dbUser.getEmail().isEmpty()) {
                        UserPasswordMasterDto userPasswordMasterDto = userService.getPasswordByUserId(dbUser.getId());
                        boolean verifyuserpassword = encoder.matches(request.getPassword(), userPasswordMasterDto.getPassword());
                        if (verifyuserpassword == true) {
                            UserInfoDto userInfoDto = userService.getUserByUserId(dbUser.getId());
                            List<ParentMenuInfo> roleMenuType = masterService.getMenuHierarchyByRole(dbUser.getId(), dbUser.getRoleId());
                            List<RoleDto> roleByRoleId = masterService.getRoleByRoleId(dbUser.getRoleId());

                            result.put("user", userInfoDto);
                            result.put("menu", roleMenuType);
                            result.put("access", roleByRoleId.get(0));
                            rdvtsResponse.setData(result);
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
                }


            } catch (Exception e) {
                e.printStackTrace();
                rdvtsResponse.setStatus(0);
                rdvtsResponse.setMessage("Something went wrong!! May be Invalid Username And Password!!");
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
            UserPasswordMasterDto userPasswordMasterDto = mapper.readValue(data, UserPasswordMasterDto.class);
            UserPasswordMasterEntity existingUserId = userPaswordMasterRepo.findByUserId(userId);

//            UserPasswordMasterEntity updatedPassword = userService.updateUserPass(userId, userPasswordMasterDto);
//            result.put("updatePassword", updatedPassword);
//            response.setData(result);
//            response.setStatus(1);
//            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
//            response.setMessage("Password Updated Successfully");

            UserPasswordMasterEntity updatedPassword = new UserPasswordMasterEntity(); /*= userService.updateUserPass(userId, userPasswordMasterDto);*/
//            if(existingUserId.getPassword() == userPasswordMasterDto.getOldPassword()){
            if(encoder.matches(userPasswordMasterDto.getOldPassword(), existingUserId.getPassword())) {
                if (userPasswordMasterDto.getOldPassword().equals(userPasswordMasterDto.getPassword())) {
                    //check if the old and new password are same
                    response.setStatus(0);
                    response.setData(result);
                    response.setMessage("New password shouldn't be same as old password !!!");
                    response.setStatusCode(new ResponseEntity<>(HttpStatus.CONFLICT));
                }
                else {
                    UserPasswordMasterEntity userPasswordMasterEntity = userService.updateUserPass(userId, userPasswordMasterDto);
                    //result.put("updatePassword", updatedPassword);
                    response.setData(result);
                    response.setStatus(1);
                    response.setMessage("Password updated successfully !!!");
                    response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                }
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


//    @PostMapping("/sendOtpToUser")
//    public RDVTSResponse sendOtpToUser(@RequestParam String email) {
//        RDVTSResponse rdvtsResponse = new RDVTSResponse();
//        Map<String, Object> result = new HashMap<>();
//        UserEntity user = userService.findUserByMobileAndEmail(email);
//        if (!user.toString().isEmpty()) {
//
//
//            UserDto userDto = new UserDto();
//            BeanUtils.copyProperties(user, userDto);
//           /* userDto.setId(user.getId());
//            userDto.setFirstName(user.getFirstName());*/
//            Integer otp = userService.sendOtpToUser(userDto);
//            if (otp > 0) {
//                result.put("otp", otp);
//                rdvtsResponse.setData(result);
//                rdvtsResponse.setStatus(1);
//                rdvtsResponse.setMessage("OTP sent successfully !!!");
//                rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
//            } else {
//                rdvtsResponse.setStatus(0);
//                rdvtsResponse.setData(result);
//                rdvtsResponse.setMessage("Something went wrong while sending otp !!!");
//                rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//            }
//            return rdvtsResponse;
//        } else {
//            rdvtsResponse.setStatus(0);
//            rdvtsResponse.setData(result);
//            rdvtsResponse.setMessage("User Not Found !!!");
//            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        }
//        return rdvtsResponse;
//    }

    @PostMapping("/sendOtpToUser")
    public RDVTSResponse sendOtpToUser(@RequestParam Long mobile) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try{
            UserDto userDtos = userService.getUserBymobile(mobile);
            //UserEntity user = userService.findUserByMobileAndEmail(email);
            if (!userDtos.toString().isEmpty()) {

                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(userDtos, userDto);
           /* userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());*/
                Integer otp = userService.sendOtpToUser(userDto);
                if (otp > 0) {
                    result.put("otp", otp);
                    rdvtsResponse.setData(result);
                    rdvtsResponse.setStatus(1);
                    rdvtsResponse.setMessage("OTP sent successfully !!!");
                    rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                } else {
                    rdvtsResponse.setStatus(0);
                    rdvtsResponse.setData(result);
                    rdvtsResponse.setMessage("Something went wrong while sending otp !!!");
                    rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                }
                return rdvtsResponse;
            } else {
                rdvtsResponse.setStatus(0);
                rdvtsResponse.setData(result);
                rdvtsResponse.setMessage("User Not Found !!!");
                rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            e.printStackTrace();
            rdvtsResponse.setStatus(0);
            rdvtsResponse.setMessage("Something went wrong!! May be Invalid Mobile Number!!");
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        }


        return rdvtsResponse;
    }


    @PostMapping("/resetPassword")
    public RDVTSResponse resetUserPassword(@RequestBody(required = false) Map<String, String> param) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();

        //UserEntity userEntity = userService.findUserByMobileAndEmail(param.get("email"));
        UserDto userDtos = userService.getUserBymobile(Long.parseLong(param.get("mobile")));
        UserPasswordMasterDto userPasswordMasterDto = userService.getPasswordByUserId(userDtos.getId());

        if (!userDtos.toString().isEmpty()) {
            if (userDtos.getOtp() == Integer.parseInt(param.get("otp"))) {

                boolean verifyuserpassword = encoder.matches(param.get("password"), userPasswordMasterDto.getPassword());


                if (verifyuserpassword) {
                    rdvtsResponse.setStatus(0);
                    rdvtsResponse.setData(result);
                    rdvtsResponse.setMessage("New password shouldn't be same as old password !!!");
                    rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.CONFLICT));
                } else {

                    userPasswordMasterDto.setPassword(param.get("password"));

                   // UserPasswordMasterEntity updatedPassword = userService.updateUserPass(userDtos.getId(), userPasswordMasterDto);
                    UserPasswordMasterEntity updatedPassword = userService.resetPassword(userDtos.getId(), userPasswordMasterDto);

                    if (!updatedPassword.toString().isEmpty()) {
                        //rdvtsResponse.setData(updatedPassword);
                        rdvtsResponse.setStatus(1);
                        rdvtsResponse.setMessage("Password reset successfully !!!");
                        rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    } else {
                        rdvtsResponse.setStatus(0);
                        rdvtsResponse.setData(result);
                        rdvtsResponse.setMessage("Something went wrong while resetting password !!!");
                        rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                    }
                }

            } else {
                rdvtsResponse.setStatus(0);
                rdvtsResponse.setData(result);
                rdvtsResponse.setMessage("Otp is not Correct Please Check Again !!!");
                rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            }
        } else {
            rdvtsResponse.setStatus(0);
            rdvtsResponse.setData(result);
            rdvtsResponse.setMessage("User Not Found !!!");
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return rdvtsResponse;
    }


}






