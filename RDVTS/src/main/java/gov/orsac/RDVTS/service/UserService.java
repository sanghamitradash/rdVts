package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.UserAreaMappingEntity;
import gov.orsac.RDVTS.entities.UserEntity;
import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import gov.orsac.RDVTS.exception.RecordExistException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {


    UserEntity saveUser(UserDto userSaveRequests) throws RecordExistException;

    Page<UserInfoDto> getUserList(UserListRequest userListRequest);

    UserEntity updateUser(int id, UserDto userDto) throws Exception;
    Boolean deactivateAreaMapping(Integer id);

    UserEntity findUserByMobile(Long Mobile);

    List<UserAreaMappingEntity> createUserAreaMapping(List<UserAreaMappingEntity> userAreaMapping);

    Page<UserAreaMappingDto> getUserMappingAreaDetails(UserAreaMappingDto userAreaMapping);

    UserPasswordMasterEntity saveUserPassword(UserPasswordMasterDto userPasswordMasterDto);

    UserPasswordMasterDto getPasswordByUserId(Integer userId);

    UserPasswordMasterDto getPasswordById(Integer id);

    UserPasswordMasterEntity updateUserPass(Integer userId, UserPasswordMasterDto userPasswordMasterDto);
    UserPasswordMasterEntity resetPassword(Integer userId, UserPasswordMasterDto userPasswordMasterDto);

    UserEntity findUserByMobileAndEmail(String email);

    Integer sendOtpToUser(UserDto user);

    UserInfoDto getUserByUserId(Integer userId);
//    Boolean saveLoginLog(Integer userId);
    UserDto getUserBymobile(Long mobile);



    UserAreaMappingEntity updateUserAreaMappingByUserId(Integer userId, UserAreaMappingDto userAreaMapping);

    List<UserAreaMappingDto> getUserAreaMappingByUserId(Integer userId);

    List<UserAreaMappingEntity> saveUserAreaMapping(Integer userLevelId, Integer userId, List<UserAreaMappingEntity> userAreaMapping);


    Boolean activateAndDeactivateUser(Integer id);
}
