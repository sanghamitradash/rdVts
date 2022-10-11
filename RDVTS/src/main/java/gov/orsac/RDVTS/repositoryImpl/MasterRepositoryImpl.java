package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.MenuDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MasterRepositoryImpl implements MasterRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    @Override
    public List<RoleDto> getRoleByRoleId(Integer roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id, name, description, parent_role_id,user_level_id, can_edit, can_add,can_view, can_delete, "+
                "can_approve,created_by,created_on,updated_by,updated_on," +
               " is_active as active FROM role_m ";
        if (roleId == -1) {
            qry += " WHERE id>1 ORDER BY id ";
        } else {
            qry += " WHERE id=:id ";
            sqlParam.addValue("id", roleId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleDto.class));
    }

    @Override
    public List<RoleDto> getRoleByUserLevelId(Integer userLevelId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        if (userLevelId == -1) {
            qry += "SELECT id, name, description, can_edit, can_add, can_delete, can_approve, user_level_id as userLevelId,parent_role_id as parentRoleId,is_active as active FROM role_m ";
                   /* " WHERE true AND id>1 Order BY id";*/
        } else {
            qry += "SELECT id, name, description, can_edit, can_add, can_delete, can_approve,user_level_id,parent_role_id as parentRoleId,is_active as active FROM role_m " +
                    " WHERE true AND user_level_id =:userLevelId  ";
                 /*   "AND id>1 ORDER BY id";*/
            sqlParam.addValue("userLevelId", userLevelId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleDto.class));
    }

    @Override
    public List<MenuDto> getMenu(Integer userId, Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id as id, name as name, parent_id as parentId, module as module,menu_order as menuOrder, is_active as active, created_by, created_on, updated_by, updated_on " +
                "FROM menu_m ";
        if (id == -1) {
            qry += " ORDER BY id ";
        } else {
            qry += " WHERE id=:id ";
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(MenuDto.class));
    }


}
