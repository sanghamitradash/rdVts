package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Boolean existsByEmail(String email);

    Boolean existsByMobile1(Long mobileNo);
    Boolean existsByMobile2(Long mobileNo);

    UserEntity findById(int id);



}
