package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public class AlertRepositoryImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public List<Integer> getTotalAlertToday(Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select distinct count(id) from rdvts_oltp.alert_data where imei in (select imei_no_1 from rdvts_oltp.device_m where id in " +
                " (select device_id from rdvts_oltp.vehicle_device_mapping where vehicle_id in " +
                " (select vehicle_id from rdvts_oltp.vehicle_activity_mapping where activity_id in " +
                " (select activity_id from rdvts_oltp.activity_work_mapping where work_id = :workId)))) " +
                " and date(gps_dtm)=date(now()) and is_active=true group by alert_type_id " ;
        sqlParam.addValue("workId", id);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
    public List<Integer> getTotalAlertWork(Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "  select distinct count(id) from rdvts_oltp.alert_data where imei in (select imei_no_1 from rdvts_oltp.device_m where id in  " +
                "  (select device_id from rdvts_oltp.vehicle_device_mapping where vehicle_id in  " +
                "    (select vehicle_id from rdvts_oltp.vehicle_activity_mapping where activity_id in  " +
                "       (select activity_id from rdvts_oltp.activity_work_mapping where work_id = :workId)))) and is_active=true group by alert_type_id " ;
        sqlParam.addValue("workId", id);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }

//alert count today qry//
//    select count(id) from rdvts_oltp.alert_data where imei in (select imei_no_1 from rdvts_oltp.device_m where id in
//        (select device_id from rdvts_oltp.vehicle_device_mapping where vehicle_id in
//        (select vehicle_id from rdvts_oltp.vehicle_activity_mapping where activity_id in
//        (select activity_id from rdvts_oltp.activity_work_mapping where work_id in (select id from rdvts_oltp.work_m))))) and date(created_on)=date(now())



    public List<AlertDto> checkAlertExists(Long imei, Integer noDataAlertId){

        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        AlertDto alertDto=new AlertDto();
        String qry = "SELECT * FROM rdvts_oltp.alert_data " +
                "   WHERE imei=:imei AND alert_type_id=:noDataAlertId AND date(gps_dtm)=date(now()) AND is_resolve = false  " ;


        sqlParam.addValue("imei", imei);
        sqlParam.addValue("noDataAlertId",noDataAlertId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class));


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

    public List<AlertDto> getAllDeviceByVehicle() {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = " SELECT distinct vm.id, vm.speed_limit as speedLimit, vdm.vehicle_id as vehicleId,vdm.device_id as deviceId,dm.imei_no_1 as imei" +
                " FROM rdvts_oltp.vehicle_m vm " +
                "  join rdvts_oltp.vehicle_device_mapping vdm on vdm.vehicle_id=vm.id " +
                "  join rdvts_oltp.device_m dm on vdm.device_id= dm.id" ;

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class));
    }

    public List<VtuLocationDto> getAlertLocationOverSpeed(Long imei, double speedLimit, Integer recordLimit) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on " +
                " FROM rdvts_oltp.vtu_location where is_active=true ";
        qry += " and imei =:imei1 and gps_fix::numeric =1 ";

        Date currentDateMinus = new Date(System.currentTimeMillis() - 900 * 1000); //5 minute in millisecond 60*5
//        DateTime dateTime = new DateTime();
//        dateTime = dateTime.minusMinutes(5);
//        Date modifiedDate = dateTime.toDate();
        Date currentDate=new Date();


        qry += "   AND date(date_time)=date(now()) AND  date_time BETWEEN :currentDateMinus AND :currentDate and REPLACE(speed, ' ', '')::numeric > :speedLimit order by date_time ASC  limit :recordLimit ";
        sqlParam.addValue("imei1", imei);

        sqlParam.addValue("recordLimit", recordLimit);
        sqlParam.addValue("currentDateMinus", currentDateMinus);
        sqlParam.addValue("speedLimit", speedLimit);
        sqlParam.addValue("currentDate", currentDate);

        //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
    }
}
