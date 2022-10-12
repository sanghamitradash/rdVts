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

    List<UserDto> getUserList(UserDto userDto);

    UserEntity updateUser(int id, UserDto userDto) throws Exception;

    UserEntity findUserByMobile(Long Mobile);

    List<UserAreaMappingEntity> createUserAreaMapping(List<UserAreaMappingEntity> userAreaMapping);

    Page<UserAreaMappingDto> getUserMappingAreaDetails(UserAreaMappingDto userAreaMapping);

    UserPasswordMasterEntity saveUserPassword(UserPasswordMasterDto userPasswordMasterDto);

    UserPasswordMasterDto getPasswordByUserId(Integer userId);

    UserPasswordMasterDto getPasswordById(Integer id);

    UserPasswordMasterEntity updateUserPass(Integer userId, UserPasswordMasterDto uerPasswordMasterDto);

    UserEntity findUserByMobileAndEmail(String email);

    Integer sendOtpToUser(UserDto user);

    UserDto getUserByUserId(Integer userId);
    UserDto getUserBymobile(Long mobile);



    UserAreaMappingEntity updateUserAreaMappingByUserId(Integer userId, UserAreaMappingDto userAreaMapping);
}
