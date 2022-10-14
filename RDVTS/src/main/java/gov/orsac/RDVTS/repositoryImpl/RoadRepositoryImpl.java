package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.RoadMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class RoadRepositoryImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public RoadMasterDto getRoadById(Integer roadId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, road.road_allignment, road.road_width, road.g_road_id as groadId, " +
                "road.geo_master_id as geoMasterId, road.is_active, road.created_by, road.created_on, road.updated_by, road.updated_on " +
                "FROM rdvts_oltp.geo_construction_m AS road " +
                "LEFT JOIN rdvts_oltp.geo_master AS geom ON geom.id=road.geo_master_id " +
                "WHERE road.id=:roadId";
        sqlParam.addValue("roadId", roadId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
    }
}
