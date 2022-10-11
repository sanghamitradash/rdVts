package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.entities.UserEntity;
import gov.orsac.RDVTS.exception.RecordExistException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {


    UserEntity saveUser(UserDto userSaveRequests) throws RecordExistException;

    List<UserDto> getUserList(UserDto userDto);

    UserEntity updateUser(int id, UserDto userDto) throws Exception;

    UserEntity findUserByMobile(Long Mobile);

}
