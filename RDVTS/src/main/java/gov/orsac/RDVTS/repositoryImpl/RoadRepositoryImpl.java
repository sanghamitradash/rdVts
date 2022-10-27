package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.RoadEntity;
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
        String qry = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, ST_AsGeoJSON(road.geom) as geom, road.road_width, road.g_road_id as groadId, " +
                " road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, road.completed_road_length, road.sanction_date, road.road_code, " +
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

    public List<RoadMasterDto> getRoadWorkById(Integer workId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadMasterDto> road;
        String qry = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, road.road_width,road.geom ,road.g_road_id as groadId,wm.id as work_id, road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, road.completed_road_length, road.sanction_date, road.road_code, " +
                "road.road_status, road.approval_status, road.approved_by  " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS gm ON gm.road_id=road.id " +
                "left join rdvts_oltp.work_m as wm on wm.id=gm.work_id " +
                "WHERE road.is_active=true  ";
        if (workId > 0) {
            qry += " and wm.id= :id";
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

        int pageNo = roadFilterDto.getOffSet()/roadFilterDto.getLimit();
        PageRequest pageable = PageRequest.of(pageNo, roadFilterDto.getLimit(), Sort.Direction.fromString("asc"), "id");
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");

        int resultCount = 0;

        String queryString = "SELECT DISTINCT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, road.road_width, road.g_road_id as groadId, " +
                "road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, geom.g_work_id as workIds, " +
                "geom.g_contractor_id as contractIds, road.completed_road_length, road.sanction_date, road.road_code, " +
                "road.road_status, road.approval_status, road.approved_by, " +
                "am.id as activityId " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.road_id=road.id and geom.is_active = true " +
                "LEFT JOIN rdvts_oltp.work_m as wm on wm.id=geom.work_id and wm.is_active = true " +
                "LEFT JOIN rdvts_oltp.activity_m as am on am.work_id=wm.id and am.is_active=true " +
                "WHERE road.is_active = true  ";

        if (roadFilterDto.getId() != null && roadFilterDto.getId() > 0) {
            queryString += " AND road.id=:id ";
            sqlParam.addValue("id", roadFilterDto.getId());
        }

        if (roadFilterDto.getRoadName() != null) {
            queryString += " AND road.road_name LIKE(:roadName) ";
            sqlParam.addValue("roadName", roadFilterDto.getRoadName());
        }

        if (roadFilterDto.getRoadLength() != null && roadFilterDto.getRoadLength() > 0) {
            queryString += " AND road.road_length=:roadLength ";
            sqlParam.addValue("roadLength", roadFilterDto.getRoadLength());
        }

        if (roadFilterDto.getRoadLocation() != null && roadFilterDto.getRoadLocation() > 0) {
            queryString += " AND road.road_location=:roadLocation ";
            sqlParam.addValue("roadLocation", roadFilterDto.getRoadLocation());
        }

        if (roadFilterDto.getWorkIds() != null && !roadFilterDto.getWorkIds().isEmpty()) {
            queryString += " AND geom.g_work_id IN (:workIds)";
            sqlParam.addValue("workIds", roadFilterDto.getWorkIds());
        }

        if (roadFilterDto.getContractIds() != null && !roadFilterDto.getContractIds().isEmpty()) {
            queryString += " AND geom.g_contractor_id IN (:contractIds)";
            sqlParam.addValue("contractIds", roadFilterDto.getContractIds());
        }

        if (roadFilterDto.getActivityIds() != null && !roadFilterDto.getActivityIds().isEmpty()) {
            queryString += " AND am.id IN (:activityids)";
            sqlParam.addValue("activityids", roadFilterDto.getActivityIds());
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

        String qry = "SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                "\tFROM rdvts_oltp.geo_master where is_active=true and dist_id =:districtId; ";
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
                "\tFROM rdvts_oltp.geo_master where is_active=true and dist_id =:divisionId; ";
        /*   "AND id>1 ORDER BY id";*/ // add division Id here
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

    public List<RoadWorkMappingDto> getWorkDetailsByRoadId(Integer roadId) {
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
//        sqlParam.addValue("roadId", userId);
        try {
            road = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadWorkMappingDto.class));

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return road;
//        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadWorkMappingDto.class));
    }
    public List<Integer> getWorkIdsByRoadId(List<Integer> id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select wm.id from rdvts_oltp.work_m as wm   " +
                "left join rdvts_oltp.geo_master as gm on gm.work_id=wm.id   " +
                "left join rdvts_oltp.geo_construction_m as road on road.id=gm.road_id  " +
                "where wm.is_active=true AND road.id =:id ";
        sqlParam.addValue("id", id);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }
    public List<Integer> getDistIdsByRoadId(List<Integer> id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "   select gm.dist_id from rdvts_oltp.geo_master as gm   " +
                "  left join rdvts_oltp.geo_construction_m as road on road.id = gm.road_id  " +
                "  where road.id=:id and gm.is_active=true ";
        sqlParam.addValue("id", id);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }
    public List<RoadMasterDto> getRoadByRoadIds(List<Integer> id, List<Integer> workIdList,List<Integer> distIdList, List<Integer> blockIds, List<Integer> vehicleIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadMasterDto> road;
        String qry = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, ST_AsGeoJSON(road.geom) as geom, road.road_width, road.g_road_id as groadId, " +
                " road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, road.completed_road_length, road.sanction_date as sanctionDate, road.road_code, " +
                "road.road_status, road.approval_status, road.approved_by, geom.work_id as workIds, geom.g_dist_id as distIds, geom.g_block_id as blockIds " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.road_id=road.id " +
                "LEFT JOIN rdvts_oltp.work_m as work on work.id = geom.work_id  " +
                "WHERE road.is_active=true ";

        if (id.get(0) > 0) {
            qry += " AND road.id IN (:id)";
            sqlParam.addValue("id", id);
        }
        if (!workIdList.isEmpty()  && workIdList != null) {
            qry += " AND geom.work_id IN (:workIdList)";
            sqlParam.addValue("workIdList", workIdList);
        }
        if (distIdList != null && distIdList.size() > 0) {
            qry += " AND geom.g_dist_id IN (:distIds)";
            sqlParam.addValue("distIds", distIdList);
        }
        if (blockIds.get(0) > 0) {
            qry += " AND geom.g_block_id IN (:blockIds)";
            sqlParam.addValue("blockIds", blockIds);
        }
        if (vehicleIds.get(0) > 0) {
            qry += " AND geom. IN (:vehicleIds)";
            sqlParam.addValue("vehicleIds", vehicleIds);
        }

        try {
            road = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return road;
    }

    public RoadStatusDropDownDto getRoadStatusDD() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT rs.id, rs.name, rs.is_active , rs.created_by, rs.created_on, rs.updated_by, rs.updated_on " +
                " FROM rdvts_oltp.road_status_m as rs ";
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(RoadStatusDropDownDto.class));
    }

    public int updateGeom(Integer roadId,String geom) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.geo_construction_m " +
                "SET geom=st_setsrid(ST_GeomFromGeoJSON('"+geom+"'),4326)  " +
                "WHERE id=:roadId and is_active=true ;";
        sqlParam.addValue("roadId", roadId);
        return namedJdbc.update(qry, sqlParam);
    }
}
