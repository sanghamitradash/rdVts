package gov.orsac.RDVTS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.MenuEntity;
import gov.orsac.RDVTS.entities.PiuEntity;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.service.GeoMasterService;
import gov.orsac.RDVTS.service.MasterService;
import gov.orsac.RDVTS.service.PiuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Autowired
    private GeoMasterService geoMasterService;

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

    // Designation Master
    @PostMapping("/saveDesignation")
    public RDVTSResponse saveDesignation(@RequestBody DesignationEntity designationEntity) throws JsonProcessingException {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
                if (designationEntity.getName() != null && designationEntity.getDescription() != null && designationEntity.getUserLevelId() != null
                    && designationEntity.getName().isEmpty() && designationEntity.getDescription().isEmpty() && designationEntity.getUserLevelId().toString().isEmpty()) {

                    DesignationEntity designationEntity1 = designationService.saveDesignation(designationEntity);
                    result.put("designationEntity1", designationEntity1);
                    rdvtsResponse.setData(designationEntity1);
                    rdvtsResponse.setStatus(1);
                    rdvtsResponse.setMessage("Designation Entered Successfully");
                } else {
                    rdvtsResponse = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.OK),
                            "Designation Name, Description, User Level are Mandatory !!",
                            result);
                    }
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @PostMapping("/getDesignationList")
    public RDVTSListResponse getDesignationList(@RequestParam (name = "id") Integer id,
                                                @RequestParam(name = "start") Integer start,
                                                @RequestParam(name = "length") Integer length,
                                                @RequestParam(name = "draw") Integer draw ) {
        DesignationDto designationDto = new DesignationDto();
        designationDto.setId(id);
        designationDto.setOffSet(start);
        designationDto.setLimit(length);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            Page<DesignationDto> designationDtoPage = designationService.getDesignationList(designationDto);
            List<DesignationDto> designationDtoList = designationDtoPage.getContent();
            result.put("DesignationDtoList", designationDtoList);
            response.setData(result);
            response.setMessage("List of Designation.");
            response.setStatus(1);
            response.setDraw(draw);
            response.setRecordsFiltered(designationDtoPage.getTotalElements());
            response.setRecordsTotal(designationDtoPage.getTotalElements());
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSListResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getAllDesignationByUserLevelId")
    public RDVTSResponse getAllDesignationByUserLevelId(@RequestParam int userLevelId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            //List<Integer> userIdList = new ArrayList<>();
            List<DesignationDto> designationList = designationService.getAllDesignationByUserLevelId(userLevelId);
            if (!designationList.isEmpty() && designationList.size() > 0) {
                result.put("designationList", designationList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
                response.setMessage("Designation By UserLevelId");
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
    }

    @PostMapping("/getDesignationById")
    public RDVTSResponse getDesignationById(@RequestParam int id) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            DesignationEntity designationEntity = designationService.getDesignationById(id);
            if (designationEntity != null) {
                result.put("designationEntity", designationEntity);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("designationList", designationEntity);
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
            List<RoleMenuInfo> roleMenuType = masterService.getAllMenuByRoleId(userId, roleId);

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

    @PostMapping("/getMenuByRoleId")
    public RDVTSResponse getMenuByRoleId(@RequestParam Integer userId, @RequestParam Integer roleId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<RoleMenuInfo> roleMenuType = masterService.getMenuByRoleId(userId, roleId);

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

    @PostMapping("/getMenuHierarchy")
    public RDVTSResponse getMenuHierarchy(@RequestParam Integer userId,
                                            @RequestParam(name = "roleId", defaultValue = "0", required = false) Integer roleId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        List<ParentMenuInfo> parentMenuList = new ArrayList<>();
        try {
            if (roleId > 0) {
                parentMenuList = masterService.getMenuHierarchyByRole(userId, roleId);
            } else {
                parentMenuList = masterService.getMenuHierarchyWithoutRoleId(userId);
            }
            result.put("menuList", parentMenuList);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Menu Information");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/updateRoleMenu")
    public RDVTSResponse updateRoleMenu(@RequestParam Integer roleId, @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            RoleMenuDto roleMenuDto = mapper.readValue(data, RoleMenuDto.class);
            List<RoleMenuInfo> roleMenuType = masterService.getAllMenuByRoleIds(0, roleId);
            for (RoleMenuInfo item : roleMenuType) {
                if (!roleMenuDto.getMenuId().contains(item.getMenuId())) {
                    // Deactivate the menu
                    masterService.deactivateMenu(roleId, item.getMenuId(), false);
                }
            }
            for (Integer item : roleMenuDto.getMenuId()) {
                boolean hasAvailable = false;
                for (RoleMenuInfo itemRM : roleMenuType) {
                    if (itemRM.getMenuId() == item) {
                        hasAvailable = true;
                        //Set Menu Active
                        masterService.deactivateMenu(roleId, item,true);
                    }
                }
                if (!hasAvailable) {
                    //save
                    roleMenuDto.setRoleId(roleId);
                    RoleMenuMaster roleMenuMObj = masterService.updateRoleMenu(roleMenuDto,item);
                }
            }
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
            response.setMessage("Role Menu Updated Successfully");
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    // PIU Master
    @Autowired
    private PiuService piuService;

    @PostMapping("/savePiu")
    public RDVTSResponse savePiu(@RequestBody PiuEntity piuEntity) throws JsonProcessingException {
        RDVTSResponse rdvtsResponse = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            PiuEntity piuEntity1 = piuService.savePiu(piuEntity);
            result.put("piuEntity1", piuEntity1);
            rdvtsResponse.setData(piuEntity1);
            rdvtsResponse.setStatus(1);
            rdvtsResponse.setMessage("PIU Entered Successfully");
        } catch (Exception e) {
            rdvtsResponse = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return rdvtsResponse;
    }

    @GetMapping("/getAllPiu")
    public RDVTSResponse getAllPiu() {
        return piuService.getAllPiu();
    }

//    @PostMapping("/updatePiu")
//    public RDVTSResponse updatePiu(@RequestParam int id,
//                                           @RequestParam(name = "data") String data) {
//        RDVTSResponse rdvtsResponse = new RDVTSResponse();
//        Map<String, Object> result = new HashMap<>();
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            PiuDto updatePiuDto = mapper.readValue(data, PiuDto.class);
//            PiuEntity updatePiuEntity = piuService.updatePiu(id, updatePiuDto);
//            rdvtsResponse.setData(result);
//            rdvtsResponse.setStatus(1);
//            rdvtsResponse.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
//            rdvtsResponse.setMessage("PIU data Updated Successfully");
//        } catch (Exception e) {
//            rdvtsResponse = new RDVTSResponse(0,
//                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
//                    e.getMessage(),
//                    result);
//        }
//        return rdvtsResponse;
//    }


    @PostMapping("/createVTUVendor")
    public RDVTSResponse saveVTUVendor(@RequestParam String data){
        RDVTSResponse response = new RDVTSResponse();
        if (data != null && !data.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                VTUVendorMasterDto vendorMasterDto = mapper.readValue(data, VTUVendorMasterDto.class);
                VTUVendorMasterEntity vendorMasterEntity = new VTUVendorMasterEntity();

                if (vendorMasterDto.getVtuVendorName() != null && vendorMasterDto.getVtuVendorAddress() != null && vendorMasterDto.getVtuVendorPhone() != null &&
                        vendorMasterDto.getCustomerCareNumber() != null && !vendorMasterDto.getVtuVendorName().isEmpty() && !vendorMasterDto.getVtuVendorAddress().isEmpty()
                        && !vendorMasterDto.getVtuVendorPhone().toString().isEmpty() && !vendorMasterDto.getCustomerCareNumber().toString().isEmpty()) {

                    if (vendorMasterDto.getVtuVendorPhone().toString().length() == 10 && vendorMasterDto.getCustomerCareNumber().toString().length() == 10) {
                       vendorMasterEntity = masterService.saveVTUVendor(vendorMasterDto);
                        result.put("saveVendor", vendorMasterEntity);
                        response.setData(result);
                        response.setStatus(1);
                        response.setStatusCode(new ResponseEntity<>(HttpStatus.CREATED));
                        response.setMessage("VTUVendor Created Successfully!!");
                    } else {
                        response = new RDVTSResponse(0,
                                new ResponseEntity<>(HttpStatus.OK),
                                "Please enter proper phone number or customer care number!!",
                                result);
                    }
                } else {
                    response = new RDVTSResponse(0,
                            new ResponseEntity<>(HttpStatus.OK),
                            "Vendor Name, Address, Phone Number, Customer Care Number are mandatory!!",
                            result);
                }

            } catch (Exception e) {
                response = new RDVTSResponse(0,
                        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                        e.getMessage(),
                        result);
            }
        } else {
            Map<String, Object> result = new HashMap<>();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.OK),
                    "No Data found!!",
                    result);
        }
        return response;
    }

    @PostMapping("/getVTUVendorById")
    public RDVTSResponse getVTUVendorById(@RequestParam Integer id,Integer userId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<VTUVendorMasterDto> vtuVendorMasterDto = masterService.getVTUVendorById(id, userId);

            result.put("getVTUVendorById", vtuVendorMasterDto);
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

    @PostMapping("/getVTUVendorList")
    public RDVTSListResponse getVTUVendorList(@RequestParam(name = "vendorId", required = false) Integer vendorId,
                                              @RequestParam(name = "userId", required = false) Integer userId,
                                              @RequestParam(name = "deviceId", required = false) Integer deviceId,
                                              @RequestParam(name = "vtuVendorName", required = false) String vtuVendorName,
                                              @RequestParam(name = "start") Integer start,
                                              @RequestParam(name = "length") Integer length,
                                              @RequestParam(name = "draw") Integer draw) {

        VTUVendorFilterDto vtuVendorFilterDto = new VTUVendorFilterDto();
        vtuVendorFilterDto.setVendorId(vendorId);
        vtuVendorFilterDto.setUserId(userId);
        vtuVendorFilterDto.setDeviceId(deviceId);
        vtuVendorFilterDto.setVtuVendorName(vtuVendorName);
        vtuVendorFilterDto.setOffSet(start);
        vtuVendorFilterDto.setLimit(length);
        RDVTSListResponse response = new RDVTSListResponse();
        Map<String, Object> result = new HashMap<>();
        try{
            Page<VTUVendorMasterDto> vendorListPage = masterService.getVTUVendorList(vtuVendorFilterDto);
            List<VTUVendorMasterDto> vendorList = vendorListPage.getContent();
//            if (!vendorList.isEmpty() && vendorList.size() > 0) {
//                result.put("vendorList", vendorList);
                response.setData(vendorList);
                response.setMessage("Vendor List");
                response.setStatus(1);
                response.setDraw(draw);
                response.setRecordsFiltered(vendorListPage.getTotalElements());
                response.setRecordsTotal(vendorListPage.getTotalElements());
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
//            } else {
//                result.put("vendorList", vendorList);
//                response.setData(result);
//                response.setStatus(1);
//                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//                response.setMessage("Record not found.");
//            }
        } catch (Exception e) {
            response = new RDVTSListResponse(0, new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),e.getMessage(),result);
        }
        return response;
    }

    @PostMapping("/updateVTUVendor")
    public RDVTSResponse updateVTUVendor(@RequestParam Integer id,
                                        @RequestParam String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            VTUVendorMasterDto vtuVendorMasterDto = mapper.readValue(data, VTUVendorMasterDto.class);
            VTUVendorMasterEntity updateVendor = masterService.updateVTUVendor(id, vtuVendorMasterDto);

            result.put("updateVTUVendor", updateVendor);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Vendor Updated Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }


    @PostMapping("/getAllDistrict")
    public RDVTSResponse getAllDistrict() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<DistrictBoundaryDto> districtList = masterService.getAllDistrict();
            if (!districtList.isEmpty() && districtList.size() > 0) {
                result.put("districtList", districtList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("districtList", districtList);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getBlockByDistId")
    public RDVTSResponse getBlockByDistId(@RequestParam(name = "distId", required = false) Integer distId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<BlockBoundaryDto> block = masterService.getBlockByDistId(distId);
            result.put("block", block);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Block By Dist");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getDivisionByDistId")
    public RDVTSResponse getDivisionByDistId(@RequestParam(name = "distId", required = false) Integer distId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<DivisionDto> div = masterService.getDivisionByDistId(distId);
            result.put("div", div);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Division Block By Dist");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    //Geo Master
    @PostMapping("/addGeoMaster")
    public RDVTSResponse saveGeoMaster(@RequestBody GeoMasterEntity geoMaster) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            GeoMasterEntity geoMasterEntity = geoMasterService.saveGeoMaster(geoMaster);
            result.put("geoMasterEntity", geoMasterEntity);
            response.setData(result);
            response.setStatus(1);
            response.setMessage("Geo Master Created Successfully");
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));

        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/updateGeoMaster")
    public RDVTSResponse updateGeoMaster(@RequestParam int id,
                                         @RequestParam(name = "data") String data) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try{
            ObjectMapper mapper = new ObjectMapper();
            GeoMasterDto updateGeoMasterDto = mapper.readValue(data,GeoMasterDto.class);
            GeoMasterEntity geoMasterEntity = geoMasterService.updateGeoMaster(id, updateGeoMasterDto);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Geo Master Updated Successfully");
        } catch (Exception e){
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getAllGeoMasterByAllId")
    public RDVTSResponse getAllGeoMasterByAllId(@RequestParam(defaultValue = "0", required = false) Integer id,
                                                @RequestParam(defaultValue = "0", required = false) Integer geoWorkId,
                                                @RequestParam(defaultValue = "0", required = false) Integer geoDistId,
                                                @RequestParam(defaultValue = "0", required = false) Integer geoBlockId,
                                                @RequestParam(defaultValue = "0", required = false) Integer geoPiuId,
                                                @RequestParam(defaultValue = "0", required = false) Integer geoContractorId,
                                                @RequestParam(defaultValue = "0", required = false) Integer workId,
                                                @RequestParam(defaultValue = "0", required = false) Integer piuId,
                                                @RequestParam(defaultValue = "0", required = false) Integer distId,
                                                @RequestParam(defaultValue = "0", required = false) Integer blockId,
                                                @RequestParam(defaultValue = "0", required = false) Integer roadId ) {
        /*GeoMasterDto geoMasterDto = new GeoMasterDto();
        geoMasterDto.setId(id);
        geoMasterDto.setGeoWorkId(geoWorkId);
        geoMasterDto.setGeoDistId(geoDistId);
        geoMasterDto.setGeoBlockId(geoBlockId);
        geoMasterDto.setGeoPiuId(geoPiuId);
        geoMasterDto.setGeoContractorId(geoContractorId);
        geoMasterDto.setWorkId(workId);
        geoMasterDto.setPiuId(piuId);
        geoMasterDto.setDistId(distId);
        geoMasterDto.setBlockId(blockId);
        geoMasterDto.setRoadId(roadId);*/
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try{
            List<GeoMasterDto> geoMasterDtoList = geoMasterService.getAllGeoMasterByAllId(id, geoWorkId, geoDistId, geoBlockId, geoPiuId, geoContractorId, workId, piuId, distId, blockId, roadId);
            result.put("geoMasterDtoList", geoMasterDtoList);
            response.setData(geoMasterDtoList);
            response.setMessage("Geo Master By Id");
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
        } catch (Exception e){
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getAllState")
    public RDVTSResponse getAllState() {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<StateDto> state = masterService.getAllState();
            if (!state.isEmpty() && state.size() > 0) {
                result.put("state", state);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            } else {
                result.put("state", state);
                response.setData(result);
                response.setStatus(1);
                response.setStatusCode(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            }
        } catch (Exception e) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    e.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getDistByStateId")
    public RDVTSResponse getDistByStateId(@RequestParam(name = "stateId", required = false) Integer stateId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<DistrictBoundaryDto> dist = masterService.getDistByStateId(stateId);
            result.put("dist", dist);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("Dist By StateId");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getListOfBlockByListOfDistId")
    public RDVTSResponse getListOfBlockByListOfDistId(@RequestParam List<Integer> distId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<BlockBoundaryDto> block = masterService.getListOfBlockByListOfDistId(distId);
            result.put("block", block);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("List of Block By List of Dist");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }

    @PostMapping("/getListOfDivisionByListOfDistId")
    public RDVTSResponse getListOfDivisionByListOfDistId(@RequestParam List<Integer> distId) {
        RDVTSResponse response = new RDVTSResponse();
        Map<String, Object> result = new HashMap<>();
        try {
            List<DivisionDto> div = masterService.getListOfDivisionByListOfDistId(distId);
            result.put("div", div);
            response.setData(result);
            response.setStatus(1);
            response.setStatusCode(new ResponseEntity<>(HttpStatus.OK));
            response.setMessage("List of Div By DistId List");
        } catch (Exception ex) {
            response = new RDVTSResponse(0,
                    new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR),
                    ex.getMessage(),
                    result);
        }
        return response;
    }


}