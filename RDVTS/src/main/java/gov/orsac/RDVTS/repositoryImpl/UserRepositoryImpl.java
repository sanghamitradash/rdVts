package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {


    @Value("${dbschema}")
    private String DBSCHEMA;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public List<UserDto> getUserList(UserDto userDto) {


        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String queryString = "";
        queryString += "SELECT id, first_name as firstName, middle_name as middleName, last_name as lastName, email, mobile_1 as mobile1, mobile_2 as mobile2, designation_id as designationId, user_level_id as userLevelId, role_id as roleId, is_active as isactive, created_by, created_on, updated_by, updated_on, contractor_id as contractorId  \n" +
                "\t FROM rdvts_oltp.user_m  WHERE  is_active=true ";

        if(userDto.getId()>0){
            queryString+="AND id=:id ";
            sqlParam.addValue("id",userDto.getId());
        }
        if(userDto.getDesignationId()!= null && userDto.getDesignationId() > 0){
            queryString+="AND getDesignationId=:getDesignationId ";
            sqlParam.addValue("getDesignationId",userDto.getDesignationId());


        }

        if(userDto.getRoleId()!= null && userDto.getRoleId() > 0){
            queryString+="AND roleId=:roleId ";
            sqlParam.addValue("roleId",userDto.getRoleId());
        }


        if(userDto.getEmail() != null && !userDto.getEmail().isEmpty()){
            queryString+="AND email=:email ";
            sqlParam.addValue("email",userDto.getEmail());
        }
        if(userDto.getMobile1() != null && userDto.getMobile1()>0){
            queryString+="AND mobile1=:mobile1 ";
            sqlParam.addValue("mobile1",userDto.getMobile1());
        }
        return namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(UserDto.class));



    }


}
