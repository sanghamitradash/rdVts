package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import gov.orsac.RDVTS.dto.RoadMasterDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.*;
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
                "road.geo_master_id as geoMasterId, road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.id=road.geo_master_id " +
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
        String qry = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, road.road_width,road.geom ,road.g_road_id as groadId,road.geo_master_id as geoMasterId,wm.id as work_id, road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS gm ON gm.id=road.geo_master_id " +
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
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        pageable = PageRequest.of(roadFilterDto.getDraw() - 1, roadFilterDto.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        int resultCount = 0;
        String queryString = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, road.road_width, road.g_road_id as groadId, " +
                "road.geo_master_id as geoMasterId, road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on, geom.g_work_id as workIds, geom.g_contractor_id as contractIds " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.id=road.geo_master_id " +
                "WHERE road.is_active = true ";

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

        String qry = " SELECT id, g_work_id, g_dist_id, g_block_id, g_piu_id, g_contractor_id, work_id, piu_id, dist_id, block_id, road_id, is_active, created_by, created_on, updated_by, updated_on\n" +
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
        if (blockId>0){
            qry+=" and block_id =:blockId";
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
                "road.geo_master_id as geoMasterId, road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.id=road.geo_master_id " +
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
                "road.geo_master_id as geoMasterId, workm.id as workId, workm.g_work_id as gWorkId, workm.g_work_name as gWorkName, workm.is_active as isActive, workm.created_by as createdBy, workm.created_on as createdOn, " +
                "workm.updated_by as updatedBy, workm.updated_on as updatedOn, piu.name as piuName " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS gm ON gm.id = road.geo_master_id  " +
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
}
