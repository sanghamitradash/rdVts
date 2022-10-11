package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.dto.UserPasswordMasterDto;
import gov.orsac.RDVTS.entities.UserEntity;
import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import gov.orsac.RDVTS.exception.RecordExistException;
import org.springframework.data.domain.Page;

public interface UserService {


    UserEntity saveUser(UserDto userSaveRequests) throws RecordExistException;

    Page<UserInfoDto> getUserList(UserDto userDto);

    UserEntity updateUser(int id, UserDto userDto) throws Exception;

    UserPasswordMasterEntity saveUserPassword(UserPasswordMasterDto userPasswordMasterDto);

    UserPasswordMasterDto getPasswordByUserId(Integer userId);

    UserPasswordMasterDto getPasswordById(Integer id);

    UserPasswordMasterEntity updateUserPass(Integer userId, UserPasswordMasterDto uerPasswordMasterDto);

}
