package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.MenuDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.dto.RoleMenuDto;
import gov.orsac.RDVTS.entities.MenuEntity;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.RoleMenuMaster;
import gov.orsac.RDVTS.entities.UserLevelMaster;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.*;
import gov.orsac.RDVTS.repositoryImpl.MasterRepositoryImpl;
import gov.orsac.RDVTS.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<RoleMenuDto> getAllMenuByRoleId(Integer userId, Integer roleId) {
        List<RoleMenuDto> roleMenu = masterRepositoryImpl.getRoleMenu(userId, roleId);
        return roleMenu;
    }




}
