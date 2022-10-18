package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.UserDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.entities.UserEntity;
import gov.orsac.RDVTS.dto.*;
import io.swagger.models.auth.In;
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

import java.util.ArrayList;
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
    public Page<UserInfoDto> getUserList(UserListRequest userListRequest) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        pageable = PageRequest.of(userListRequest.getOffSet(),userListRequest.getLimit(), Sort.Direction.fromString("desc"), "id");
        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;

        String queryString = " ";

        queryString += "SELECT  um.id, um.first_name, um.middle_name, um.last_name, um.email, um.mobile_1 as mobile1, um.mobile_2 as mobile2 ,\n" +
                "um.designation_id as designationId , um.user_level_id, um.role_id as roleId , um.is_active as isactive, um.created_by , \n" +
                "um.created_on, um.updated_by, um.updated_on, um.contractor_id as contractorId,dm.name as designation,ulm.name as userLevel ,\n" +
                "rm.name as role,cm.name as contractor \n" +
                "\t FROM rdvts_oltp.user_m as um \n" +
                "\t left join rdvts_oltp.designation_m as dm on dm.id=um.designation_id \n" +
                "\t left join rdvts_oltp.user_level_m as ulm on ulm.id=um.user_level_id \n" +
                "\t left join rdvts_oltp.role_m as rm on rm.id=um.role_id \n" +
                "\t left join rdvts_oltp.contractor_m cm on cm.id=um.contractor_id where um.is_active=true ";



        if( userListRequest.getId()!= null && userListRequest.getId() > 0 ){
            queryString+=" AND um.id=:id ";
            sqlParam.addValue("id",userListRequest.getId());
        }
        if(userListRequest.getDesignationId()!= null && userListRequest.getDesignationId() > 0){
            queryString+=" AND   um.designation_id=:getDesignationId ";
            sqlParam.addValue("getDesignationId",userListRequest.getDesignationId());


        }

        if(userListRequest.getRoleId()!= null && userListRequest.getRoleId() > 0){
            queryString+=" AND um.role_id=:roleId ";
            sqlParam.addValue("roleId",userListRequest.getRoleId());
        }

        if(userListRequest.getContractorId()!= null && userListRequest.getContractorId() > 0){
            queryString+=" AND um.contractor_id=:contractorId ";
            sqlParam.addValue("contractorId",userListRequest.getContractorId());
        }



        if(userListRequest.getEmail() != null && !userListRequest.getEmail().isEmpty()){
            queryString+=" AND um.email=:email ";
            sqlParam.addValue("email",userListRequest.getEmail());
        }
        if(userListRequest.getMobile1() != null){
            queryString+=" AND um.mobile1=:mobile1 ";
            sqlParam.addValue("mobile1",userListRequest.getMobile1());
        }


        resultCount = count(queryString, sqlParam);
        if (userListRequest.getLimit() > 0){
            queryString += " LIMIT " +userListRequest.getLimit() + " OFFSET " + userListRequest.getOffSet();
        }

        List<UserInfoDto> userListRequests = namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(UserInfoDto.class));
        return new PageImpl<>(userListRequests,pageable,resultCount);
        //return namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(UserDto.class));



    }




    public UserInfoDto getUserByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT  um.id, um.first_name, um.middle_name, um.last_name, um.email, um.mobile_1 as mobile1, um.mobile_2 as mobile2,\n" +
                "um.designation_id as designationId , um.user_level_id, um.role_id as roleId , um.is_active as isactive, um.created_by, \n" +
                "um.created_on, um.updated_by, um.updated_on, um.contractor_id as contractorId,dm.name as designation,ulm.name as userLevel,\n" +
                "rm.name as role,cm.name as contractor\n" +
                "\tFROM rdvts_oltp.user_m as um \n" +
                "\tleft join rdvts_oltp.designation_m as dm on dm.id=um.designation_id\n" +
                "\tleft join rdvts_oltp.user_level_m as ulm on ulm.id=um.user_level_id\n" +
                "\tleft join rdvts_oltp.role_m as rm on rm.id=um.role_id\n" +
                "\tleft join rdvts_oltp.contractor_m cm on cm.id=um.contractor_id where um.is_active=true and um.id=:userId; ";

        sqlParam.addValue("userId", userId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(UserInfoDto.class));
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

    public boolean deactivateAreaByUserId(int id){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.user_area_mapping SET is_active = false WHERE user_id=:id";
        sqlParam.addValue("id",id);
        Integer update = namedJdbc.update(qry, sqlParam);
        return update>0;
    }





    public List<UserAreaMappingDto> getUserAreaMappingByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT am.id,am.user_id,am.g_dist_id as gdistId,geoDist.g_district_name as gDistName,am.dist_id,dist.district_name as distName,  " +
                "am.g_block_id as gblockId,geoblock.g_block_name as gBlockName,am.block_id,block.block_name as blockName,am.division_id,am.g_state_id as gStateId,geo.state_name as gStateName,  " +
                "am.state_id as stateId  " +
                "from rdvts_oltp.user_area_mapping as am " +
                "left join rdvts_oltp.geo_district_m as geoDist on geoDist.id = am.g_dist_id  " +
                "left join rdvts_oltp.district_boundary as dist on dist.dist_id = am.dist_id  " +
                "left join rdvts_oltp.geo_block_m as geoblock on geoblock.id = am.g_block_id  " +
                "left join rdvts_oltp.block_boundary as block on block.block_id = am.block_id  " +
                "left join rdvts_oltp.geo_state_m as geo on geo.id =am.g_state_id ";

        if(userId >0){
            qry +=" WHERE am.user_id=:userId ";
            sqlParam.addValue("userId",userId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UserAreaMappingDto.class));
    }


    public UserAreaMappingRequestDTO getStateByDistId(Integer distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT dist.state_id,state.state_name from rdvts_oltp.district_boundary as dist  " +
                      "left join rdvts_oltp.state_m  as state on state.id = dist.state_id WHERE dist.dist_id =:distId ";
        sqlParam.addValue("distId",distId);
        return namedJdbc.queryForObject(qry,sqlParam,new BeanPropertyRowMapper<>(UserAreaMappingRequestDTO.class));
    }

    public UserAreaMappingRequestDTO getStateDistByBlockId(Integer blockId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT block.dist_id, block.district_name,dist.state_id,state.state_name from rdvts_oltp.block_boundary as block " +
                "left join rdvts_oltp.district_boundary as dist on dist.dist_id = block.dist_id  " +
                "left join rdvts_oltp.state_m as state on state.id = dist.state_id  " +
                "WHERE block.gid =:blockId";
        sqlParam.addValue("blockId", blockId);
        return namedJdbc.queryForObject(qry,sqlParam,new BeanPropertyRowMapper<>(UserAreaMappingRequestDTO.class));
    }

    public UserAreaMappingRequestDTO getStateDistByDivisionId(Integer divisionId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT div.dist_id,dist.district_name,dist.state_id,state.state_name  from rdvts_oltp.division_m as div  " +
                "left join rdvts_oltp.district_boundary as dist on dist.dist_id= div.dist_id  " +
                "left join rdvts_oltp.state_m as state on state.id= dist.state_id   " +
                "WHERE  div.id =:divisionId ";
        sqlParam.addValue("divisionId",divisionId);
        return namedJdbc.queryForObject(qry,sqlParam,new BeanPropertyRowMapper<>(UserAreaMappingRequestDTO.class));
    }
    public List<Integer> getDistIdByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct dist_id from rdvts_oltp.user_area_mapping where user_id=:userId ";
        sqlParam.addValue("userId",userId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
    public List<Integer> getBlockIdByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct block_id from rdvts_oltp.user_area_mapping where user_id=:userId";
        sqlParam.addValue("userId",userId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
    public List<Integer> getDivisionByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct division_id from rdvts_oltp.user_area_mapping where user_id=:userId";
        sqlParam.addValue("userId",userId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
    public List<Integer> getDistrictByDivisionId(List<Integer> divisionId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select dist_id from rdvts_oltp.division_m where id in(:divisionId) ";
        sqlParam.addValue("divisionId",divisionId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
}

