package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class GeoMasterRepositoryImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public List<GeoMasterDto> getAllGeoMasterByAllId(GeoMasterDto geoMasterDto){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select * from rdvts_oltp.geo_master where is_active = true";
        if (geoMasterDto.getId() > 0){
            qry+="and id = :id";
            sqlParam.addValue("id", geoMasterDto.getId());
        }
        if (geoMasterDto.getGeoWorkId() > 0){
            qry+="and g_work_id = :geoWorkId";
            sqlParam.addValue("geoWorkId", geoMasterDto.getGeoWorkId());
        }
        if (geoMasterDto.getGeoDistId() > 0){
            qry+="and g_dist_id = :geoDistId";
            sqlParam.addValue("geoDistId", geoMasterDto.getGeoDistId());
        }

        List<GeoMasterDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
        return list;

    }
}
