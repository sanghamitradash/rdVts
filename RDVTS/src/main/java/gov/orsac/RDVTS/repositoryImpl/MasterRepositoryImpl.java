package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.UserLevelMaster;
import gov.orsac.RDVTS.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public List<RoleDto> getRoleByUserLevelIdForList(Integer userLevelId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        if (userLevelId == -1) {
            qry += "SELECT role.id, role.name, role.description, role.can_edit, role.can_add, role.can_delete, role.can_approve, " +
                    "role.user_level_id as userLevelId,level.name as userLevelName,role.parent_role_id as parentRoleId,role1.name parentRoleName,role.is_active as active " +
                    "FROM rdvts_oltp.role_m  as role join rdvts_oltp.role_m  as role1 on role1.id=role.parent_role_id " +
                    "left join rdvts_oltp.user_level_m as level " +
                    "on level.id=role.user_level_id ";
            /* " WHERE true AND id>1 Order BY id";*/
        } else {
            qry += "SELECT role.id, role.name, role.description, role.can_edit, role.can_add, role.can_delete, role.can_approve, " +
            "role.user_level_id as userLevelId,level.name as userLevelName,role.parent_role_id as parentRoleId,role1.name parentRoleName,role.is_active as active " +
            "FROM rdvts_oltp.role_m  as role join rdvts_oltp.role_m  as role1 on role1.id=role.parent_role_id " +
            "left join rdvts_oltp.user_level_m as level " +
            "on level.id=role.user_level_id WHERE role.is_active=true AND role.user_level_id =:userLevelId  ";
            /*   "AND id>1 ORDER BY id";*/
            sqlParam.addValue("userLevelId", userLevelId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleDto.class));
    }

    @Override
    public List<MenuDto> getMenu(Integer userId, Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id as id, name as name, parent_id as parentId, module as module,menu_order as menuOrder, is_active as active,menu_icon as menuIcon,created_by, created_on, updated_by, updated_on " +
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
        String qry = "SELECT rm.id,m.name, m.parent_id, m,module, rm.role_id, rm.menu_id, rm.is_active as active, rm.created_by, rm.updated_by, rm.is_default,m.menu_icon as menuIcon " +
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
        String qry ="SELECT rm.id as id,m.name, m.parent_id,module, rm.role_id, m.id as menuId, rm.is_active as active,m.menu_icon as menuIcon, " +
                "rm.created_by, rm.updated_by, rm.is_default from rdvts_oltp.menu_m as m " +
                "left join rdvts_oltp.role_menu as rm ON rm.menu_id = m.id and rm.role_id=:roleId and " +
                "rm.is_active=true where m.is_active=true";
            sqlParam.addValue("roleId", roleId);
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoleMenuInfo.class));

    }

    public List<ParentMenuInfo> getAllParentMenu(Integer roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT menu.id AS value, menu.name AS label,menu.parent_id, menu.module,roleMenu.is_default as isDefault,menu.menu_icon as menuIcon " +
                " FROM  rdvts_oltp.role_menu as roleMenu " +
                " left join  rdvts_oltp.menu_m as menu on menu.id=roleMenu.menu_id " +
                " WHERE parent_id = 0 AND menu.is_active = true";
        if(roleId >= 0){
            qry+= " AND roleMenu.role_id=:roleId";
            sqlParam.addValue("roleId", roleId);
        }
        qry+= "  order by menu.menu_order ASC";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ParentMenuInfo.class));
    }

    public List<HierarchyMenuInfo> getHierarchyMenuListById(Integer parentId, Integer roleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
            String qry = "SELECT menu.id AS value, menu.name AS label,menu.parent_id, menu.module,roleMenu.is_default as isDefault,menu.menu_icon as menuIcon " +
                    " FROM rdvts_oltp.menu_m as menu " +
                    " left join rdvts_oltp.role_menu as roleMenu on menu.id=roleMenu.menu_id " +
                    " WHERE parent_id =:parentId  " +
                    " AND menu.is_active = true  And role_id=:roleId ORDER BY menu.menu_order ASC";
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

    public List<VTUVendorMasterDto> getVTUVendorById(Integer id, Integer userId){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<VTUVendorMasterDto> vendor;
        String qry ="SELECT vtuM.id, vtuM.vtu_vendor_name, vtuM.vtu_vendor_address, vtuM.vtu_vendor_phone, vtuM.customer_care_number, " +
                "vtuM.is_active, vtuM.created_by, vtuM.created_on, vtuM.updated_by, vtuM.updated_on  " +
                "FROM rdvts_oltp.vtu_vendor_m AS vtuM " +
//                "LEFT JOIN rdvts_oltp.device_m AS dev ON dev.vtu_vendor_id=vtuM.id " +
                "WHERE vtuM.is_active = true ";

        if(id>0){
            qry+=" AND vtuM.id=:id";
        }
        sqlParam.addValue("id", id);
        sqlParam.addValue("userId",userId);
        try{
            vendor = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VTUVendorMasterDto.class));
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return vendor;
    }

    public Page<VTUVendorMasterDto> getVTUVendorList(VTUVendorFilterDto vtuVendorFilterDto){
//        UserDto userDto = new UserDto();
//        userDto.setId(vtuVendorMasterDto.getUserId());

        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        pageable = PageRequest.of(vtuVendorFilterDto.getDraw()-1,vtuVendorFilterDto.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;
        String queryString = " ";
        queryString = "SELECT DISTINCT vtuM.id, vtuM.vtu_vendor_name, vtuM.vtu_vendor_address, vtuM.vtu_vendor_phone, vtuM.customer_care_number, " +
                "vtuM.is_active, vtuM.created_by, vtuM.created_on, vtuM.updated_by, vtuM.updated_on " +
                "FROM rdvts_oltp.vtu_vendor_m as vtuM " +
                "Left join rdvts_oltp.device_m as dev on dev.vtu_vendor_id=vtuM.id and dev.is_active=true " +
                "where vtuM.is_active=true ";

        if(vtuVendorFilterDto.getVendorId() != null && vtuVendorFilterDto.getVendorId() > 0){
            queryString += " AND vtuM.id=:vendorId ";
            sqlParam.addValue("vendorId", vtuVendorFilterDto.getVendorId());
        }

        if(vtuVendorFilterDto.getDeviceId() != null && vtuVendorFilterDto.getDeviceId() > 0){
            queryString += " AND dev.id=:deviceId ";
            sqlParam.addValue("deviceId", vtuVendorFilterDto.getDeviceId());
        }


//        if (deviceIds != null && !deviceIds.isEmpty()) {
//            queryString += " AND dev.id IN (:deviceIds)";
//            sqlParam.addValue("deviceIds", deviceIds);
//        }


//        if(vtuVendorFilterDto.getDeviceId() != null && vtuVendorFilterDto.getDeviceId() > 0){
//            queryString += " AND dev.id=:deviceId ";
//            sqlParam.addValue("deviceId", vtuVendorFilterDto.getDeviceId());
//        }

        if(vtuVendorFilterDto.getVtuVendorName() != null){
            queryString += " AND vtuM.vtu_vendor_name LIKE(:vtuVendorName) ";
            sqlParam.addValue("vtuVendorName", vtuVendorFilterDto.getVtuVendorName());
        }
//        if(vtuVendorFilterDto.getUserId() != null && vtuVendorFilterDto.getUserId() > 0){
//            sqlParam.addValue("userId", vtuVendorFilterDto.getUserId());
//        }

        resultCount = count(queryString, sqlParam);
        if (vtuVendorFilterDto.getLimit() > 0){
            queryString += " Order by vtuM.id desc LIMIT " +vtuVendorFilterDto.getLimit() + " OFFSET " + vtuVendorFilterDto.getOffSet();
        }

        List<VTUVendorMasterDto> list = namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(VTUVendorMasterDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }

    @Override
    public List<Integer> getDeviceByVendorId(Integer deviceId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select dev.id, dev.model_name  " +
                "from rdvts_oltp.device_m as dev  " +
                "where dev.vtu_vendor_id=deviceId";
        sqlParam.addValue("deviceId", deviceId);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
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

    public List<Integer> listOfBlockIdsByDistId(int blockId, int distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT DISTINCT block_id FROM user_area_mapping WHERE user_id=:userId AND dist_id=:distId";
        sqlParam.addValue("userId", blockId);
        sqlParam.addValue("distId", distId);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<DivisionDto> getDivisionByDistId(Integer distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT div.id, div.division_name as divisionName,div.dist_id as distId,dist.district_name as distName,div.division_id from rdvts_oltp.division_m as div     " +
                     "left join rdvts_oltp.district_boundary as dist on dist.dist_id = div.dist_id  "+
                     "where div.dist_id =:distId ";
        sqlParam.addValue("distId", distId);
        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DivisionDto.class));

    }

    public List<StateDto> getAllState() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT state.id, state.state_name from rdvts_oltp.state_m as state ";
        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(StateDto.class));
    }

    public List<DistrictBoundaryDto> getDistByStateId(Integer stateId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT dist.dist_id, dist.district_name from rdvts_oltp.district_boundary as dist  " +
                "left join rdvts_oltp.state_m as state on state.id = dist.state_id  " +
                "where state.id=:stateId ";
        sqlParam.addValue("stateId", stateId);
        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DistrictBoundaryDto.class));
    }

    public List<BlockBoundaryDto> getListOfBlockByListOfDistId(List<Integer> distIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT block.gid,block.block_name,block.block_code,block.district_name,block.district_code,block.state_name,  " +
                     "block.state_code,block.dist_id,block.block_id from rdvts_oltp.block_boundary as block  ";

        if (distIds != null && !distIds.isEmpty()) {
            qry += " WHERE block.dist_id IN (:distIds)";
            sqlParam.addValue("distIds", distIds);
        }
       return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(BlockBoundaryDto.class));
    }
    public List<Integer> getListOfBlockIds(List<Integer> distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT DISTINCT block.block_id FROM rdvts_oltp.block_boundary as block  WHERE block.dist_id=:distId";
        sqlParam.addValue("distId", distId);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<DivisionDto> getListOfDivisionByListOfDistId(List<Integer> distIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT div.id, div.division_name as divisionName,div.dist_id as distId,dist.district_name as distName,div.division_id from rdvts_oltp.division_m as div   " +
                "left join rdvts_oltp.district_boundary as dist on dist.dist_id = div.dist_id" ;

        if (distIds != null && !distIds.isEmpty()) {
            qry += " WHERE div.dist_id IN (:distIds)";
            sqlParam.addValue("distIds", distIds);
        }
        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DivisionDto.class));
    }
    public List<Integer> getVehicleByContractorIdList(List<Integer> contractorIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct vehicle_id from rdvts_oltp.vehicle_owner_mapping where contractor_id  in(:contractorIds)" ;
            sqlParam.addValue("contractorIds", contractorIds);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }

    public Boolean deactivateVendor(Integer vendorId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.vtu_vendor_m  " +
                "SET is_active=false WHERE id=:vendorId  ";
        sqlParam.addValue("vendorId", vendorId);
        sqlParam.addValue("userId", userId);

        int update = namedJdbc.update(qry, sqlParam);
        boolean result = false;
        if (update > 0) {
            result = true;
        }
        return result;
    }

    public List<Integer> getDeviceIdsByDistIds(List<Integer> distIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct device_id from rdvts_oltp.device_area_mapping where dist_id in(:distIds) ";
        sqlParam.addValue("distIds", distIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getDeviceIdsByBlockIds(List<Integer> blockIds) {
       MapSqlParameterSource sqlParam = new MapSqlParameterSource();
       String qry = "select distinct device_id from rdvts_oltp.device_area_mapping where block_id in (:blockIds)           ";
       sqlParam.addValue("blockIds", blockIds);
       return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getDeviceIdsByDivisionIds(List<Integer> divisionIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct device_id from rdvts_oltp.device_area_mapping where division_id  in(:divisionIds)           ";
        sqlParam.addValue("divisionIds",divisionIds);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }

    public List<Integer> getDeviceIdsByBlockAndDivision(List<Integer> blockIds, List<Integer> divisionIds,List<Integer>distIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct device_id from rdvts_oltp.device_area_mapping where block_id in (:blockIds) OR division_id in (:divisionIds) OR dist_id in (:distIds) ";
        sqlParam.addValue("blockIds",blockIds);
        sqlParam.addValue("divisionIds",divisionIds);
        sqlParam.addValue("distIds",distIds);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
}



