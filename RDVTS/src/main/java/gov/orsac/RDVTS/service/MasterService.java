package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.MenuEntity;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.RoleMenuMaster;
import gov.orsac.RDVTS.entities.UserLevelMaster;

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
    List<ParentMenuInfo> getMenuHierarchyByRole(Integer userId, Integer roleId);
    List<ParentMenuInfo> getMenuHierarchyWithoutRoleId(Integer userId);

}
