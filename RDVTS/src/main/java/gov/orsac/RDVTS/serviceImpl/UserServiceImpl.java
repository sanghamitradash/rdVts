package gov.orsac.RDVTS.serviceImpl;


import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.dto.UserAreaMappingDto;
import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.entities.UserAreaMappingEntity;
import gov.orsac.RDVTS.entities.UserEntity;
import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import gov.orsac.RDVTS.exception.RecordExistException;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.UserAreaMappingRepository;
import gov.orsac.RDVTS.repository.UserPaswordMasterRepo;
import gov.orsac.RDVTS.repository.UserRepository;
import gov.orsac.RDVTS.repositoryImpl.UserPaswordMasterRepoImpl;
import gov.orsac.RDVTS.repositoryImpl.UserRepositoryImpl;
import gov.orsac.RDVTS.service.EmailService;
import gov.orsac.RDVTS.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private UserAreaMappingRepository userAreaMappingRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    @Autowired
    private EmailService emailService;


    public int count(String qryStr, MapSqlParameterSource sqlParam) {
        String sqlStr = "SELECT COUNT(*) from (" + qryStr + ") as t";
        Integer intRes = namedJdbc.queryForObject(sqlStr, sqlParam, Integer.class);
        if (null != intRes) {
            return intRes;
        }
        return 0;
    }



    @Autowired
    private BCryptPasswordEncoder encoder;



    @Autowired
    private UserPaswordMasterRepoImpl userPaswordMasterRepoImpl;

    @Autowired
    private UserPaswordMasterRepo userPaswordMasterRepo;

    @Override
    public UserEntity saveUser(UserDto userSaveRequests) throws RecordExistException {
        if (checkEmailExists(userSaveRequests.getEmail())){
            throw new RecordExistException("User", "Email", userSaveRequests.getEmail());
        }
        if (checkMobileNumberExists(userSaveRequests.getMobile1())){
            throw new RecordExistException("User", "Mobile", userSaveRequests.getMobile1());
        }

        if (userSaveRequests.getMobile2()!=null && !userSaveRequests.getMobile2().toString().isEmpty() && checkMobileNumberExists2(userSaveRequests.getMobile2())){
            throw new RecordExistException("User", "Mobile", userSaveRequests.getMobile2());
        }
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
    public Page<UserInfoDto> getUserList(UserListRequest userListRequest) {
        return userRepositoryImpl.getUserList(userListRequest);
      //  return userList;
    }

    @Override
    public UserEntity updateUser(int id, UserDto userDto) throws Exception {

        UserEntity existingUser = userRepository.findById(id);

        if(existingUser == null){
            throw new RecordNotFoundException("User", "id", id);
        }
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setMiddleName(userDto.getMiddleName());
        existingUser.setUserLevelId(userDto.getUserLevelId());
        existingUser.setUpdatedBy(userDto.getUpdatedBy());
        existingUser.setDesignationId(userDto.getDesignationId());
        existingUser.setContractorId(userDto.getContractorId());
        existingUser.setRoleId(userDto.getRoleId());
        UserEntity save = userRepository.save(existingUser);
        return save;
    }

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
    public UserPasswordMasterEntity updateUserPass(Integer userId, UserPasswordMasterDto userPasswordMasterDto) {
        UserPasswordMasterEntity existingUserId = userPaswordMasterRepo.findByUserId(userId);
        if(existingUserId != null){
            int saveHistory = userPaswordMasterRepoImpl.savePasswordInHistory(userId, userPasswordMasterDto);
        }

        if(encoder.matches(userPasswordMasterDto.getOldPassword(), existingUserId.getPassword())){
            if(userPasswordMasterDto.getPassword().equals(userPasswordMasterDto.getConfirmPassword())){
                Boolean savePass = userPaswordMasterRepoImpl.savePassword(existingUserId.getUserId(), userPasswordMasterDto);
            }

        }


        //existingUserId.setPassword(encoder.encode(userPasswordMasterDto.getPassword()));
        //commented
//        Boolean savePass = userPaswordMasterRepoImpl.savePassword(existingUserId.getUserId(), userPasswordMasterDto);
        return existingUserId;
    }

    @Override
    public UserPasswordMasterEntity resetPassword(Integer userId, UserPasswordMasterDto userPasswordMasterDto) {
        UserPasswordMasterEntity existingUserId = userPaswordMasterRepo.findByUserId(userId);
        if(existingUserId != null){
            int saveHistory = userPaswordMasterRepoImpl.savePasswordInHistory(userId, userPasswordMasterDto);
            if (saveHistory==1){
                Boolean savePass = userPaswordMasterRepoImpl.savePassword(existingUserId.getUserId(), userPasswordMasterDto);
            }
        }




        //existingUserId.setPassword(encoder.encode(userPasswordMasterDto.getPassword()));
        //commented
//        Boolean savePass = userPaswordMasterRepoImpl.savePassword(existingUserId.getUserId(), userPasswordMasterDto);
        return existingUserId;
    }

    @Override
    public UserAreaMappingEntity updateUserAreaMappingByUserId(Integer userId, UserAreaMappingDto userAreaMapping) {
       UserAreaMappingEntity existingArea = userAreaMappingRepository.findByUserId(userId);
        if(existingArea == null){
            throw new RecordNotFoundException("existingArea", "userId", userId);
        }
        existingArea.setUserId(userAreaMapping.getUserId());
        existingArea.setGDistId(userAreaMapping.getGdistId());
        existingArea.setDistId(userAreaMapping.getDistId());
        existingArea.setGBlockId(userAreaMapping.getGblockId());
        existingArea.setBlockId(userAreaMapping.getBlockId());
        UserAreaMappingEntity save = userAreaMappingRepository.save(existingArea);
        return save;

    }

    @Override
    public List<UserAreaMappingDto> getUserAreaMappingByUserId(Integer userId) {
        return userRepositoryImpl.getUserAreaMappingByUserId(userId);
    }

    @Override
    public List<UserAreaMappingEntity> createUserAreaMapping(List<UserAreaMappingEntity> userAreaMapping){
         for(UserAreaMappingEntity userAreaMapping1 : userAreaMapping) {
        userAreaMapping1.setGBlockId(2);
        userAreaMapping1.setGDistId(2);
         }
        return userAreaMappingRepository.saveAll(userAreaMapping);
}

    @Override
    public Page<UserAreaMappingDto> getUserMappingAreaDetails(UserAreaMappingDto userAreaMapping) {
        PageRequest pageable = PageRequest.of(userAreaMapping.getPage(), userAreaMapping.getSize(), Sort.Direction.fromString(userAreaMapping.getSortOrder()), userAreaMapping.getSortBy());
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer  resultCount = 0;
        String queryString = " ";

        queryString = "SELECT um.id,um.user_id,um.g_dist_id,um.dist_id,um.g_block_id,um.block_id,um.division_id from rdvts_oltp.user_area_mapping as um   " +
                      "WHERE um.is_active = true" ;

        if(userAreaMapping.getId() != null && userAreaMapping.getId() > 0){
            queryString += " AND um.id=:id ";
            sqlParam.addValue("id", userAreaMapping.getId());
        }

        if(userAreaMapping.getUserId() != null && userAreaMapping.getUserId() > 0){
            queryString += " AND um.user_id=:userId ";
            sqlParam.addValue("userId", userAreaMapping.getUserId());
        }

        if(userAreaMapping.getBlockId() != null && userAreaMapping.getBlockId() > 0){
            queryString += " AND um.block_id=:blockId ";
            sqlParam.addValue("blockId", userAreaMapping.getBlockId());
        }

        if(userAreaMapping.getDistId() != null && userAreaMapping.getDistId() > 0){
            queryString += " AND um.dist_id=:distId ";
            sqlParam.addValue("distId", userAreaMapping.getDistId());
        }

        if(userAreaMapping.getGdistId() != null && userAreaMapping.getGdistId() > 0){
            queryString += " AND um.g_dist_id=:gDistId ";
            sqlParam.addValue("gDistId", userAreaMapping.getGdistId());
        }

        if(userAreaMapping.getGblockId() != null && userAreaMapping.getGblockId() > 0){
            queryString += " AND um.g_block_id=:gBlockId ";
            sqlParam.addValue("gBlockId", userAreaMapping.getGblockId());
        }

        if(userAreaMapping.getDivisionId() != null && userAreaMapping.getDivisionId() > 0){
            queryString += " AND um.division_id=:divisionId ";
            sqlParam.addValue("divisionId", userAreaMapping.getDivisionId());
        }

        queryString += " ORDER BY " + order.getProperty() + " " + order.getDirection().name();
        resultCount = count(queryString, sqlParam);
        queryString += " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

        List<UserAreaMappingDto> userAreaInfo = namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(UserAreaMappingDto.class));
        return new PageImpl<>(userAreaInfo,pageable,resultCount);
    }


    @Override
    public UserEntity findUserByMobile(Long mobile) {
        return userRepository.findUserByMobile(mobile);
    }
    @Override
    public Boolean deactivateAreaMapping(Integer id) {
        return userRepositoryImpl.deactivateAreaByUserId(id);
    }

    @Override
    public UserEntity findUserByMobileAndEmail(String email) {
        //return userRepository.findUserByMobileAndEmail(email);
        return userRepositoryImpl.getUserByemail(email);
    }


    @Override
    public Integer sendOtpToUser(UserDto user) {
       MailDto mailDto = new MailDto();
        Random random = new Random();
        int otp = random.nextInt(999999);


        int id=user.getId();
       // System.out.println(id);
        UserEntity existingUser = userRepository.findById(id);
        existingUser.setOtp(otp);
        userRepository.save(existingUser);


        mailDto.setRecipient(user.getEmail());
        mailDto.setSubject("OTP for new password.");
        String message = readMailBody("FORGOT_PWD_OTP_TEMPLATE.txt", user, otp);
        mailDto.setMessage(message);
        if (emailService.sendHtmlMail(mailDto)){
            return otp;
        }
        return 0;
        //return otp;
    }

    public String readMailBody(String filename, UserDto user, Integer otp){
        String mailBody = null;
        try {
            File resource = new ClassPathResource(filename).getFile();
            mailBody = new String(Files.readAllBytes(resource.toPath()));
            mailBody = mailBody.replace("{name}", user.getFirstName());
            mailBody = mailBody.replace("{otp}", String.valueOf(otp));
        }catch (IOException io){
            System.out.println(io.getMessage());
        }
        return mailBody;
    }


    public UserInfoDto getUserByUserId(Integer userId){

            return userRepositoryImpl.getUserByUserId(userId);

    }

    public UserDto getUserBymobile(Long mobile){

        return userRepositoryImpl.getUserBymobile(mobile);

    }

    public List<UserAreaMappingEntity> saveUserAreaMapping(Integer userLevelId, Integer userId, List<UserAreaMappingEntity> userAreaMapping){
        List<UserAreaMappingEntity> user = new ArrayList();
        if(userLevelId == 1){
            for(UserAreaMappingEntity userAreaMapping1 : userAreaMapping) {
                //userAreaMapping1.setStateId(userLevelId);
                userAreaMapping1.setUserId(userId);
                user.add(userAreaMapping1);

            }
        }
        if(userLevelId ==2){
            for(UserAreaMappingEntity userAreaMapping1 :userAreaMapping){
                UserAreaMappingRequestDTO  userRequest = userRepositoryImpl.getStateByDistId(userAreaMapping1.getDistId());
                userAreaMapping1.setStateId(userRequest.getStateId());
                userAreaMapping1.setUserId(userId);
                user.add(userAreaMapping1);
            }
        }
        if(userLevelId == 3){
            for(UserAreaMappingEntity userAreaMapping1 :userAreaMapping){
                UserAreaMappingRequestDTO  userRequest = userRepositoryImpl.getStateDistByBlockId(userAreaMapping1.getBlockId());
                userAreaMapping1.setStateId(userRequest.getStateId());
                userAreaMapping1.setDistId(userRequest.getDistId());
                userAreaMapping1.setUserId(userId);
                user.add(userAreaMapping1);
            }
        }
        if(userLevelId == 4){
            for(UserAreaMappingEntity userAreaMapping1 :userAreaMapping){
                UserAreaMappingRequestDTO  userRequest = userRepositoryImpl.getStateDistByDivisionId(userAreaMapping1.getDivisionId());
                userAreaMapping1.setStateId(userRequest.getStateId());
                userAreaMapping1.setDistId(userRequest.getDistId());
                userAreaMapping1.setUserId(userId);
                user.add(userAreaMapping1);
            }
        }

        return userAreaMappingRepository.saveAll(userAreaMapping);
    }




}
