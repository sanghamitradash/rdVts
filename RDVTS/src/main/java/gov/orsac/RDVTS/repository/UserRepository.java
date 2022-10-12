package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Boolean existsByEmail(String email);

    Boolean existsByMobile1(Long mobileNo);
    Boolean existsByMobile2(Long mobileNo);

    UserEntity findById(int id);


    @Query(value = "select * from user_m where email=:email", nativeQuery = true)
    UserEntity findUserByMobileAndEmail(String email );


    @Query(value = "select * from user_m where mobile_1=:mobile and is_active=true", nativeQuery = true)
    UserEntity findUserByMobile(long mobile);



}
