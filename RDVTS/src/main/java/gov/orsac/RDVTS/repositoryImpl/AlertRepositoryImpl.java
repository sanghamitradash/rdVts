package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.AlertDto;
import gov.orsac.RDVTS.dto.LocationDto;
import gov.orsac.RDVTS.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AlertRepositoryImpl implements AlertRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;
    public AlertDto checkAlertExists(Long imei, Integer noDataAlertId){

        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
AlertDto alertDto=new AlertDto();
        String qry = "SELECT * FROM rdvts_oltp.alert_data " +
                "   WHERE imei=:imei AND alert_type_id=:noDataAlertId AND date(gps_dtm)=date(now()) AND is_resolve = false  " ;


        sqlParam.addValue("imei", imei);
        sqlParam.addValue("noDataAlertId",noDataAlertId);
        try {
           if(namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class)) != null)
            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class));
           else
               return null;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }


        //return locationDto;



        //return true;
    }
}
