package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.*;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.*;
import gov.orsac.RDVTS.repositoryImpl.MasterRepositoryImpl;
import gov.orsac.RDVTS.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MasterServiceImpl implements MasterService {
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;
    @Autowired
    private MasterRepositoryImpl masterRepositoryImpl;
    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private VTUVendorMasterRepository vtuVendorRepository;

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        return roleRepo.save(role);
    }

    @Override
    public List<RoleDto> getRoleByRoleId(Integer id) {
        List<RoleDto> role = masterRepository.getRoleByRoleId(id);
        return role;
    }

    @Override
    public RoleEntity updateRole(int id, RoleEntity role) {
        RoleEntity existingRole = roleRepo.findRoleById(id);
        if (existingRole == null) {
            throw new RecordNotFoundException("Role", "id", id);
        }
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        existingRole.setUserLevelId(role.getUserLevelId());
        existingRole.setParentRoleId(role.getParentRoleId());
        existingRole.setCanEdit(role.isCanEdit());
        existingRole.setCanView(role.isCanView());
        existingRole.setCanAdd(role.isCanAdd());
        existingRole.setCanDelete(role.isCanDelete());
        existingRole.setCanApprove(role.isCanApprove());
        existingRole.setUpdatedBy(role.getUpdatedBy());
        existingRole.setActive(role.isActive());

        return roleRepo.save(existingRole);
    }

    @Override
    public List<RoleDto> getRoleByUserLevelId(Integer userLevelId) {
        List<RoleDto> role = masterRepository.getRoleByUserLevelId(userLevelId);
        return role;
    }

    @Override
    public MenuEntity saveMenu(MenuEntity menuMaster) {
        menuMaster.setActive(true);

        return menuRepository.save(menuMaster);
    }

    @Override
    public List<MenuDto> getMenu(Integer userId, Integer id) {
        List<MenuDto> menu = masterRepository.getMenu(userId, id);
        return menu;
    }

    @Override
    public MenuEntity updateMenu(int id, MenuEntity menuMaster) {
        MenuEntity existingMenu = menuRepository.findMenuById(id);
        if (existingMenu == null) {
            throw new RecordNotFoundException("MenuEntity", "id", id);
        }
        existingMenu.setName(menuMaster.getName());
        existingMenu.setParentId(menuMaster.getParentId());
        existingMenu.setModule(menuMaster.getModule());
        existingMenu.setMenuOrder(menuMaster.getMenuOrder());
        existingMenu.setUpdatedBy(menuMaster.getUpdatedBy());
        existingMenu.setActive(menuMaster.isActive());
        return menuRepository.save(existingMenu);
    }

    @Override
    public UserLevelMaster saveUserLevel(UserLevelMaster userLevel) {
        userLevel.setActive(true);
        UserLevelMaster userLevel1 = new UserLevelMaster();
        BeanUtils.copyProperties(userLevel, userLevel1);
        return userLevelRepository.save(userLevel1);
    }

    @Override
    public UserLevelMaster updateUserLevel(int id, UserLevelMaster userLevel) {
        UserLevelMaster existingUserLevel = userLevelRepository.findUserLevelById(id);
        if (existingUserLevel == null) {
            throw new RecordNotFoundException("UserLevel", "id", id);
        }
        existingUserLevel.setName(userLevel.getName());
        existingUserLevel.setParentId(userLevel.getParentId());
        existingUserLevel.setActive(userLevel.isActive());
        existingUserLevel.setUpdatedBy(userLevel.getUpdatedBy());
        return userLevelRepository.save(existingUserLevel);
    }

    @Override
    public List<UserLevelMaster> getUserLevelById(int id) {

        return masterRepository.getUserLevelById(id);
    }

    @Override
    public List<UserLevelMaster> getAllUserLevel(int userId) {
        Integer userLevelIdByUserId = masterRepositoryImpl.getUserLevelIdByUserId(userId);
        return masterRepository.getAllUserLevel(userLevelIdByUserId);
    }

    @Override
    public List<RoleMenuMaster> saveRoleMenu(RoleMenuDto roleMenuInfo) {
        List<RoleMenuMaster> roleMenuMaster = new ArrayList<>();
        for (int j = 0; j < roleMenuInfo.getMenuId().size(); j++) {
            RoleMenuMaster roleMenu = new RoleMenuMaster();
            roleMenu.setMenuId(roleMenuInfo.getMenuId().get(j));
            roleMenu.setActive(true);
            roleMenu.setCreatedBy(roleMenuInfo.getCreatedBy());
            roleMenu.setRoleId(roleMenuInfo.getRoleId());
            roleMenuMaster.add(roleMenu);
        }
        return roleMenuRepository.saveAll(roleMenuMaster);
    }

    @Override
    public List<RoleMenuInfo> getAllMenuByRoleId(Integer userId, Integer roleId) {
        List<RoleMenuInfo> roleMenu = masterRepositoryImpl.getRoleMenu(userId, roleId);
        return roleMenu;
    }

    @Override
    public List<RoleMenuInfo> getMenuByRoleId(Integer userId, Integer roleId) {
        List<RoleMenuInfo> roleMenu = masterRepositoryImpl.getMenuByRoleId(userId, roleId);
        return roleMenu;
    }

    @Override
    public List<ParentMenuInfo> getMenuHierarchyByRole(Integer userId, Integer roleId) {
        List<RoleMenuInfo> allMenuByRoleId = getAllMenuByRoleId(userId, roleId);
        List<Integer> menuIdsByRoleId = new ArrayList<>();
        for (int i = 0; i < allMenuByRoleId.size(); i++) {
            menuIdsByRoleId.add(allMenuByRoleId.get(i).getMenuId());
        }
        List<ParentMenuInfo> parentMenuList = masterRepositoryImpl.getAllParentMenu(roleId);
        List<ParentMenuInfo> finalList = new ArrayList<>();
        Integer cnt = 0;
        for (ParentMenuInfo parentMenuInfo : parentMenuList) {
            if (menuIdsByRoleId.contains(parentMenuInfo.getValue())) {
                List<HierarchyMenuInfo> childMenuList = masterRepositoryImpl.getHierarchyMenuListById(parentMenuInfo.getValue(), roleId);
                if (childMenuList.size() > 0) {
                    List<HierarchyMenuInfo> finalChildList = new ArrayList<>();
                    int childCnt = 0;
                    for (HierarchyMenuInfo hierarchyMenuInfo : childMenuList) {
                        if (menuIdsByRoleId.contains(hierarchyMenuInfo.getValue())) {
                            finalChildList.add(childCnt, hierarchyMenuInfo);
                        }
                    }
                    parentMenuInfo.setChildren(finalChildList);
                } else {
                    parentMenuInfo.setChildren(new ArrayList<>());
                }
                finalList.add(cnt++, parentMenuInfo);
            }
        }
        return finalList;
    }

    @Override
    public List<ParentMenuInfo> getMenuHierarchyWithoutRoleId(Integer userId) {
        List<ParentMenuInfo> parentMenuList = masterRepositoryImpl.getAllParentMenuWithoutRoleId();
        List<ParentMenuInfo> finalList = new ArrayList<>();
        Integer cnt = 0;
        for (ParentMenuInfo parentMenuInfo : parentMenuList) {
            List<HierarchyMenuInfo> childMenuList = masterRepositoryImpl.getHierarchyMenuListByIdWithoutRoleId(parentMenuInfo.getValue());
            if (childMenuList.size() > 0) {
                List<HierarchyMenuInfo> finalChildList = new ArrayList<>();
                int childCnt = 0;
                for (HierarchyMenuInfo hierarchyMenuInfo : childMenuList) {
                    finalChildList.add(childCnt, hierarchyMenuInfo);
                }
                parentMenuInfo.setChildren(finalChildList);
            } else {
                parentMenuInfo.setChildren(new ArrayList<>());
            }
            finalList.add(cnt++, parentMenuInfo);
        }
        return finalList;
    }

    @Override
    public List<RoleMenuInfo> getAllMenuByRoleIds(Integer userId, Integer roleId) {
        List<RoleMenuInfo> roleMenu = masterRepositoryImpl.getRoleMenus(userId, roleId);
        return roleMenu;
    }

    @Override
    public Boolean deactivateMenu(int roleId, int menuId, boolean isActive) {
        return masterRepositoryImpl.deactivateMenu(roleId, menuId, isActive);
    }

    @Override
    public RoleMenuMaster updateRoleMenu(RoleMenuDto roleMenuDto, Integer menuId) {
        RoleMenuMaster roleMenu = new RoleMenuMaster();
        roleMenu.setMenuId(menuId);
        roleMenu.setActive(true);
        roleMenu.setCreatedBy(roleMenuDto.getCreatedBy());
        roleMenu.setRoleId(roleMenuDto.getRoleId());

        return roleMenuRepository.save(roleMenu);
    }

    @Override
    public VTUVendorMasterEntity saveVTUVendor(VTUVendorMasterDto vendorMasterDto) {
        vendorMasterDto.setIsActive(true);
        vendorMasterDto.setCreatedBy(1);
        vendorMasterDto.setUpdatedBy(1);
        VTUVendorMasterEntity vendorMasterEntity = new VTUVendorMasterEntity();
        BeanUtils.copyProperties(vendorMasterDto, vendorMasterEntity);
        return vtuVendorRepository.save(vendorMasterEntity);
    }

    @Override
    public List<VTUVendorMasterDto> getVTUVendorById(Integer id, Integer userId) {
        return masterRepository.getVTUVendorById(id, userId);
    }

    @Override
    public Page<VTUVendorMasterDto> getVTUVendorList(VTUVendorFilterDto vtuVendorFilterDto) {

        return masterRepository.getVTUVendorList(vtuVendorFilterDto);
    }

    @Override
    public VTUVendorMasterEntity updateVTUVendor(Integer id, VTUVendorMasterDto vtuVendorMasterDto){
        VTUVendorMasterEntity existingId = vtuVendorRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("id", "id", id));
        existingId.setVtuVendorName(vtuVendorMasterDto.getVtuVendorName());
        existingId.setVtuVendorAddress(vtuVendorMasterDto.getVtuVendorAddress());
        existingId.setVtuVendorPhone(vtuVendorMasterDto.getVtuVendorPhone());
        existingId.setCustomerCareNumber(vtuVendorMasterDto.getCustomerCareNumber());
        VTUVendorMasterEntity save = vtuVendorRepository.save(existingId);
        return save;
    }

    @Override
    public List<DistrictBoundaryDto> getAllDistrict() {
        return masterRepositoryImpl.getAllDistrict();
    }

    @Override
    public List<BlockBoundaryDto> getBlockByDistId(Integer distId) {
        return masterRepositoryImpl.getBlockByDistId(distId);
    }

    @Override
    public List<DivisionDto> getDivisionBlockByDistId(Integer distId) {
        return masterRepositoryImpl.getDivisionBlockByDistId(distId);
    }

    @Override
    public List<StateDto> getAllState() {
        return masterRepositoryImpl.getAllState();
    }

    @Override
    public List<DistrictBoundaryDto> getDistByStateId(Integer stateId) {
        return masterRepositoryImpl.getDistByStateId(stateId);
    }
}
