package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Boolean existsByEmail(String email);

    Boolean existsByMobile1(Long mobileNo);
    Boolean existsByMobile2(Long mobileNo);

    UserEntity findById(int id);


    @Query(value = "SELECT id, first_name, middle_name, last_name, email, mobile_1, mobile_2, designation_id, user_level_id, role_id, is_active, created_by, created_on, updated_by, updated_on, contractor_id, otp\\n\" +\n" +
            "                \"\\t FROM rdvts_oltp.user_m where email=:email and is_active=true;", nativeQuery = true)
    UserEntity findUserByMobileAndEmail(String email);


    @Query(value = "select * from rdvts_oltp.user_m where mobile_1=:mobile", nativeQuery = true)
    UserEntity findUserByMobile(long mobile);



}
