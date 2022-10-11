package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.MenuDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.entities.MenuEntity;
import gov.orsac.RDVTS.entities.RoleEntity;

import java.util.List;

public interface MasterService {
    //Role Master
    RoleEntity saveRole(RoleEntity role);
    List<RoleDto> getRoleByRoleId(Integer id);
    RoleEntity updateRole(int id, RoleEntity role);
    List<RoleDto> getRoleByUserLevelId(Integer userLevelId);

    //Menu master
    MenuEntity saveMenu(MenuEntity menuMaster);
    List<MenuDto> getMenu(Integer userId, Integer id);
    MenuEntity updateMenu(int id, MenuEntity menuMaster);

}
