package gov.orsac.RDVTS.serviceImpl;


import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.entities.UserEntity;
import gov.orsac.RDVTS.exception.RecordExistException;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.UserRepository;
import gov.orsac.RDVTS.repositoryImpl.UserRepositoryImpl;
import gov.orsac.RDVTS.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Override
    public UserEntity saveUser(UserDto userSaveRequests) throws RecordExistException {
        if (checkEmailExists(userSaveRequests.getEmail())){
            throw new RecordExistException("User", "Email", userSaveRequests.getEmail());
        }
        if (checkMobileNumberExists(userSaveRequests.getMobile1())){
            throw new RecordExistException("User", "Mobile", userSaveRequests.getMobile1());
        }
        if (checkMobileNumberExists2(userSaveRequests.getMobile2())){
            throw new RecordExistException("User", "Mobile", userSaveRequests.getMobile2());
        }
//        userSaveRequests.setPassword(encoder.encode(userSaveRequests.getPassword()));
        /*Set<Role> userRoles = new HashSet<>();
        for (Role role : userSaveRequests.getRoles()) {
            Role nextRole = roleRepo.findRoleById(role.getId());
            userRoles.add(nextRole);
        }
        userSaveRequests.setRoles(userRoles)*/;
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userSaveRequests, user);
        return userRepository.save(user);
    }


    public Boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }


    public Boolean checkMobileNumberExists(Long mobileNo) {
        return userRepository.existsByMobile1(mobileNo);
    }
    public Boolean checkMobileNumberExists2(Long mobileNo) {
        return userRepository.existsByMobile2(mobileNo);
    }


    @Override
    public List<UserDto> getUserList(UserDto userDto) {
        List<UserDto> userList = userRepositoryImpl.getUserList(userDto);
        return userList;
    }

    @Override
    public UserEntity updateUser(int id, UserDto userDto) throws Exception {

        UserEntity existingUser = userRepository.findById(id);

        if(existingUser == null){
            throw new RecordNotFoundException("User", "id", id);
        }
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setMiddleName(userDto.getMiddleName());
        existingUser.setMobile1(userDto.getMobile1());
        existingUser.setMobile2(userDto.getMobile2());
        existingUser.setUserLevelId(userDto.getUserLevelId());
        existingUser.setUpdatedBy(userDto.getUpdatedBy());
        existingUser.setDesignationId(userDto.getDesignationId());
        existingUser.setContractorId(userDto.getContractorId());
        UserEntity save = userRepository.save(existingUser);
        return save;
    }


    @Override
    public UserEntity findUserByMobile(Long mobile) {
        return userRepository.findUserByMobile(mobile);
    }


}
