package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.repository.HelperRepository;
import gov.orsac.RDVTS.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HelperServiceImpl implements HelperService {
    @Autowired
    private HelperRepository helperRepository;
    @Override
    public Integer getUserLevelByUserId(Integer userId) {
        return helperRepository.getUserLevelByUserId(userId);
    }

    @Override
    public List<Integer> getLowerUserLevelIdsByUserLevelId(Integer userLevelId,Integer userId) {
        return helperRepository.getLowerUserLevelIdsByUserLevelId(userLevelId,userId);
    }

    @Override
    public List<Integer> getLowerUserByUserId(Integer userId) {
        Integer userLevel=helperRepository.getUserLevelByUserId(userId);

        List<Integer> userLevelIds=helperRepository.getLowerUserLevelIdsByUserLevelId(userLevel,userId);
        return helperRepository.getLowerUserByUserId(userLevelIds);
    }

    @Override
    public List<DesignationDto> getLowerDesignation(Integer userId,Integer designationId) {
        return helperRepository.getLowerDesignation(userId,designationId);
    }

    @Override
    public List<RoleDto> getLowerRole(Integer userId,Integer roleId) {
        return helperRepository.getLowerRole(userId,roleId);
    }
}
