package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RoleDto;

import java.util.List;

public interface HelperService {
    Integer getUserLevelByUserId(Integer userId);
    List<Integer> getLowerUserLevelIdsByUserLevelId(Integer userLevelId,Integer userId);
    List<Integer> getLowerUserByUserId(Integer userId);
    List<DesignationDto> getLowerDesignation(Integer userId,Integer designationId);
    List<RoleDto>getLowerRole(Integer userId,Integer roleId);
}
