package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MasterService {
    //Role Master
    RoleEntity saveRole(RoleEntity role);
    List<RoleDto> getRoleByRoleId(Integer id);
    RoleEntity updateRole(int id, RoleEntity role);
    List<RoleDto> getRoleByUserLevelId(Integer userLevelId);

    //Menu Master
    MenuEntity saveMenu(MenuEntity menuMaster);
    List<MenuDto> getMenu(Integer userId, Integer id);
    MenuEntity updateMenu(int id, MenuEntity menuMaster);

    //UserLevel Master
    UserLevelMaster saveUserLevel(UserLevelMaster userLevel);
    UserLevelMaster updateUserLevel(int id, UserLevelMaster userLevel);
    List<UserLevelMaster> getUserLevelById(int id);
    List<UserLevelMaster> getAllUserLevel(int userId);

    //RoleMenu Master
    List<RoleMenuMaster> saveRoleMenu(RoleMenuDto roleMenuInfo);
    List<RoleMenuInfo> getAllMenuByRoleId(Integer userId, Integer id);
    List<RoleMenuInfo> getMenuByRoleId(Integer userId, Integer id);

    List<ParentMenuInfo> getMenuHierarchyByRole(Integer userId, Integer roleId);
    List<ParentMenuInfo> getMenuHierarchyWithoutRoleId(Integer userId);
    List<RoleMenuInfo> getAllMenuByRoleIds(Integer userId,Integer roleId);
    Boolean deactivateMenu(int roleId, int menuId, boolean isActive);
    RoleMenuMaster updateRoleMenu(RoleMenuDto roleMenuDto, Integer menuId);

    VTUVendorMasterEntity saveVTUVendor(VTUVendorMasterDto vendorMasterDto);

    VTUVendorMasterDto getVTUVendorById(Integer id);

    Page<VTUVendorMasterDto> getVTUVendorList(VTUVendorFilterDto vtuVendorFilterDto);

    VTUVendorMasterEntity updateVTUVendor(Integer id, VTUVendorMasterDto vtuVendorMasterDto);

    List<DistrictBoundaryDto> getAllDistrict();
    List<BlockBoundaryDto> getBlockByDistId(Integer distId);

    List<DivisionDto> getDivisionBlockByDistId(Integer distId);

    List<StateDto> getAllState();

    List<DistrictBoundaryDto> getDistByStateId(Integer stateId);
}
