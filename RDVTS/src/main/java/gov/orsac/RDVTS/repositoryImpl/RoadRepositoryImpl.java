package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoadEntity;
import gov.orsac.RDVTS.entities.RoadLocationEntity;
import gov.orsac.RDVTS.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class RoadRepositoryImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;



    @Autowired
    private MasterRepositoryImpl masterRepositoryImpl;

    public int count(String qryStr, MapSqlParameterSource sqlParam) {
        String sqlStr = "SELECT COUNT(*) from (" + qryStr + ") as t";
        Integer intRes = namedJdbc.queryForObject(sqlStr, sqlParam, Integer.class);
        if (null != intRes) {
            return intRes;
        }
        return 0;
    }

    public List<RoadMasterDto> getRoadById(Integer roadId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadMasterDto> road;
//        String qry = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, ST_AsGeoJSON(road.geom) as geom, road.road_width, road.g_road_id as groadId, " +
//                "road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, road.completed_road_length, road.sanction_date, road.road_code, " +
//                "road.road_status, road.approval_status, road.approved_by , piu.name as piuName " +
//                "FROM rdvts_oltp.geo_construction_m AS road " +
//                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.road_id=road.id and geom.is_active=true " +
//                "LEFT JOIN rdvts_oltp.piu_id as piu on piu.id=geom.piu_id and piu.is_active=true " +
//                "WHERE road.is_active=true ";
        String qry ="SELECT distinct road.id, road.road_name, pm.id as package_id, pm.package_no as package_name, road.sanction_length as road_length, road.road_location, road.road_allignment, \n" +
                "ST_AsGeoJSON(road.geom) as geom, road.road_width,road.g_road_id as groadId, road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on,\n" +
                "gm.completed_road_length, gm.sanction_date, road.road_code, road.road_status, road.approval_status, road.approved_by , pium.name as piuName \n" +
                "from rdvts_oltp.road_m as road\n" +
                "left join rdvts_oltp.geo_mapping as gm on gm.road_id = road.id and gm.is_active=true \n" +
                "left join rdvts_oltp.package_m as pm on pm.id = gm.package_id and pm.is_active=true \n" +
                "left join rdvts_oltp.piu_id as pium on pium.id = gm.piu_id and pium.is_active=true \n" +
                "where road.is_active=true ";

        if (roadId > 0) {
            qry += " AND road.id=:roadId";
        }
        sqlParam.addValue("roadId", roadId);
        sqlParam.addValue("userId", userId);
        try {
            road = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return road;
    }

    public List<RoadMasterDto> getRoadWorkById(Integer workId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadMasterDto> road;
//        String qry = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, road.road_width,road.geom ,road.g_road_id as groadId,wm.id as work_id, road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, road.completed_road_length, road.sanction_date, road.road_code, " +
//                "road.road_status, road.approval_status, road.approved_by  " +
//                "FROM rdvts_oltp.geo_construction_m AS road " +
//                "LEFT JOIN rdvts_oltp.geo_master AS gm ON gm.road_id=road.id " +
//                "left join rdvts_oltp.work_m as wm on wm.id=gm.work_id " +
//                "WHERE road.is_active=true  ";
        String qry = " select road.id ,gm.package_id , pm.package_no as package_name, road.road_name , road.sanction_length as road_length,road.road_location, road.road_allignment, \n" +
                " road.road_width,road.geom ,road.g_road_id as groadId, gm.package_id as work_id, road.is_active, road.created_by, road.created_on, road.updated_by,\n" +
                " road.updated_on, gm.completed_road_length, road.sanction_date, road.road_code, road.road_status, road.approval_status, road.approved_by \n" +
                " from rdvts_oltp.road_m as road \n" +
                " left join rdvts_oltp.geo_mapping as gm on gm.road_id = road.id and gm.is_active ='true'\n" +
                " left join rdvts_oltp.package_m as pm on pm.id = gm.package_id and pm.is_active ='true'\n" +
                " where road.is_active = 'true' ";
        if (workId > 0) {
//            qry += " and wm.id= :id";
            qry += " and pm.id =:id";
        }
        sqlParam.addValue("id", workId);
        try {
            road = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return road;
    }

    public Page<RoadMasterDto> getRoadList(RoadFilterDto roadFilterDto) {
//        UserDto userDto = new UserDto();
//        userDto.setId(vtuVendorMasterDto.getUserId());

        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        PageRequest pageable = null;

        int pageNo = roadFilterDto.getOffSet() / roadFilterDto.getLimit();
        PageRequest pageable = PageRequest.of(pageNo, roadFilterDto.getLimit(), Sort.Direction.fromString("asc"), "id");
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");

        int resultCount = 0;

//        String queryString = "SELECT DISTINCT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, road.road_width, road.g_road_id as groadId, " +
//                "road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, geom.g_work_id as workIds, geom.contractor_id as contractIds, road.completed_road_length, " +
//                "road.sanction_date, road.road_code, road.road_status, road.approval_status, road.approved_by, " +
//                " case when (road.geom is not null) then TRUE else FALSE end as geomPresent  " +
//                "FROM rdvts_oltp.geo_construction_m AS road " +
//                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.road_id=road.id and geom.is_active = true " +
//                "LEFT JOIN rdvts_oltp.work_m as wm on wm.id=geom.work_id and wm.is_active = true " +
//                "WHERE road.is_active = true    ";
        String queryString = "select distinct road.id ,road.road_name, pm.package_no as package_name, road.sanction_length as road_length,case when (road.geom is not null) then TRUE else FALSE end as geomPresent  " +
                "from rdvts_oltp.road_m as road\n" +
                " left join rdvts_oltp.geo_mapping as gm on road.id = gm.road_id " +
                " left join rdvts_oltp.package_m as pm on gm.package_id = pm.id " +
                " where road.is_active = 't' and gm.is_active = 't' and pm.is_active = 't' ";

//        String subQuery = "";

        if (roadFilterDto.getId() != null && roadFilterDto.getId() > 0) {
            queryString += " AND road.id=:id ";
            sqlParam.addValue("id", roadFilterDto.getId());
        }

        if (roadFilterDto.getRoadName() != null) {
            queryString += " AND road.road_name LIKE(:roadName) ";
            sqlParam.addValue("roadName", roadFilterDto.getRoadName());
        }

//        if (roadFilterDto.getRoadLength() != null && roadFilterDto.getRoadLength() > 0) {
//            queryString += " AND road.road_length=:roadLength ";
//            sqlParam.addValue("roadLength", roadFilterDto.getRoadLength());
//        }

//        if (roadFilterDto.getRoadLocation() != null && roadFilterDto.getRoadLocation() > 0) {
//            queryString += " AND road.road_location=:roadLocation ";
//            sqlParam.addValue("roadLocation", roadFilterDto.getRoadLocation());
//        }

//        if (roadFilterDto.getWorkIds() != null && !roadFilterDto.getWorkIds().isEmpty()) {
//            queryString += " AND geom.g_work_id IN (:workIds)";
//            sqlParam.addValue("workIds", roadFilterDto.getWorkIds());
//        }

//        if (roadFilterDto.getContractIds() != null && !roadFilterDto.getContractIds().isEmpty()) {
//            queryString += " AND geom.contractor_id IN (:contractIds)";
//            sqlParam.addValue("contractIds", roadFilterDto.getContractIds());
//        }
        if (roadFilterDto.getWorkIds() != null && !roadFilterDto.getWorkIds().isEmpty()) {
            queryString += " AND gm.package_id  IN (:workIds)";
            sqlParam.addValue("packageIds", roadFilterDto.getWorkIds());
        }

        if (roadFilterDto.getActivityIds() != null && !roadFilterDto.getActivityIds().isEmpty()) {
            queryString += " AND am.id IN (:activityids)";
            sqlParam.addValue("activityids", roadFilterDto.getActivityIds());
        }

        if (roadFilterDto.getDistIds() != null && !roadFilterDto.getDistIds().isEmpty()) {
//            queryString += " AND geom.dist_id IN (:distIds)";
            queryString += " AND gm.dist_id IN (:distIds)";
            sqlParam.addValue("distIds", roadFilterDto.getDistIds());
        }

//        if (roadFilterDto.getDivisionIds() != null && !roadFilterDto.getDivisionIds().isEmpty()) {
//            queryString += " AND geom.division_id IN (:divisionIds)";
//            sqlParam.addValue("divisionIds", roadFilterDto.getDivisionIds());
//        }

        UserInfoDto user = userRepositoryImpl.getUserByUserId(roadFilterDto.getUserId());
        if (user.getUserLevelId() == 5) {
//                queryString += " AND geom.contractor_id=:contractorId ";
                queryString += " AND road.id in (select gm.road_id from  rdvts_oltp.geo_mapping as gm\n" +
                        "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id = gm.id\n" +
                        "left join rdvts_oltp.vehicle_owner_mapping as vom on vam.vehicle_id = vom.vehicle_id\n" +
                        "left join rdvts_oltp.contractor_m as cm on cm.id = vom.contractor_id\n" +
                        "where vom.contractor_id = :contractorId)";
                sqlParam.addValue("contractorId", user.getUserLevelId());
        }

        else if (user.getUserLevelId() == 2) {
            List<Integer> distIds = userRepositoryImpl.getDistIdByUserId(roadFilterDto.getUserId());
            List<Integer> blockIds = userRepositoryImpl.getBlockIdByDistId(distIds);
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByDistId(distIds);
            List<Integer> roadIds = masterRepositoryImpl.getRoadIdsByBlockAndDivision(blockIds, divisionIds, distIds);
            if (queryString != null && queryString.length() > 0) {
                queryString += " AND  road.id in(:roadIds) ";
                sqlParam.addValue("roadIds", roadIds);
            } else {
                queryString += " WHERE road.id in(:roadIds) ";
                sqlParam.addValue("roadIds", roadIds);
            }
        }

        else if(user.getUserLevelId()==3){
            List<Integer> blockIds=userRepositoryImpl.getBlockIdByUserId(roadFilterDto.getUserId());
            List<Integer> roadIds  = masterRepositoryImpl.getRoadIdsByBlockIds(blockIds);
            if(queryString != null && queryString.length() > 0){
                queryString += " AND  road.id in(:roadIds) ";
                sqlParam.addValue("roadIds",roadIds);
            } else {
                queryString += " WHERE  road.id in(:roadIds) ";
                sqlParam.addValue("roadIds",roadIds);
            }
        }

        else if(user.getUserLevelId()==4){
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByUserId(roadFilterDto.getUserId());
            List<Integer> roadIds = masterRepositoryImpl.getRoadIdsByDivisionId(divisionIds);
            if(queryString != null && queryString.length() > 0){
                queryString += " AND  road.id in(:roadIds) ";
                sqlParam.addValue("roadIds", roadIds);
            } else {
                queryString += " where  road.id in(:roadIds) ";
                sqlParam.addValue("roadIds", roadIds);
            }
        }

        resultCount = count(queryString, sqlParam);
        if (roadFilterDto.getLimit() > 0) {
            queryString += " Order by road.id desc LIMIT " + roadFilterDto.getLimit() + " OFFSET " + roadFilterDto.getOffSet();
        }

        List<RoadMasterDto> list = namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }

    public List<GeoMasterDto> getWorkByroadIds(Integer roadId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on " +
                " FROM rdvts_oltp.geo_master where is_active=true  ";
        /*   "AND id>1 ORDER BY id";*/

        if (roadId > 0) {
            qry += " and road_id =:roadId ";
            sqlParam.addValue("roadId", roadId);
        }

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
    }

    public List<GeoMasterDto> workByContractorIds(Integer contractorId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                "\tFROM rdvts_oltp.geo_master where is_active=true  ";
        /*   "AND id>1 ORDER BY id";*/
        if (contractorId > 0) {
            qry += " and contractor_id =:contractorId ";
            sqlParam.addValue("contractorId", contractorId);
        }

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
    }

    public List<GeoMasterDto> getworkByDistrictId(Integer districtId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on " +
                " FROM rdvts_oltp.geo_master where is_active=true  ";
        /*   "AND id>1 ORDER BY id";*/
        if (districtId > 0) {
            qry += " and dist_id =:districtId";
            sqlParam.addValue("districtId", districtId);
        }

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
    }

    public List<GeoMasterDto> getworkByBlockId(Integer blockId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                " FROM rdvts_oltp.geo_master where is_active=true  ";
        /*   "AND id>1 ORDER BY id";*/
        if (blockId > 0) {
            qry += " and block_id =:blockId";
            sqlParam.addValue("blockId", blockId);
        }

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
    }

    public List<GeoMasterDto> getworkByDivisionId(Integer divisionId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                "\tFROM rdvts_oltp.geo_master where is_active=true  ";
        /*   "AND id>1 ORDER BY id";*/ // add division Id here

        if (divisionId > 0) {
            qry += "   and division_id =:divisionId;";
            sqlParam.addValue("divisionId", divisionId);
        }
        sqlParam.addValue("divisionId", divisionId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
    }


    public List<RoadMasterDto> getGeomByRoadId(Integer roadId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadMasterDto> road;
        String qry = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, ST_AsGeoJSON(road.geom) as geom, road.road_width, road.g_road_id as groadId,  " +
                " road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, road.completed_road_length, road.sanction_date, road.road_code, \n" +
                "road.road_status, road.approval_status, road.approved_by  " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.road_id=road.id " +
                "WHERE road.is_active=true ";
        if (roadId > 0) {
            qry += " AND road.id=:roadId";
        }
        sqlParam.addValue("roadId", roadId);
        sqlParam.addValue("userId", userId);
        try {
            road = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return road;
    }


    public List<GeoMasterDto> getVehicleListByRoadId(Integer roadId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                "\tFROM rdvts_oltp.geo_master where is_active=true and road_id =:roadId; ";
        /*   "AND id>1 ORDER BY id";*/
        sqlParam.addValue("roadId", roadId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
    }

    public List<RoadWorkMappingDto> getWorkDetailsByRoadId(Integer roadId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadWorkMappingDto> road;
        String qry = "SELECT road.id as roadId, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, road.road_width,road.g_road_id as gRoadId, " +
                " workm.id as workId, workm.g_work_id as gWorkId, workm.g_work_name as gWorkName, workm.is_active as isActive, workm.created_by as createdBy, workm.created_on as createdOn, " +
                "workm.updated_by as updatedBy, workm.updated_on as updatedOn, piu.name as piuName " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS gm ON gm.road_id = road.id  " +
                "LEFT JOIN rdvts_oltp.work_m AS workm ON workm.g_work_id = gm.g_work_id " +
                "LEFT JOIN rdvts_oltp.piu_id AS piu ON piu.gpiu_id=gm.g_piu_id " +
                "WHERE road.is_active = true ";
        if (roadId > 0) {
            qry += " AND road.id=:roadId";
        }
        sqlParam.addValue("roadId", roadId);
        sqlParam.addValue("userId", userId);
        try {
            road = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadWorkMappingDto.class));

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return road;
    }

    public List<Integer> getDistIdsByRoadId(List<Integer> id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "   select gm.dist_id from rdvts_oltp.geo_master as gm   " +
                "  left join rdvts_oltp.geo_construction_m as road on road.id = gm.road_id  " +
                "  where road.id=:id and gm.is_active=true ";
        sqlParam.addValue("id", id);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getRoadIdsByWorkId(List<Integer> workIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct gm.road_id from rdvts_oltp.geo_master as gm  " +
                " where gm.work_id in (:workIds) ";
        sqlParam.addValue("workIds", workIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getRoadIdsBydistIds(List<Integer> distIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct gm.road_id from rdvts_oltp.geo_master as gm  " +
                " where gm.dist_id in (:distIds) ";
        sqlParam.addValue("distIds", distIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getRoadIdsByblockIds(List<Integer> blockIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct gm.road_id from rdvts_oltp.geo_master as gm  " +
                " where gm.block_id in (:blockIds)";
        sqlParam.addValue("blockIds", blockIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getRoadIdsByVehicleId(List<Integer> vehicleIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct gm.road_id from rdvts_oltp.geo_master as gm  " +
                "left join rdvts_oltp.work_m as wm on wm.id=gm.work_id " +
                "left join rdvts_oltp.activity_m as am on am.work_id=wm.id " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id=am.id " +
                "left join rdvts_oltp.vehicle_m as vm on vm.id=vam.vehicle_id where vm.id in (:vehicleId)";
        sqlParam.addValue("vehicleIds", vehicleIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getRoadIdsByVehicleIdsForFilter(List<Integer> vehicleIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = "select distinct road_id from rdvts_oltp.geo_master where work_id in (select distinct work_id from rdvts_oltp.activity_m where id in \n" +
//                " (select distinct activity_id from rdvts_oltp.vehicle_activity_mapping where vehicle_id in (:vehicleIds) and is_active=true))";
        String qry = "SELECT distinct gm.road_id from rdvts_oltp.geo_mapping gm\n" +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id = gm.id and vam.is_active= 'true'\n" +
                "where gm.is_active = 'true' and vam.vehicle_id in(:vehicleIds)";
        sqlParam.addValue("vehicleIds", vehicleIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getRoadIdsByActivityIdsForFilter(List<Integer> activityIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = " select distinct road_id from rdvts_oltp.geo_master where work_id in (select distinct work_id from rdvts_oltp.activity_m where id in (:activityIds)) ";
        String qry = " select distinct road_id from rdvts_oltp.geo_mapping where activity_id in(:activityIds) and is_active = 'true'";
        sqlParam.addValue("activityIds", activityIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getRoadIdsByDeviceIdsForFilter(List<Integer> deviceIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = " select distinct road_id from rdvts_oltp.geo_master where work_id in (select distinct work_id from rdvts_oltp.activity_m where is_active=true and id in " +
//                "(select distinct activity_id from rdvts_oltp.vehicle_activity_mapping where is_active=true and vehicle_id in " +
//                " (select distinct vehicle_id from rdvts_oltp.vehicle_device_mapping where device_id in(:deviceIds)) and is_active=true)) ";
        String qry = " SELECT distinct gm.road_id from rdvts_oltp.geo_mapping gm\n" +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id = gm.id and vam.is_active= 'true'\n" +
                "left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.vehicle_id = vam.vehicle_id and vdm.is_active ='true'\n" +
                "where gm.is_active = 'true' and vdm.device_id in (:deviceIds))  ";
        sqlParam.addValue("deviceIds", deviceIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<RoadMasterDto> getRoadByRoadIds(List<Integer> roadIdList, List<Integer> workIds, List<Integer> distIds, List<Integer> blockIds, List<Integer> vehicleIds, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadMasterDto> road;
//        String qry = "SELECT distinct road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, ST_AsGeoJSON(road.geom) as geom, road.road_width, road.g_road_id as groadId, " +
//                " road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, road.completed_road_length, road.sanction_date as sanctionDate, road.road_code, " +
//                " road.road_status, road.approval_status, road.approved_by, geom.work_id as workIds, geom.g_dist_id as distIds, geom.g_block_id as blockIds " +
//                " FROM rdvts_oltp.geo_construction_m AS road " +
//                " LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.road_id=road.id " +
//                " LEFT JOIN rdvts_oltp.work_m as work on work.id = geom.work_id  " +
//                " WHERE road.is_active=true ";
        String qry = "select distinct road.id ,gm.package_id , pm.package_no as package_name, road.road_name , road.sanction_length as road_length, road.road_location, road.road_allignment, \n" +
                " road.road_width,ST_AsGeoJSON(road.geom) as geom ,road.g_road_id as groadId, road.is_active, road.created_by, road.created_on, road.updated_by,\n" +
                " road.updated_on, gm.completed_road_length, road.sanction_date as sanctionDate, road.road_code, road.road_status, road.approval_status, road.approved_by, gm.package_id as workIds, gm.dist_id as distIds\n" +
                " from rdvts_oltp.road_m as road \n" +
                " left join rdvts_oltp.geo_mapping as gm on gm.road_id = road.id and gm.is_active ='true'\n" +
                " left join rdvts_oltp.package_m as pm on pm.id = gm.package_id and pm.is_active ='true'\n" +
                " where road.is_active = 'true'  ";

        if (roadIdList != null && roadIdList.size() > 0 && !roadIdList.isEmpty()) {
//            qry += " AND road.id IN (:roadIdList)";
            qry += " AND road.id IN (:roadIdList) ";
            sqlParam.addValue("roadIdList", roadIdList);
        }
        if (workIds != null && !workIds.isEmpty()) {
//            qry += " AND geom.work_id IN (:workIds)";
            qry += " AND gm.package_id IN (:workIds) ";
            sqlParam.addValue("workIds", workIds);
        }
        if (distIds != null && distIds.size() > 0) {
            qry += " AND gm.dist_id IN (:distIds) ";
            sqlParam.addValue("distIds", distIds);
        }
//        if (blockIds != null && blockIds.size() > 0) {
//            qry += " AND geom.g_block_id IN (:blockIds)";
//            sqlParam.addValue("blockIds", blockIds);
//        }
        try {
            road = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return road;
    }

    public RoadStatusDropDownDto getRoadStatusDD(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT rs.id, rs.name, rs.is_active , rs.created_by, rs.created_on, rs.updated_by, rs.updated_on " +
                " FROM rdvts_oltp.road_status_m as rs order by rs.name";
        sqlParam.addValue("userId", userId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(RoadStatusDropDownDto.class));
    }

    public int updateGeom(Integer roadId, String geom, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = "UPDATE rdvts_oltp.geo_construction_m " +
//                "SET geom=st_setsrid(ST_GeomFromGeoJSON('" + geom + "'),4326)  " +
//                "WHERE id=:roadId and is_active=true ;";
        String qry = "UPDATE rdvts_oltp.road_m " +
                "SET geom=st_setsrid(ST_GeomFromGeoJSON('" + geom + "'),4326)  " +
                "WHERE id=:roadId and is_active=true ;";
        sqlParam.addValue("roadId", roadId);
        sqlParam.addValue("userId", userId);
        return namedJdbc.update(qry, sqlParam);
    }

    public List<UnassignedRoadDDDto> unassignedRoadDD(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select id, road_name from rdvts_oltp.geo_construction_m  " +
                "where id not in (select road_id from rdvts_oltp.geo_master where is_active=true) and  is_active=true order by road_name ";
        sqlParam.addValue("userId", userId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UnassignedRoadDDDto.class));
    }

    public Integer saveGeom(Integer roadId, List<RoadLocationEntity> roadLocation, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String update = "";
        String qry = "";
        String geom_type = "";
        String str = "";


        str += "LINESTRING(";
        if (roadLocation.size()> 1) {

            for (int i = 0; i < roadLocation.size(); i++) {
                str += roadLocation.get(i).getLongitude() + " " + roadLocation.get(i).getLatitude() + ",";

            }
            str = str.substring(0, str.length() - 1);
            str += ")";
//            update = "UPDATE rdvts_oltp.geo_construction_m set geom = ST_GeomFromText('" + str + "',4326)  where id=" + roadId + "";
            update = "UPDATE rdvts_oltp.road_m set geom = ST_GeomFromText('" + str + "',4326)  where id=" + roadId + "";
            sqlParam.addValue("userId", userId);
            namedJdbc.update(update, sqlParam);
        }
        return 1;
    }
    public Integer saveLength(Integer roadId, List<RoadLocationEntity> roadLocation, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = "UPDATE rdvts_oltp.geo_construction_m set gis_length=ST_Length(ST_Transform(geom,26986))/1000 where id=:roadId";
        String qry = "UPDATE rdvts_oltp.road_m set gis_length=ST_Length(ST_Transform(geom,26986))/1000 where id=:roadId";
        sqlParam.addValue("roadId", roadId);
        sqlParam.addValue("userId", userId);
        namedJdbc.update(qry, sqlParam);
        return 1;
    }


        public List<GeoMasterDto> getWorkByCircleId(Integer circleObj) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                "\tFROM rdvts_oltp.geo_master where is_active=true  ";
        /*   "AND id>1 ORDER BY id";*/ // add division Id here
        if (circleObj > 0) {
            qry += " AND circle_id=:circleObj";
        }
        sqlParam.addValue("circleObj", circleObj);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
    }
    public List<AlertCountDto> getAlert(Integer roadId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
            String qry = " select distinct road.id as roadId, ad.alert_type_id as alertTypeId, atm.alert_type as alertType, ad.latitude, ad.longitude, ad.altitude, ad.accuracy, ad.speed, ad.is_resolve,\n" +
                    "ad.resolved_by, count(ad.id) over (partition by ad.alert_type_id,road.id)  " +
                    "from rdvts_oltp.geo_construction_m as road " +
                    "left join rdvts_oltp.geo_master as gm on gm.road_id=road.id " +
                    "left join rdvts_oltp.activity_work_mapping as awm on awm.work_id=gm.work_id " +
                    "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id=awm.activity_id " +
                    "left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.vehicle_id = vam.vehicle_id " +
                    "left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id " +
                    "left join rdvts_oltp.alert_data as ad on ad.imei=dm.imei_no_1 " +
                    "left join rdvts_oltp.alert_type_m as atm on atm.id=ad.alert_type_id where gm.is_active=true and road.id=:roadId order by road.id ";
        sqlParam.addValue("roadId", roadId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
    }
}


//
//        update+="UPDATE public.asset set geom = ST_GeomFromText('".$str."',4326),length = ST_Length(st_transform(ST_GeomFromText('".$str."',4326),32645))/1000 where id=:roadId"
//
//        update += " UPDATE rdvts_oltp.geo_construction_m " +
//                "  SET geom=st_setsrid(ST_GeomFromText('" + latitude + "', '" + latitude +"'),4326)  " +
//                "  WHERE id=:roadId and is_active=true";
//
//        sqlParam.addValue("roadId", roadId);
//        sqlParam.addValue("latitude", latitude);
//        sqlParam.addValue("longitude", longitude);
//        sqlParam.addValue("userId", userId);
//        return namedJdbc.update(update, sqlParam);
//    }
//}
//if($geom_type == 'Line')
//        {
//        $sql = "select * from public.asset_boundary where asset_id='".$asset_id."'";
//        $result = $this->db->query($sql)->result();
//        if(count($result)>1)
//        {
//        $str = "LINESTRING(";
//        for($i=0;$i<count($result);$i++)
//        {
//        $str = $str.$result[$i]->longitude." ".$result[$i]->latitude.",";
//        }
//        $str = substr($str,0,-1);
//        $str = $str.")";
//        $update = "UPDATE public.asset set geom = ST_GeomFromText('".$str."',4326),length = ST_Length(st_transform(ST_GeomFromText('".$str."',4326),32645))/1000 where id='".$asset_id."'";
//        $this->db->query($update);
//        }
//        }
