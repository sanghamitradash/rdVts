package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.repository.HelperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HelperRepositoryImpl implements HelperRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;
    @Override
    public Integer getUserLevelByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String query = "select user_level_id from rdvts_oltp.user_m where id=:userId ";
        sqlParam.addValue("userId",userId);
        return namedJdbc.queryForObject(query, sqlParam, Integer.class);
    }

    @Override
    public List<Integer> getLowerUserLevelIdsByUserLevelId(Integer userLevelId,Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String query = "WITH RECURSIVE user_level_org AS ( SELECT id, name, parent_id, is_active, created_by, updated_by " +
                "FROM  rdvts_oltp.user_level_m  WHERE id =:userLevelId UNION ALL SELECT e.id, e.name, e.parent_id, e.is_active, e.created_by," +
                "e.updated_by FROM rdvts_oltp.user_level_m e INNER JOIN user_level_org u ON u.id = e.parent_id )" +
                "SELECT id FROM user_level_org";
        sqlParam.addValue("userLevelId",userLevelId);
        return namedJdbc.queryForList(query, sqlParam, Integer.class);
    }

    @Override
    public List<Integer> getLowerUserByUserId(List<Integer> userLevelIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String query = "SELECT id FROM rdvts_oltp.user_m  where user_level_id in(:userLevelIds) OR user_level_id IN (5) ";
        sqlParam.addValue("userLevelIds",userLevelIds);
        return namedJdbc.queryForList(query, sqlParam, Integer.class);
    }

    @Override
    public List<DesignationDto> getLowerDesignation(Integer userId,Integer designationId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String query = "WITH RECURSIVE designation_org AS ( SELECT id, name,description, parent_id, is_active, created_by, updated_by " +
                "FROM  rdvts_oltp.designation_m  WHERE id =:designationId UNION ALL SELECT e.id, e.name,e.description, e.parent_id, e.is_active, e.created_by," +
                "e.updated_by FROM rdvts_oltp.designation_m e INNER JOIN designation_org u ON u.id = e.parent_id )" +
                "SELECT id,name,description,parent_id FROM designation_org ";
        sqlParam.addValue("designationId",designationId);
        return namedJdbc.query(query, sqlParam, new BeanPropertyRowMapper<>(DesignationDto.class));
    }

    @Override
    public List<RoleDto> getLowerRole(Integer userId,Integer roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String query = "WITH RECURSIVE role_org AS ( SELECT id, name, parent_role_id, is_active, created_by, updated_by " +
                "FROM  rdvts_oltp.role_m  WHERE id =:roleId UNION ALL SELECT e.id, e.name, e.parent_role_id, e.is_active, e.created_by," +
                "e.updated_by FROM rdvts_oltp.role_m e INNER JOIN role_org u ON u.id = e.parent_role_id )" +
                "SELECT id,name,parent_role_id FROM role_org ";
        sqlParam.addValue("roleId",roleId);
        return namedJdbc.query(query, sqlParam, new BeanPropertyRowMapper<>(RoleDto.class));
    }
}
