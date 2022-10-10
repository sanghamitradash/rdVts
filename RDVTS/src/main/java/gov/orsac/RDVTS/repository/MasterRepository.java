package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.MenuDto;
import gov.orsac.RDVTS.dto.RoleDto;

import java.util.List;

public interface MasterRepository {
    List<RoleDto> getRoleByRoleId(Integer roleId);
    List<RoleDto> getRoleByUserLevelId(Integer userLevelId);
    List<MenuDto> getMenu(Integer userId,Integer id);
}
