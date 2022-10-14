package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RoleDto;

import java.util.List;

public interface HelperRepository {
    Integer getUserLevelByUserId(Integer userId);
    List<Integer> getLowerUserLevelIdsByUserLevelId(Integer userLevelId,Integer userId);
    List<Integer> getLowerUserByUserId(List<Integer> userLevelId);
    List<DesignationDto> getLowerDesignation(Integer userId);
    List<RoleDto>getLowerRole(Integer userId);
}
