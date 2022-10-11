package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.UserPasswordMasterDto;
import gov.orsac.RDVTS.entities.ContractorEntity;
import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.UserPaswordMasterRepo;
import gov.orsac.RDVTS.repositoryImpl.UserPaswordMasterRepoImpl;
import gov.orsac.RDVTS.service.UserPaswordMasterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserPaswordMasterServiceImpl implements UserPaswordMasterService {

    @Autowired
    private UserPaswordMasterRepo userPaswordMasterRepo;

    @Autowired
    private UserPaswordMasterRepoImpl userPaswordMasterRepoImpl;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserPasswordMasterEntity saveUserPassword(UserPasswordMasterDto userPasswordMasterDto){

        userPasswordMasterDto.setPassword(encoder.encode(userPasswordMasterDto.getPassword()));
        userPasswordMasterDto.setIsActive(true);
        userPasswordMasterDto.setCreatedBy(1);
        userPasswordMasterDto.setUpdatedBy(1);
        UserPasswordMasterEntity userPasswordMasterEntity= new UserPasswordMasterEntity();
        BeanUtils.copyProperties(userPasswordMasterDto, userPasswordMasterEntity);
        return userPaswordMasterRepo.save(userPasswordMasterEntity);
    }

    @Override
    public UserPasswordMasterDto getPasswordByUserId(Integer userId){
        UserPasswordMasterDto userPasswordMasterDto = userPaswordMasterRepoImpl.getPasswordByUserId(userId);
        return userPasswordMasterDto;
    }

    @Override
    public UserPasswordMasterDto getPasswordById(Integer id){
        UserPasswordMasterDto userPasswordMasterDto = userPaswordMasterRepoImpl.getPasswordById(id);
        return userPasswordMasterDto;
    }

    @Override
    public UserPasswordMasterEntity updateUser(Integer userId, UserPasswordMasterDto userPasswordMasterDto) {
        UserPasswordMasterEntity existingId = userPaswordMasterRepo.findById(userId).orElseThrow(() -> new RecordNotFoundException("userId", "userId", userId));
        UserPasswordMasterEntity saveHistory = userPaswordMasterRepoImpl.savePasswordInHistory(userId, userPasswordMasterDto);
        existingId.setPassword(encoder.encode(userPasswordMasterDto.getPassword()));
        UserPasswordMasterEntity save = userPaswordMasterRepo.save(existingId);
        return save;
    }


}
