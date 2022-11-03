package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.UserLevelMaster;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MasterRepository {
    List<RoleDto> getRoleByRoleId(Integer roleId);
    List<RoleDto> getRoleByUserLevelId(Integer userLevelId);
    List<RoleDto> getRoleByUserLevelIdForList(Integer userLevelId);
    List<MenuDto> getMenu(Integer userId,Integer id);
    List<UserLevelMaster> getUserLevelById(Integer userLevelId);
    List<UserLevelMaster> getAllUserLevel(Integer userLevelId);
    List<VTUVendorMasterDto> getVTUVendorById(Integer id, Integer userId);
    Page<VTUVendorMasterDto> getVTUVendorList(VTUVendorFilterDto vtuVendorFilterDto);

    List<Integer> getDeviceByVendorId(Integer deviceId);

    List<DivisionDto> getDivisionByCircleId(Integer circleId);
}
