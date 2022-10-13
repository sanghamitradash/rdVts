package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.UserLevelMaster;
import gov.orsac.RDVTS.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MasterRepositoryImpl implements MasterRepository {
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
    public List<RoleMenuInfo> getMenuByRoleId(int userId, int roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT rm.id as id,m.name, m.parent_id,module, rm.role_id, m.id as menuId, rm.is_active as active," +
                "rm.created_by, rm.updated_by, rm.is_default from rdvts_oltp.menu_m as m " +
                "left join rdvts_oltp.role_menu as rm ON rm.menu_id = m.id and rm.role_id=:roleId and " +
                "rm.is_active=true where m.is_active=true";
            sqlParam.addValue("roleId", roleId);
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleMenuInfo.class));

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
        String qry = "SELECT menu.id AS value, menu.name AS label,menu.parent_id, menu.module,roleMenu.is_default as isDefault "+
                " FROM  rdvts_oltp.menu_m as menu left join rdvts_oltp.role_menu as roleMenu on roleMenu.menu_id=menu.id WHERE parent_id = 0 "+
                " AND menu.is_active = true  order by menu.id ASC";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ParentMenuInfo.class));
    }

    public List<HierarchyMenuInfo> getHierarchyMenuListByIdWithoutRoleId(Integer parentId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT menu.id AS value, menu.name AS label,menu.parent_id, menu.module" +
//                " ,roleMenu.is_default as isDefault" +
                " FROM rdvts_oltp.menu_m as menu " +
                //" left join oiipcra_oltp.role_menu as roleMenu on menu.id=roleMenu.menu_id " +
                " WHERE parent_id =:parentId  AND menu.is_active = true ORDER BY menu.id ASC";
        sqlParam.addValue("parentId", parentId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(HierarchyMenuInfo.class));
    }
    public List<RoleMenuInfo> getRoleMenus(int userId, int roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT m.name, m.parent_id, m,module, rm.role_id, rm.menu_id, rm.is_active as active, rm.created_by, rm.updated_by, rm.is_default" +
                " FROM rdvts_oltp.role_menu as rm" +
                " LEFT JOIN menu_m as m ON rm.menu_id = m.id where true ";
        if (roleId == -1) {
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleMenuInfo.class));
        } else {
            qry += " AND role_id=:roleId ORDER BY rm.id";
            sqlParam.addValue("roleId", roleId);
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleMenuInfo.class));
        }
    }
    public Boolean deactivateMenu(int roleId, int menuId, boolean isActive){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE role_menu SET is_active = :isActive WHERE role_id =:roleId AND menu_id=:menuId";
        sqlParam.addValue("roleId",roleId);
        sqlParam.addValue("menuId",menuId);
        sqlParam.addValue("isActive",isActive);
        Integer update = namedJdbc.update(qry, sqlParam);
        return update > 0;
    }

    public VTUVendorMasterDto getVTUVendorById(Integer id){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry ="SELECT vtuM.id, vtuM.vtu_vendor_name, vtuM.vtu_vendor_address, vtuM.vtu_vendor_phone, vtuM.customer_care_number, " +
                "vtuM.is_active, vtuM.created_by, vtuM.created_on, vtuM.updated_by, vtuM.updated_on " +
                "FROM rdvts_oltp.vtu_vendor_m AS vtuM WHERE id =:id";
        sqlParam.addValue("id", id);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VTUVendorMasterDto.class));
    }

    public Page<VTUVendorMasterDto> getVTUVendorList(VTUVendorMasterDto vtuVendorMasterDto){
        UserDto userDto = new UserDto();
        userDto.setId(vtuVendorMasterDto.getUserId());


        PageRequest pageable = PageRequest.of(vtuVendorMasterDto.getPage(), vtuVendorMasterDto.getSize(), Sort.Direction.fromString(vtuVendorMasterDto.getSortOrder()), vtuVendorMasterDto.getSortBy());
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer  resultCount = 0;
        String queryString = " ";


        queryString = "SELECT vtuM.id, vtuM.vtu_vendor_name, vtuM.vtu_vendor_address, vtuM.vtu_vendor_phone, vtuM.customer_care_number, " +
                "vtuM.is_active, vtuM.created_by, vtuM.created_on, vtuM.updated_by, vtuM.updated_on, dev.id as deviceId " +
                "FROM rdvts_oltp.vtu_vendor_m AS vtuM  " +
                "LEFT JOIN rdvts_oltp.device_m AS dev ON dev.vtu_vendor_id=vtuM.id " +
                "WHERE vtuM.is_active = true";

        if(vtuVendorMasterDto.getId() != null && vtuVendorMasterDto.getId() > 0){
            queryString += " AND vtuM.id=:id ";
            sqlParam.addValue("id", vtuVendorMasterDto.getId());
        }

        if(vtuVendorMasterDto.getDeviceId() != null && vtuVendorMasterDto.getDeviceId() > 0){
            queryString += " AND dev.id=:deviceId ";
            sqlParam.addValue("deviceId", vtuVendorMasterDto.getDeviceId());
        }

        if(vtuVendorMasterDto.getVtuVendorName() != null){
            queryString += " AND vtuM.vtu_vendor_name LIKE(:vtuVendorName) ";
            sqlParam.addValue("vtuVendorName", vtuVendorMasterDto.getVtuVendorName());
        }

        queryString += " ORDER BY " + order.getProperty() + " " + order.getDirection().name();
        resultCount = count(queryString, sqlParam);
        queryString += " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

        List<VTUVendorMasterDto> vendorInfo = namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(VTUVendorMasterDto.class));
        return new PageImpl<>(vendorInfo,pageable,resultCount);

    }

    public List<DistrictBoundaryDto> getAllDistrict() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT dist.gid, dist.district_name as districtName,dist.district_code as districtCode,dist.state_name as stateName,  " +
                "dist.state_code as stateCode, " +
                "dist.dist_id as distId ,dist.state_id as stateId from rdvts_oltp.district_boundary as dist ";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DistrictBoundaryDto.class));
    }

    public List<BlockBoundaryDto> getBlockByDistId(Integer distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT block.gid,block.block_name,block.block_code,block.district_name,block.district_code,block.state_name,  " +
                     "block.state_code,block.dist_id,block.block_id from rdvts_oltp.block_boundary as block  " +
                     "where block.dist_id =:distId ";
        sqlParam.addValue("distId", distId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(BlockBoundaryDto.class));
    }

    public List<DivisionDto> getDivisionBlockByDistId(Integer distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT div.id, div.division_name as divisionName,div.dist_id as distId,dist.district_name as distName,div.division_id from rdvts_oltp.division_m as div     " +
                     "left join rdvts_oltp.district_boundary as dist on dist.dist_id = div.dist_id  "+
                     "where div.dist_id =:distId ";
        sqlParam.addValue("distId", distId);
        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DivisionDto.class));

    }
}

