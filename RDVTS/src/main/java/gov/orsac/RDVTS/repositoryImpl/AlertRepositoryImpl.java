package gov.orsac.RDVTS.repositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlertRepositoryImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public Integer getTotalAlertToday() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select distinct count(id) from rdvts_oltp.alert_data where imei in (select imei_no_1 from rdvts_oltp.device_m where id in " +
                " (select device_id from rdvts_oltp.vehicle_device_mapping where vehicle_id in " +
                " (select vehicle_id from rdvts_oltp.vehicle_activity_mapping where activity_id in " +
                " (select activity_id from rdvts_oltp.activity_work_mapping where work_id in (:workId))))) " +
                " and date(gps_dtm)=date(now()) and is_active=true " ;
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }
    public Integer getTotalAlertWork() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "  select id from rdvts_oltp.alert_data where imei in (select imei_no_1 from rdvts_oltp.device_m where id in " +
                " (select device_id from rdvts_oltp.vehicle_device_mapping where vehicle_id in " +
                " (select vehicle_id from rdvts_oltp.vehicle_work_mapping where vehicle_id in (:vehicleId) and work_id in (:workId)))) " ;
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }
}

//alert count today qry//
//    select count(id) from rdvts_oltp.alert_data where imei in (select imei_no_1 from rdvts_oltp.device_m where id in
//        (select device_id from rdvts_oltp.vehicle_device_mapping where vehicle_id in
//        (select vehicle_id from rdvts_oltp.vehicle_activity_mapping where activity_id in
//        (select activity_id from rdvts_oltp.activity_work_mapping where work_id in (select id from rdvts_oltp.work_m))))) and date(created_on)=date(now())
