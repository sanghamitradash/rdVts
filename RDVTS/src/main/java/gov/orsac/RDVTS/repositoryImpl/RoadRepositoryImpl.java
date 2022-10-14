package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.RoadMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class RoadRepositoryImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;
    public RoadMasterDto getDeviceById(Integer roadId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        sqlParam.addValue("roadId", roadId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
    }
}
