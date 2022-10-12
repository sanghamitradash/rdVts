package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.ContractorDto;
import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.entities.UserEntity;
import gov.orsac.RDVTS.dto.*;
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


    public int count(String qryStr, MapSqlParameterSource sqlParam) {
        String sqlStr = "SELECT COUNT(*) from (" + qryStr + ") as t";
        Integer intRes = namedJdbc.queryForObject(sqlStr, sqlParam, Integer.class);
        if (null != intRes) {
            return intRes;
        }
        return 0;
    }
    public Page<UserDto> getUserList(UserDto userDto) {

        PageRequest pageable = PageRequest.of(userDto.getPage(), userDto.getSize(), Sort.Direction.fromString(userDto.getSortOrder()), userDto.getSortBy());
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer  resultCount = 0;
        String queryString = " ";

        queryString += "SELECT id,id as userId, first_name as firstName, middle_name as middleName, last_name as lastName, email, mobile_1 as mobile1, " +
                "mobile_2 as mobile2, designation_id as designationId, user_level_id as userLevelId, role_id as roleId, is_active as isactive, created_by, created_on, updated_by, updated_on, contractor_id as contractorId  \n" +
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


        queryString += " ORDER BY " + order.getProperty() + " " + order.getDirection().name();
        resultCount = count(queryString, sqlParam);
        queryString += " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

        List<UserDto> userInfo = namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(UserDto.class));
        return new PageImpl<>(userInfo,pageable,resultCount);
        //return namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(UserDto.class));



    }




    public UserDto getUserByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, first_name, middle_name, last_name, email, mobile_1, mobile_2, designation_id, user_level_id, role_id, is_active, created_by, created_on, updated_by, updated_on, contractor_id, otp\n" +
                "\t FROM rdvts_oltp.user_m where id=:userId and is_active=true; ";

        sqlParam.addValue("userId", userId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(UserDto.class));
    }

    public UserDto getUserBymobile(Long mobile) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, first_name, middle_name, last_name, email, mobile_1, mobile_2, designation_id, user_level_id, role_id, is_active, created_by, created_on, updated_by, updated_on, contractor_id, otp\n" +
                "\t FROM rdvts_oltp.user_m where mobile_1=:mobile and is_active=true; ";

        sqlParam.addValue("mobile", mobile);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(UserDto.class));
    }

    public UserEntity getUserByemail(String email) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, first_name, middle_name, last_name, email, mobile_1, mobile_2, designation_id, user_level_id, role_id, is_active, created_by, created_on, updated_by, updated_on, contractor_id, otp\n" +
                "\t FROM rdvts_oltp.user_m where email=:email and is_active=true; ";

        sqlParam.addValue("email", email);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(UserEntity.class));
    }





    public List<UserAreaMappingDto> getUserAreaMappingByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT am.id,am.userId,am.g_dist_id as gdistId,geoDist.g_district_name as gDistName,am.dist_id,dist.district_name as distName,  " +
                "am.g_block_id as gblockId,geoblock.g_block_name as gBlockName,am.block_id,block.block_name as blockName,am.division_id,am.g_state_id as gStateId,geo.state_name as gStateName,  " +
                "am.state_id as stateId  " +
                "from rdvts_oltp.user_area_mapping as am " +
                "left join rdvts_oltp.geo_district_m as geoDist on geoDist.id = am.g_dist_id  " +
                "left join rdvts_oltp.district_boundary as dist on dist.dist_id = am.dist_id  " +
                "left join rdvts_oltp.geo_block_m as geoblock on geoblock.id = am.g_block_id  " +
                "left join rdvts_oltp.block_boundary as block on block.block_id = am.block_id  " +
                "left join rdvts_oltp.geo_state_m as geo on geo.id =am.g_state_id ";

        if(userId >0){
            qry +=" WHERE am.userId=:userId ";
            sqlParam.addValue("userId",userId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UserAreaMappingDto.class));
    }
    }

