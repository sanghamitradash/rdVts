package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RDVTSResponse;
import gov.orsac.RDVTS.entities.DesignationEntity;
import gov.orsac.RDVTS.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/master")
public class MasterController {
    @Autowired
    private MasterService masterService;
    @Autowired
    private DesignationService designationService;

    ObjectMapper objectMapper = new ObjectMapper();

    //Role Master
    @PostMapping("/saveRole")
    public RDVTSResponse saveRole(@RequestBody RoleEntity role) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            RoleEntity savedRole = masterService.saveRole(role);

            result.put("savedRole", savedRole);
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
    @PostMapping("/getRoleByRoleId")
    public RDVTSResponse getRoleByRoleId(@RequestParam Integer roleId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoleDto> roleByRoleId = masterService.getRoleByRoleId(roleId);
            if (!roleByRoleId.isEmpty() && roleByRoleId.size() > 0) {
                result.put("roleByRoleId", roleByRoleId);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("Role By RoleId");
            } else {
                result.put("roleByRoleId", roleByRoleId);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/updateRole")
    public RDVTSResponse updateRole(@RequestParam Integer id, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            RoleEntity updateRole = mapper.readValue(data, RoleEntity.class);
            RoleEntity role = masterService.updateRole(id, updateRole);
            //Role updateRole = masterService.updateRole(updateRole);
            result.put("role", role);
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
    @PostMapping("/getRoleByUserLevelId")
    public RDVTSResponse getRoleByUserLevelId(@RequestParam Integer userLevelId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoleDto> roleByUserLevelId = masterService.getRoleByUserLevelId(userLevelId);
            if (!roleByUserLevelId.isEmpty() && roleByUserLevelId.size() > 0) {
                result.put("RoleByUserLevelId", roleByUserLevelId);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("Role By UserLevelId");
            } else {
                result.put("RoleByUserLevelId", roleByUserLevelId);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    //Menu Master
    @PostMapping("/createMenu")
    public RDVTSResponse saveMenu(@RequestBody MenuEntity menuMaster) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            MenuEntity menuMObj = masterService.saveMenu(menuMaster);
            result.put("menuMaster", menuMObj);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("New Menu Created");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/getAllMenu")
    public RDVTSResponse getAllMenu(@RequestParam Integer userId, @RequestParam Integer menuId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<MenuDto> menu = masterService.getMenu(userId, menuId);
            if (!menu.isEmpty() && menu.size() > 0) {
                result.put("menu", menu);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("All Menu.");
            } else {
                result.put("menu", menu);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/updateMenu")
    public RDVTSResponse updateMenu(@RequestParam int id, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            MenuEntity updateMenu = mapper.readValue(data, MenuEntity.class);
            MenuEntity menuObj = masterService.updateMenu(id, updateMenu);
            result.put("menu", menuObj);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Menu Updated Successfully");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

   //Designation Master
    @PostMapping("/saveDesignation")
    public RDVTSResponse saveDesignation(@RequestBody DesignationEntity designationEntity) throws JsonProcessingException {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try{
            DesignationEntity designationEntity1 = designationService.saveDesignation(designationEntity);
            result.put("designationEntity1",designationEntity1);
            rdvtsResponse.setData(designationEntity1);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setMessage("Designation Entered Successfully");
        }
        catch(Exception e){
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

//    @GetMapping("/getAllDesignation")
//    public RDVTSResponse getAllDesignation(){
//        return designationService.getAllDesignation();
//    }
@PostMapping("/getAllDesignationByUserLevelId")
public RDVTSResponse getAllDesignationByUserLevelId(@RequestParam int userLevelId) {
    RDVTSResponse response = new RDVTSResponse();
    Map<String, Object> result = new HashMap<>();
    try {
        //List<Integer> userIdList = new ArrayList<>();
        List<DesignationEntity> designationList = designationService.getAllDesignationByUserLevelId(userLevelId);
        if (!designationList.isEmpty() && designationList.size() > 0) {
            result.put("designationList", designationList);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } else {
            result.put("designationList", designationList);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            response.setMessage("Record not found.");
        }
    } catch (Exception e) {
        response = new RDVTSResponse(0,
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                e.getMessage(),
                result);
    }
    return response;

    //UserLevel Master
    @PostMapping("/createUserLevel")
    public RDVTSResponse saveUserLevel(@RequestBody UserLevelMaster userLevel) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            UserLevelMaster userLevelObj = masterService.saveUserLevel(userLevel);
            result.put("userLevel", userLevelObj);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("New UserLevel Created");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/updateUserLevel")
    public RDVTSResponse updateUserLevel(@RequestParam int id, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            UserLevelMaster updateUserLevel = mapper.readValue(data, UserLevelMaster.class);
            UserLevelMaster usrLevelObj = masterService.updateUserLevel(id, updateUserLevel);
            result.put("userLevel", usrLevelObj);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("User Level Updated Successfully");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getUserLevelById")
    public RDVTSResponse getUserLevelById(@RequestParam int id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<UserLevelMaster> userLevelList = masterService.getUserLevelById(id);
            if (!userLevelList.isEmpty() && userLevelList.size() > 0) {
                result.put("userLevelById", userLevelList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("userLevelById", userLevelList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getAllUserLevel")
    public RDVTSResponse getAllUserLevel(@RequestParam int userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<UserLevelMaster> userLevelList = masterService.getAllUserLevel(userId);
            if (!userLevelList.isEmpty() && userLevelList.size() > 0) {
                result.put("userLevelList", userLevelList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("userLevelList", userLevelList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    //RoleMenu Master
    @PostMapping("/createRoleMenu")
    public RDVTSResponse saveRoleMenu(@RequestBody RoleMenuDto roleMenuInfo) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoleMenuMaster> roleMenuMObj = masterService.saveRoleMenu(roleMenuInfo);
            result.put("RoleMenu", roleMenuMObj);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("New RoleMenu Created");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }
    @PostMapping("/getAllMenuByRoleId")
    public RDVTSResponse getAllMenuByRoleId(@RequestParam Integer userId, @RequestParam Integer roleId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoleMenuDto> roleMenuType = masterService.getAllMenuByRoleId(userId, roleId);
            if (!roleMenuType.isEmpty() && roleMenuType.size() > 0) {
                result.put("RoleMenu", roleMenuType);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("All RoleMenu");
            } else {
                result.put("RoleMenu", roleMenuType);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                response.setMessage("Record not found.");
            }
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


}

    @PostMapping("/updateDesignation")
    public RDVTSResponse updateDesignation(@RequestParam int id,
                                           @RequestParam(name = "data") String data) {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            DesignationDto updateDesignationDto = mapper.readValue(data, DesignationDto.class);
            DesignationEntity updateDesignationEntity = designationService.updateDesignation(id, updateDesignationDto);
            rdvtsResponse.setData(result);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            rdvtsResponse.setMessage("Designation Updated Successfully");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/activateAndDeactivateDesignation")
    public RDVTSResponse activateAndDeactivateDesignation(@RequestParam Integer id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Boolean res = designationService.activateAndDeactivateDesignation(id);
            if (res) {
                response.setData(result);
                response.setStatus(1);
                response.setMessage("Designation Status Changed Successfully");
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                response.setStatus(0);
                response.setMessage("Something went wrong");
                response.setStatusCode(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(), result);
        }
        return response;
    }

}
