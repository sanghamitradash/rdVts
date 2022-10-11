package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.MenuDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.dto.RoleMenuDto;
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
    List<RoleMenuDto> getAllMenuByRoleId(Integer userId, Integer id);

}
