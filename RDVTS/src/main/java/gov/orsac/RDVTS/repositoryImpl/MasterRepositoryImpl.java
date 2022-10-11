package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.UserLevelMaster;
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

    @Override
    public List<UserLevelMaster> getUserLevelById(Integer userLevelId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id, name, parent_id, is_active as active, created_by, created_on, updated_by, updated_on " +
                "FROM rdvts_oltp.user_level_m ";
        if (userLevelId == -1) {
            qry += " ORDER BY id ";
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UserLevelMaster.class));
        } else {
            qry += " WHERE id=:id";
            sqlParam.addValue("id", userLevelId);
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UserLevelMaster.class));
        }
    }

    @Override
    public List<UserLevelMaster> getAllUserLevel(Integer userLevelId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String GetAllUserLevelQuery = String.format("WITH RECURSIVE user_level_org AS ( SELECT id," +
                " name, parent_id, is_active, created_by, updated_by FROM  rdvts_oltp.user_level_m  WHERE id = %s" +
                " UNION ALL" +
                " SELECT e.id, e.name, e.parent_id, e.is_active, e.created_by, e.updated_by" +
                " FROM rdvts_oltp.user_level_m e INNER JOIN user_level_org u ON u.id = e.parent_id )" +
                " SELECT * FROM user_level_org;", userLevelId);
        return namedJdbc.query(GetAllUserLevelQuery, sqlParam, new BeanPropertyRowMapper<>(UserLevelMaster.class));
    }

    public Integer getUserLevelIdByUserId(int userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT user_level_id FROM rdvts_oltp.user_m WHERE id =:userId";
        sqlParam.addValue("userId", userId);
        return namedJdbc.queryForObject(qry, sqlParam, Integer.class);
    }
    public List<RoleMenuInfo> getRoleMenu(int userId, int roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT rm.id,m.name, m.parent_id, m,module, rm.role_id, rm.menu_id, rm.is_active as active, rm.created_by, rm.updated_by, rm.is_default" +
                " FROM rdvts_oltp.role_menu as rm" +
                " LEFT JOIN menu_m as m ON rm.menu_id = m.id where rm.is_active=true ";
        if (roleId == -1) {
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleMenuInfo.class));
        } else {
            qry += " AND role_id=:roleId ORDER BY rm.id";
            sqlParam.addValue("roleId", roleId);
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleMenuInfo.class));
        }
    }

    public List<ParentMenuInfo> getAllParentMenu(Integer roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT menu.id AS value, menu.name AS label,menu.parent_id, menu.module,roleMenu.is_default as isDefault " +
                " FROM  rdvts_oltp.role_menu as roleMenu " +
                " left join  rdvts_oltp.menu_m as menu on menu.id=roleMenu.menu_id " +
                " WHERE parent_id = 0 AND menu.is_active = true";
        if(roleId > 0){
            qry+= " AND roleMenu.role_id=:roleId";
            sqlParam.addValue("roleId", roleId);
        }
        qry+= "  order by menu.order ASC";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ParentMenuInfo.class));
    }

    public List<HierarchyMenuInfo> getHierarchyMenuListById(Integer parentId, Integer roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT menu.id AS value, menu.name AS label,menu.parent_id, menu.module,roleMenu.is_default as isDefault" +
                " FROM rdvts_oltp.menu_m as menu " +
                " left join rdvts_oltp.role_menu as roleMenu on menu.id=roleMenu.menu_id " +
                " WHERE parent_id =:parentId  AND menu.is_active = true  And role_id=:roleId ORDER BY menu.order ASC";
        sqlParam.addValue("parentId", parentId);
        sqlParam.addValue("roleId", roleId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(HierarchyMenuInfo.class));
    }
    public List<ParentMenuInfo> getAllParentMenuWithoutRoleId() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT menu.id AS value, menu.name AS label,menu.parent_id, menu.module " +
                " FROM  rdvts_oltp.menu_m as menu " +
//                " left join  rdvts_oltp.menu_m as menu on menu.id=roleMenu.menu_id " +
                " WHERE parent_id = 0 AND menu.is_active = true";
        qry+= "  order by menu.id ASC";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ParentMenuInfo.class));
    }

    public List<HierarchyMenuInfo> getHierarchyMenuListByIdWithoutRoleId(Integer parentId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT menu.id AS value, menu.name AS label,menu.parent_id, menu.module" +
                //" ,roleMenu.is_default as isDefault" +
                " FROM rdvts_oltp.menu_m as menu " +
                //" left join rdvts_oltp.role_menu as roleMenu on menu.id=roleMenu.menu_id " +
                " WHERE parent_id =:parentId  AND menu.is_active = true ORDER BY menu.id ASC";
        sqlParam.addValue("parentId", parentId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(HierarchyMenuInfo.class));
    }

}
