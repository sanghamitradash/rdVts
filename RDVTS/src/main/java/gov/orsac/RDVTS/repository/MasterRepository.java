package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.MenuDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.entities.UserLevelMaster;

import java.util.List;

public interface MasterRepository {
    List<RoleDto> getRoleByRoleId(Integer roleId);
    List<RoleDto> getRoleByUserLevelId(Integer userLevelId);
    List<MenuDto> getMenu(Integer userId,Integer id);
    List<UserLevelMaster> getUserLevelById(Integer userLevelId);
    List<UserLevelMaster> getAllUserLevel(Integer userLevelId);
}
