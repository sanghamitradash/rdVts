package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlertRepositoryImpl  {
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

    public Boolean updateResolve(Long imei1, Integer noDataAlertId){

        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = "Update rdvts_oltp.alert_data al  SET is_resolve = true WHERE al.imei =:imei and al.alert_type_id =:noDataAlertId " ;


        sqlParam.addValue("imei", imei1);
        sqlParam.addValue("noDataAlertId",noDataAlertId);
        int update = namedJdbc.update(qry, sqlParam);
        boolean result = false;
        if (update > 0) {
            result = true;
        }
        return result;

    }

    public List<Long> getImeiForNoMovement() {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = " SELECT l.imei from  (select distinct imei_no_1 from rdvts_oltp.device_m  ) tl " +
                " JOIN (select * from rdvts_oltp.vtu_location  " +
                "  Where id IN (Select max(id) from rdvts_oltp.vtu_location where gps_fix::numeric=1 group by imei) and gps_fix::numeric=1 " +
                " AND date(date_time)=date(now()) " +
                "  order by id desc) as l ON l.imei = tl.imei_no_1 " ;


        return namedJdbc.queryForList(qry, sqlParam,Long.class);
    }

    public List<VtuLocationDto> getLocationRecordByFrequency(Long imei1,Integer recordLimit) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "SELECT * FROM rdvts_oltp.vtu_location where imei =:imei1  and gps_fix::numeric=1" +
                "                                ORDER BY date_time ASC limit :recordLimit" ;
        sqlParam.addValue("imei1", imei1);
        sqlParam.addValue("recordLimit", recordLimit);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
    }

    public List<BufferDto> getBuffer(Long item) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "" ;
       // sqlParam.addValue("imei1", 864180069579428);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(BufferDto.class));
    }

    public Boolean checkIntersected(String longitude, String latitude, String vtuItemLongitude, String vtuItemLatitude) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "SELECT ST_Intersects(st_setsrid(st_geomfromtext(st_astext(ST_BUFFER( ST_SetSRID(ST_MakePoint("+longitude+", "+latitude+"),4326)::geography,100)::geometry)),4326), " +
                "                                'SRID=4326;POINT("+vtuItemLongitude+" "+vtuItemLatitude+" )'::geometry)" ;

//        sqlParam.addValue("roadBuffer", roadBuffer);
//        sqlParam.addValue("latitude", latitude);
//        sqlParam.addValue("longitude", longitude);

        return namedJdbc.queryForObject(qry, sqlParam,(Boolean.class));
    }

    public Boolean bufferQuery(String bufferPointLongitude, String bufferPointLatitude, String longitude, String latitude) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "SELECT ST_Intersects(st_buffer(st_setsrid(st_makepoint("+bufferPointLongitude+","+bufferPointLatitude+"),4326)::geography,100)::geometry, " +
                "   'SRID=4326;POINT("+longitude+" "+latitude+" )'::geometry) as inFlag" ;

//        sqlParam.addValue("roadBuffer", roadBuffer);
//        sqlParam.addValue("latitude", latitude);
//        sqlParam.addValue("longitude", longitude);

        return namedJdbc.queryForObject(qry, sqlParam,(Boolean.class));
    }

    public Boolean checkGeoFenceIntersected(String geom, String longitude, String latitude) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String geomText ="'"+geom+"'";
        String qry = "  select ST_Intersects(st_setsrid(st_geomfromtext(  st_astext(ST_BUFFER("+geomText+"::geography,50)::geometry)  " +
                " ),4326),  'SRID=4326;POINT("+longitude+" "+latitude+" )'::geometry) as inFlag" ;

        return namedJdbc.queryForObject(qry, sqlParam,(Boolean.class));
    }
}
