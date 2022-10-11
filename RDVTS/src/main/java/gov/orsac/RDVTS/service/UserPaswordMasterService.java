package gov.orsac.RDVTS.service;

import gov.orsac.RDVTS.dto.UserPasswordMasterDto;
import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public interface UserPaswordMasterService {

    UserPasswordMasterEntity saveUserPassword(UserPasswordMasterDto userPasswordMasterDto);

    UserPasswordMasterDto getPasswordByUserId(Integer userId);

    UserPasswordMasterDto getPasswordById(Integer id);

    UserPasswordMasterEntity updateUser(Integer userId, UserPasswordMasterDto uerPasswordMasterDto);

}
