package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.MenuDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.entities.MenuEntity;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.MasterRepository;
import gov.orsac.RDVTS.repository.MenuRepository;
import gov.orsac.RDVTS.repository.RoleRepository;
import gov.orsac.RDVTS.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
