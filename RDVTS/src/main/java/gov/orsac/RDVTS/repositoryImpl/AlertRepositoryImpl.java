package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.AlertTypeEntity;
import gov.orsac.RDVTS.entities.WorkCronEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Slf4j
@Repository
public class AlertRepositoryImpl {

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

    public List<AlertCountDto> getTotalAlertToday(/*AlertFilterDto filterDto,*/ Integer id, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        currentDateTime = currentDateTime + " 00:00:00";
//        PageRequest pageable = null;
//
//        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
//        pageable = PageRequest.of(filterDto.getDraw()-1,filterDto.getLimit(), Sort.Direction.fromString("desc"), "id");
//
//        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
//        int resultCount=0;
        String qry = " select distinct wm.id as workId, ad.id as alertId, ad.alert_type_id as alertTypeId,atm.alert_type as alertType, ad.latitude, ad.longitude, ad.accuracy, ad.speed, ad.altitude, \n" +
                "ad.gps_dtm as gpsDtm, ad.is_resolve as isResolve, ad.resolved_by as resolvedBy, count(ad.id) over (partition by ad.alert_type_id,wm.id) " +
                "from rdvts_oltp.work_m as wm " +
                "left join rdvts_oltp.activity_work_mapping as awm on awm.work_id=wm.id " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on awm.activity_id=vam.activity_id and vam.is_active=true " +
                "left join rdvts_oltp.vehicle_device_mapping as vdm on vam.vehicle_id=vdm.vehicle_id and vdm.is_active=true " +
                "left join rdvts_oltp.device_m as dm on vdm.device_id=dm.id and dm.is_active=true " +
                "left join rdvts_oltp.alert_data as ad on dm.imei_no_1=ad.imei and dm.is_active=true " +
                "left join rdvts_oltp.alert_type_m as atm on atm.id=ad.alert_type_id " +
                "where awm.is_active=true and wm.id=:workId and ad.gps_dtm >=:currentDateTime::timestamp  ";
        sqlParam.addValue("workId", id);
        sqlParam.addValue("currentDateTime",currentDateTime);

//        if (filterDto.getAlertTypeId() != null && filterDto.getAlertTypeId() > 0) {
//            qry += " AND ad.alert_type_id=:alertTypeId ";
//            sqlParam.addValue("alertTypeId", filterDto.getAlertTypeId());
//        }
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        if (filterDto.getStartDate() != null && !filterDto.getStartDate().isEmpty()) {
//            qry += " AND date(ad.gps_dtm) >= :startDate";
//            Date startDate = null;
//            try {
//                startDate = format.parse(filterDto.getStartDate());
//            } catch (Exception exception) {
//                log.info("From Date Parsing exception : {}", exception.getMessage());
//            }
//            sqlParam.addValue("startDate", startDate, Types.DATE);
//        }
//        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
//            qry += " AND date(ad.gps_dtm) <= :endDate";
//            Date endDate = null;
//            try {
//                endDate = format.parse(filterDto.getEndDate());
//            } catch (Exception exception) {
//                log.info("To Date Parsing exception : {}", exception.getMessage());
//            }
//            sqlParam.addValue("endDate", endDate, Types.DATE);
//        }

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
//        resultCount = count(qry, sqlParam);
//        if (filterDto.getLimit() > 0){
//            qry += " Order by wm.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
//        }
//
//        List<AlertCountDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
//        return new PageImpl<>(list, pageable, resultCount);
    }
    public List<AlertCountDto> getTotalAlertWork(/*AlertFilterDto filterDto,*/ Integer id, Integer userId) {
//        PageRequest pageable = null;
//
//        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
//        pageable = PageRequest.of(filterDto.getDraw()-1,filterDto.getLimit(), Sort.Direction.fromString("desc"), "id");
//
//        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
//        int resultCount=0;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "  select distinct wm.id as workId, ad.id as alertId, ad.alert_type_id as alertTypeId,ad.speed as speed,atm.alert_type as alertType, ad.latitude, ad.longitude, ad.accuracy, ad.speed, ad.altitude,  " +
                " ad.gps_dtm as gpsDtm,count(ad.id) over (partition by ad.alert_type_id,wm.id) " +
                "from rdvts_oltp.work_m as wm " +
                "left join rdvts_oltp.activity_work_mapping as awm on awm.work_id=wm.id " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on awm.activity_id=vam.activity_id and vam.is_active=true " +
                "left join rdvts_oltp.vehicle_device_mapping as vdm on vam.vehicle_id=vdm.vehicle_id and vdm.is_active=true " +
                "left join rdvts_oltp.device_m as dm on vdm.device_id=dm.id and dm.is_active=true " +
                "left join rdvts_oltp.alert_data as ad on dm.imei_no_1=ad.imei and dm.is_active=true " +
                "left join rdvts_oltp.alert_type_m as atm on atm.id=ad.alert_type_id " +
                "where awm.is_active=true and wm.id=:workId  "  ;
        sqlParam.addValue("workId", id);
//        if (filterDto.getAlertTypeId() != null && filterDto.getAlertTypeId() > 0) {
//            qry += " AND ad.alert_type_id=:alertTypeId ";
//            sqlParam.addValue("alertTypeId", filterDto.getAlertTypeId());
//        }
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        if (filterDto.getStartDate() != null && !filterDto.getStartDate().isEmpty()) {
//            qry += " AND date(ad.gps_dtm) >= :startDate";
//            Date startDate = null;
//            try {
//                startDate = format.parse(filterDto.getStartDate());
//            } catch (Exception exception) {
//                log.info("From Date Parsing exception : {}", exception.getMessage());
//            }
//            sqlParam.addValue("startDate", startDate, Types.DATE);
//        }
//        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
//            qry += " AND date(ad.gps_dtm) <= :endDate";
//            Date endDate = null;
//            try {
//                endDate = format.parse(filterDto.getEndDate());
//            } catch (Exception exception) {
//                log.info("To Date Parsing exception : {}", exception.getMessage());
//            }
//            sqlParam.addValue("endDate", endDate, Types.DATE);
//        }
        return  namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));

//        resultCount = count(qry, sqlParam);
//        if (filterDto.getLimit() > 0){
//            qry += " order by wm.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
//        }
//
//        List<AlertCountDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
//        return new PageImpl<>(list, pageable, resultCount);
    }

//alert count today qry//
//    select count(id) from rdvts_oltp.alert_data where imei in (select imei_no_1 from rdvts_oltp.device_m where id in
//        (select device_id from rdvts_oltp.vehicle_device_mapping where vehicle_id in
//        (select vehicle_id from rdvts_oltp.vehicle_activity_mapping where activity_id in
//        (select activity_id from rdvts_oltp.activity_work_mapping where work_id in (select id from rdvts_oltp.work_m))))) and date(created_on)=date(now())



    public Boolean checkAlertExists(Long imei, Integer noDataAlertId){

        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        currentDateTime = currentDateTime + " 00:00:00";
        AlertDto alertDto=new AlertDto();
        String qry = " select case when count(*) >0 then true else false  end as bool FROM rdvts_oltp.alert_data  " +
                "WHERE imei=:imei AND alert_type_id=:noDataAlertId AND gps_dtm >=:currentDateTime::timestamp AND is_resolve = false  " ;

        sqlParam.addValue("currentDateTime",currentDateTime);
        sqlParam.addValue("imei", imei);
        sqlParam.addValue("noDataAlertId",noDataAlertId);
        return namedJdbc.queryForObject(qry, sqlParam, (Boolean.class));


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
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        currentDateTime = currentDateTime + " 00:00:00";


        String qry = " SELECT l.imei from  (select distinct imei_no_1 from rdvts_oltp.device_m  ) tl " +
                " JOIN (select * from rdvts_oltp.vtu_location  " +
                "  Where id IN (Select max(id) from rdvts_oltp.vtu_location where gps_fix::numeric=1 group by imei) and gps_fix::numeric=1 " +
                " AND date_time >=:currentDateTime::timestamp " +
                "  order by id desc) as l ON l.imei = tl.imei_no_1 " ;
        sqlParam.addValue("currentDateTime",currentDateTime);


        return namedJdbc.queryForList(qry, sqlParam,Long.class);
    }

    public List<VtuLocationDto> getLocationRecordByFrequency(Long imei1,Integer recordLimit, Date startTime, Date endTime) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "SELECT * FROM rdvts_oltp.vtu_location where imei =:imei1  and gps_fix::numeric=1" +
                "     and date_time between :startTime and :endTime ORDER BY date_time ASC limit :recordLimit" ;

        sqlParam.addValue("imei1", imei1);
        sqlParam.addValue("recordLimit", recordLimit);
        sqlParam.addValue("startTime", startTime);
        sqlParam.addValue("endTime", endTime);

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


        //return null;
    }

    public List<AlertDto> getAllDeviceByVehicle() {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = " SELECT distinct vm.id, vm.speed_limit as speedLimit, vdm.vehicle_id as vehicleId,vdm.device_id as deviceId,dm.imei_no_1 as imei" +
                " FROM rdvts_oltp.vehicle_m vm " +
                "  join rdvts_oltp.vehicle_device_mapping vdm on vdm.vehicle_id=vm.id " +
                "  join rdvts_oltp.device_m dm on vdm.device_id= dm.id" ;

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class));
    }

    public List<VtuLocationDto> getAlertLocationOverSpeed(Long imei, double speedLimit, Date startTime, Date endTime) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        currentDateTime = currentDateTime + " 00:00:00";

        String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on " +
                " FROM rdvts_oltp.vtu_location where is_active=true ";
        qry += " and imei =:imei1 and gps_fix::numeric =1 ";

        Date currentDateMinus = new Date(System.currentTimeMillis() - 900 * 1000); //5 minute in millisecond 60*5
//        DateTime dateTime = new DateTime();
//        dateTime = dateTime.minusMinutes(5);
//        Date modifiedDate = dateTime.toDate();
        Date currentDate=new Date();


        qry += "   AND date_time BETWEEN :startTime AND :startTime  order by date_time ASC ";
        sqlParam.addValue("imei1", imei);
        sqlParam.addValue("currentDateTime",currentDateTime);

        sqlParam.addValue("currentDateMinus", currentDateMinus);
        sqlParam.addValue("speedLimit", speedLimit);
        sqlParam.addValue("currentDate", currentDate);
        sqlParam.addValue("startTime", startTime);
        sqlParam.addValue("endTime", endTime);


        List<VtuLocationDto> vtuLocationDtoList= namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
      return vtuLocationDtoList;


    }

    public AlertTypeEntity getAlertTypeDetails(int i) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "SELECT * FROM rdvts_oltp.alert_type_m where is_active=true and id=:id" ;

        sqlParam.addValue("id", i);

        return namedJdbc.queryForObject(qry, sqlParam,new BeanPropertyRowMapper<>(AlertTypeEntity.class));
    }


    public List<AlertTypeEntity> getAlertTypeDetails() {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "SELECT * FROM rdvts_oltp.alert_type_m where is_active=true " ;
        return namedJdbc.query(qry, sqlParam,new BeanPropertyRowMapper<>(AlertTypeEntity.class));
    }

    public Page<AlertCountDto> getAlertToday(AlertFilterDto filterDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        currentDateTime = currentDateTime + " 00:00:00";
        PageRequest pageable = null;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        pageable = PageRequest.of(filterDto.getDraw()-1,filterDto.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;
        String qry = " select distinct wm.id as workId, ad.id as alertId, ad.alert_type_id as alertTypeId,atm.alert_type as alertType, ad.latitude, ad.longitude, ad.accuracy, ad.speed, ad.altitude, \n" +
                "ad.gps_dtm as gpsDtm, ad.is_resolve as isResolve, ad.resolved_by as resolvedBy, count(ad.id) over (partition by ad.alert_type_id,wm.id) " +
                "from rdvts_oltp.work_m as wm " +
                "left join rdvts_oltp.activity_work_mapping as awm on awm.work_id=wm.id " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on awm.activity_id=vam.activity_id and vam.is_active=true " +
                "left join rdvts_oltp.vehicle_device_mapping as vdm on vam.vehicle_id=vdm.vehicle_id and vdm.is_active=true " +
                "left join rdvts_oltp.device_m as dm on vdm.device_id=dm.id and dm.is_active=true " +
                "left join rdvts_oltp.alert_data as ad on dm.imei_no_1=ad.imei and dm.is_active=true " +
                "left join rdvts_oltp.alert_type_m as atm on atm.id=ad.alert_type_id " +
                "where awm.is_active=true and ad.gps_dtm >=:currentDateTime::timestamp  ";
        sqlParam.addValue("currentDateTime",currentDateTime);
        if (filterDto.getWorkId() != null && filterDto.getWorkId() > 0) {
            qry += " and wm.id=:workId ";
            sqlParam.addValue("workId", filterDto.getWorkId());
        }
        if (filterDto.getAlertTypeId() != null && filterDto.getAlertTypeId() > 0) {
            qry += " AND ad.alert_type_id=:alertTypeId ";
            sqlParam.addValue("alertTypeId", filterDto.getAlertTypeId());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (filterDto.getStartDate() != null && !filterDto.getStartDate().isEmpty()) {
            qry += " AND date(ad.gps_dtm) >= :startDate";
            Date startDate = null;
            try {
                startDate = format.parse(filterDto.getStartDate());
            } catch (Exception exception) {
                log.info("From Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("startDate", startDate, Types.DATE);
        }
        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
            qry += " AND date(ad.gps_dtm) <= :endDate";
            Date endDate = null;
            try {
                endDate = format.parse(filterDto.getEndDate());
            } catch (Exception exception) {
                log.info("To Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("endDate", endDate, Types.DATE);
        }


        resultCount = count(qry, sqlParam);
        if (filterDto.getLimit() > 0){
            qry += " Order by wm.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
        }

        List<AlertCountDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
        return new PageImpl<>(list, pageable, resultCount);

    }

    public Page<AlertCountDto> getWorkAlertTotal(AlertFilterDto filterDto) {
        PageRequest pageable = null;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        pageable = PageRequest.of(filterDto.getDraw()-1,filterDto.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "  select distinct ad.id as alertId, ad.alert_type_id as alertTypeId, gm.work_id as workId, ad.speed as speed,atm.alert_type as alertType, ad.latitude,  " +
                "ad.longitude, ad.accuracy, ad.speed, ad.altitude, ad.gps_dtm as gpsDtm, " +
                "count(ad.id) over (partition by ad.alert_type_id,gm.work_id) " +
                "from rdvts_oltp.alert_data as ad  " +
                "left join rdvts_oltp.device_m as dm on dm.imei_no_1=ad.imei and dm.is_active=true   " +
                "left join rdvts_oltp.vehicle_device_mapping  as vdm on vdm.device_id=dm.id and vdm.is_active=true " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id = vdm.vehicle_id and vam.is_active=true " +
                "left join rdvts_oltp.activity_work_mapping as awm on awm.activity_id=vam.activity_id " +
                "left join rdvts_oltp.geo_master as gm on gm.work_id=awm.work_id " +
                "left join rdvts_oltp.alert_type_m as atm on atm.id=ad.alert_type_id  where ad.is_active=true  " ;
//        if(filterDto.getBlockId() != null && filterDto.getBlockId() > 0){
//            qry += " and gm.block_id=:blockId ";
//            sqlParam.addValue("blockId", filterDto.getBlockId());
//        }
        if (filterDto.getWorkId() != null && filterDto.getWorkId() > 0) {
            qry += " and gm.work_id=:workId ";
            sqlParam.addValue("workId", filterDto.getWorkId());
        }
        if (filterDto.getAlertId() != null && filterDto.getAlertId() > 0) {
            qry += " and ad.id=:alertId ";
            sqlParam.addValue("alertId", filterDto.getAlertId());
        }
        if (filterDto.getVehicleId() != null && filterDto.getVehicleId() > 0) {
            qry += " AND vam.vehicle_id = :vehicleId";
            sqlParam.addValue("vehicleId", filterDto.getVehicleId());
        }
        if (filterDto.getActivityId() != null && filterDto.getActivityId() > 0) {
            qry += " AND vam.activity_id = :activityId";
            sqlParam.addValue("activityId", filterDto.getActivityId());
        }
        if (filterDto.getDeviceId() != null && filterDto.getDeviceId() > 0) {
            qry += " AND vdm.device_id = :deviceId";
            sqlParam.addValue("deviceId", filterDto.getDeviceId());
        }
        if (filterDto.getDistId() != null && filterDto.getDistId() > 0) {
            qry += " AND gm.dist_id = :distId";
            sqlParam.addValue("distId", filterDto.getDistId());
        }
        if (filterDto.getDivisionId() != null && filterDto.getDivisionId() > 0) {
            qry += " AND gm.division_id = :divisionId";
            sqlParam.addValue("divisionId", filterDto.getDivisionId());
        }
        if (filterDto.getRoadId() != null && filterDto.getRoadId() > 0) {
            qry += " AND gm.road_id = :roadId";
            sqlParam.addValue("roadId", filterDto.getRoadId());
        }
        if (filterDto.getAlertTypeId() != null && filterDto.getAlertTypeId() > 0) {
            qry += " AND ad.alert_type_id=:alertTypeId ";
            sqlParam.addValue("alertTypeId", filterDto.getAlertTypeId());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (filterDto.getStartDate() != null && !filterDto.getStartDate().isEmpty()) {
            qry += " AND date(ad.gps_dtm) >= :startDate";
            Date startDate = null;
            try {
                startDate = format.parse(filterDto.getStartDate());
            } catch (Exception exception) {
                log.info("From Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("startDate", startDate, Types.DATE);
        }
        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
            qry += " AND date(ad.gps_dtm) <= :endDate";
            Date endDate = null;
            try {
                endDate = format.parse(filterDto.getEndDate());
            } catch (Exception exception) {
                log.info("To Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("endDate", endDate, Types.DATE);
        }

        UserInfoDto user = userRepositoryImpl.getUserByUserId(filterDto.getUserId());
        if (user.getUserLevelId() == 5) {
            qry += " AND gm.contractor_id=:contractorId ";
            sqlParam.addValue("contractorId", user.getUserLevelId());
        }

        else if (user.getUserLevelId() == 2) {
            List<Integer> distIds = userRepositoryImpl.getDistIdByUserId(filterDto.getUserId());
            List<Integer> blockIds = userRepositoryImpl.getBlockIdByDistId(distIds);
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByDistId(distIds);
            List<Integer> workIds = masterRepositoryImpl.getWorkIdsByBlockAndDivision(blockIds, divisionIds, distIds);
            if(workIds != null && workIds.size() > 0){
                if (qry != null && qry.length() > 0) {
                    qry += " AND  gm.work_id in(:workIds) ";
                    sqlParam.addValue("workIds", workIds);
                } else {
                    qry += " WHERE gm.work_id in(:workIds) ";
                    sqlParam.addValue("workIds", workIds);
                }
            }
        }

        else if(user.getUserLevelId()==3){
            List<Integer> blockIds=userRepositoryImpl.getBlockIdByUserId(filterDto.getUserId());
            List<Integer> workIds  = masterRepositoryImpl.getWorkIdsByBlockIds(blockIds);
            if(qry != null && qry.length() > 0){
                qry += " AND  gm.work_id in(:workIds) ";
                sqlParam.addValue("workIds",workIds);
            } else {
                qry += " WHERE  gm.work_id in(:workIds) ";
                sqlParam.addValue("workIds",workIds);
            }
        }

        else if(user.getUserLevelId() == 4){
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByUserId(filterDto.getUserId());
            List<Integer> workIds = masterRepositoryImpl.getWorkIdByDivisionId(divisionIds);
            if(workIds != null && workIds.size() > 0){
                if(qry != null && qry.length() > 0){
                    qry += " and gm.work_id in(:workIds) ";
                    sqlParam.addValue("workIds", workIds);
                } else {
                    qry += " and gm.work_id in(:workIds) ";
                    sqlParam.addValue("workIds", workIds);
                }
            }
        }

        resultCount = count(qry, sqlParam);
        if (filterDto.getLimit() > 0){
            qry += " order by ad.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
        }

        List<AlertCountDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
        return new PageImpl<>(list, pageable, resultCount);

    }

    public Page<AlertCountDto> getVehicleAlert(AlertFilterDto filterDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        pageable = PageRequest.of(filterDto.getDraw()-1,filterDto.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;

        String qry = "select imei,alert.id as alertId, alert_type_id,type.alert_type as alertType,latitude,longitude,altitude,accuracy,speed,gps_dtm,vdm.vehicle_id as vehicleId, \n" +
                " is_resolve,resolved_by as resolvedBy,userM.first_name as resolvedByUser, count(alert.id) over (partition by alert.alert_type_id)    \n" +
                " from  rdvts_oltp.alert_data  as alert  \n" +
                " left join rdvts_oltp.alert_type_m as type on type.id=alert.alert_type_id and type.is_active=true   \n" +
                " left join rdvts_oltp.user_m as userM on userM.id=alert.resolved_by and userM.is_active=true    \n" +
                " left join rdvts_oltp.device_m as dm on dm.imei_no_1=alert.imei and dm.is_active=true \n" +
                " left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.device_id=dm.id and vdm.is_active=true \n" +
                " left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id=vdm.vehicle_id and vam.is_active=true \n" +
                " left join rdvts_oltp.activity_work_mapping as awm on awm.activity_id=vam.activity_id  and awm.is_active=true \n" +
                "  left join rdvts_oltp.geo_master as gm on gm.work_id=awm.work_id  where alert.is_active=true   " ;
//        if(filterDto.getBlockId() != null && filterDto.getBlockId() > 0){
//            qry += " and gm.block_id=:blockIds ";
//            sqlParam.addValue("blockId", filterDto.getBlockId());
//        }
        if (filterDto.getAlertId() != null && filterDto.getAlertId() > 0) {
            qry += " and alert.id=:alertId ";
            sqlParam.addValue("alertId", filterDto.getAlertId());
        }
        if (filterDto.getVehicleId() != null && filterDto.getVehicleId() > 0) {
            qry += " and vdm.vehicle_id=:vehicleId ";
            sqlParam.addValue("vehicleId", filterDto.getVehicleId());
        }
        if (filterDto.getActivityId() != null && filterDto.getActivityId() > 0) {
            qry += " AND vam.activity_id = :activityId";
            sqlParam.addValue("activityId", filterDto.getActivityId());
        }
        if (filterDto.getDeviceId() != null && filterDto.getDeviceId() > 0) {
            qry += " AND vdm.device_id = :deviceId";
            sqlParam.addValue("deviceId", filterDto.getDeviceId());
        }
        if (filterDto.getDistId() != null && filterDto.getDistId() > 0) {
            qry += " AND gm.dist_id = :distId";
            sqlParam.addValue("distId", filterDto.getDistId());
        }
        if (filterDto.getDivisionId() != null && filterDto.getDivisionId() > 0) {
            qry += " AND gm.division_id = :divisionId";
            sqlParam.addValue("divisionId", filterDto.getDivisionId());
        }
        if (filterDto.getWorkId() != null && filterDto.getWorkId() > 0) {
            qry += " AND gm.work_id = :workId";
            sqlParam.addValue("workId", filterDto.getWorkId());
        }
        if (filterDto.getRoadId() != null && filterDto.getRoadId() > 0) {
            qry += " AND gm.road_id = :roadId";
            sqlParam.addValue("roadId", filterDto.getRoadId());
        }
        if (filterDto.getAlertTypeId() != null && filterDto.getAlertTypeId() > 0) {
            qry += " AND alert.alert_type_id=:alertTypeId ";
            sqlParam.addValue("alertTypeId", filterDto.getAlertTypeId());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (filterDto.getStartDate() != null && !filterDto.getStartDate().isEmpty()) {
            qry += " AND date(alert.gps_dtm) >= :startDate ";
            Date startDate = null;
            try {
                startDate = format.parse(filterDto.getStartDate());
            } catch (Exception exception) {
                log.info("From Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("startDate", startDate, Types.DATE);
        }
        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
            qry += " AND date(alert.gps_dtm) <= :endDate ";
            Date endDate = null;
            try {
                endDate = format.parse(filterDto.getEndDate());
            } catch (Exception exception) {
                log.info("To Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("endDate", endDate, Types.DATE);
        }
        UserInfoDto user = userRepositoryImpl.getUserByUserId(filterDto.getUserId());
        if (user.getUserLevelId() == 5) {
            qry += " AND gm.contractor_id=:contractorId ";
            sqlParam.addValue("contractorId", user.getUserLevelId());
        }

        else if (user.getUserLevelId() == 2) {
            List<Integer> distIds = userRepositoryImpl.getDistIdByUserId(filterDto.getUserId());
            List<Integer> blockIds = userRepositoryImpl.getBlockIdByDistId(distIds);
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByDistId(distIds);
            List<Integer> vehicleIds = masterRepositoryImpl.getVehicleIdsByBlockAndDivision(blockIds, divisionIds, distIds);
            if (qry != null && qry.length() > 0) {
                qry += " AND  vdm.vehicle_id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds", vehicleIds);
            } else {
                qry += " where vdm.vehicle_id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds", vehicleIds);
            }
        }

        else if(user.getUserLevelId()==3){
            List<Integer> blockIds=userRepositoryImpl.getBlockIdByUserId(filterDto.getUserId());
            List<Integer> vehicleId = masterRepositoryImpl.getVehicleIdsByBlockIds(blockIds);
            if(vehicleId != null && vehicleId.size() > 0) {
                if(qry != null && qry.length() > 0){
                    qry += " AND vdm.vehicle_id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds",vehicleId);
                } else {
                    qry += " where vdm.vehicle_id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds",vehicleId);
                }
            }
        }

        else if(user.getUserLevelId()==4){
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByUserId(filterDto.getUserId());
            List<Integer> vehicleIds = userRepositoryImpl.getVehicleIdByDivisionId(divisionIds);
            if(vehicleIds != null && vehicleIds.size() > 0){
                if(qry != null && qry.length() > 0){
                    qry += " AND vdm.vehicle_id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds", vehicleIds);
                } else {
                    qry += " where vdm.vehicle_id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds", vehicleIds);
                }
            }
        }

        resultCount = count(qry, sqlParam);
        if (filterDto.getLimit() > 0){
            qry += " Order by alert.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
        }

        List<AlertCountDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
        return new PageImpl<>(list, pageable, resultCount);

    }

    public Page<AlertCountDto> getRoadAlert(AlertFilterDto filterDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        pageable = PageRequest.of(filterDto.getDraw()-1,filterDto.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;
        String qry = "  select distinct ad.id as alertId, ad.alert_type_id as alertTypeId, atm.alert_type as alertType, ad.gps_dtm, ad.latitude, ad.longitude, ad.altitude, ad.accuracy, ad.speed, ad.is_resolve, " +
                "ad.resolved_by, count(ad.id) over (partition by ad.alert_type_id)  " +
                "from rdvts_oltp.alert_data as ad " +
                "left join rdvts_oltp.device_m as dm on dm.imei_no_1=ad.imei and dm.is_active=true  " +
                "left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.device_id = dm.id and vdm.is_active=true " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id=vdm.vehicle_id and vam.is_active=true " +
                "left join rdvts_oltp.activity_work_mapping as awm on awm.activity_id=vam.activity_id and awm.is_active=true " +
                "left join rdvts_oltp.geo_master as gm on gm.work_id=awm.work_id and gm.is_active=true " +
                "left join rdvts_oltp.alert_type_m as atm on atm.id=ad.alert_type_id where ad.is_active=true " ;
//        if(filterDto.getBlockId() != null && filterDto.getBlockId() > 0){
//            qry += " and gm.block_id=:blockIds ";
//            sqlParam.addValue("blockIds", filterDto.getBlockId());
//        }
        if (filterDto.getRoadId() != null && filterDto.getRoadId() > 0) {
            qry += " and gm.road_id=:roadId ";
            sqlParam.addValue("roadId", filterDto.getRoadId());
        }
        if (filterDto.getAlertId() != null && filterDto.getAlertId() > 0) {
            qry += " and ad.id=:alertId ";
            sqlParam.addValue("alertId", filterDto.getAlertId());
        }
        if (filterDto.getVehicleId() != null && filterDto.getVehicleId() > 0) {
            qry += " AND vdm.vehicle_id = :vehicleId";
            sqlParam.addValue("vehicleId", filterDto.getVehicleId());
        }
        if (filterDto.getActivityId() != null && filterDto.getActivityId() > 0) {
            qry += " AND vam.activity_id = :activityId";
            sqlParam.addValue("activityId", filterDto.getActivityId());
        }
        if (filterDto.getWorkId() != null && filterDto.getWorkId() > 0) {
            qry += " AND awm.work_id = :workId";
            sqlParam.addValue("workId", filterDto.getWorkId());
        }
        if (filterDto.getDeviceId() != null && filterDto.getDeviceId() > 0) {
            qry += " AND vdm.device_id = :deviceId";
            sqlParam.addValue("deviceId", filterDto.getDeviceId());
        }
        if (filterDto.getDistId() != null && filterDto.getDistId() > 0) {
            qry += " AND gm.dist_id = :distId";
            sqlParam.addValue("distId", filterDto.getDistId());
        }
        if (filterDto.getDivisionId() != null && filterDto.getDivisionId() > 0) {
            qry += " AND gm.division_id = :divisionId";
            sqlParam.addValue("divisionId", filterDto.getDivisionId());
        }
        if (filterDto.getAlertTypeId() != null && filterDto.getAlertTypeId() > 0) {
            qry += " AND ad.alert_type_id=:alertTypeId ";
            sqlParam.addValue("alertTypeId", filterDto.getAlertTypeId());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (filterDto.getStartDate() != null && !filterDto.getStartDate().isEmpty()) {
            qry += " AND date(ad.gps_dtm) >= :startDate";
            Date startDate = null;
            try {
                startDate = format.parse(filterDto.getStartDate());
            } catch (Exception exception) {
                log.info("From Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("startDate", startDate, Types.DATE);
        }
        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
            qry += " AND date(ad.gps_dtm) <= :endDate";
            Date endDate = null;
            try {
                endDate = format.parse(filterDto.getEndDate());
            } catch (Exception exception) {
                log.info("To Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("endDate", endDate, Types.DATE);
        }
        UserInfoDto user = userRepositoryImpl.getUserByUserId(filterDto.getUserId());
        if (user.getUserLevelId() == 5) {
            qry += " AND gm.contractor_id=:contractorId ";
            sqlParam.addValue("contractorId", user.getUserLevelId());
        }
        else if (user.getUserLevelId() == 2) {
            List<Integer> distIds = userRepositoryImpl.getDistIdByUserId(filterDto.getUserId());
            List<Integer> blockIds = userRepositoryImpl.getBlockIdByDistId(distIds);
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByDistId(distIds);
            List<Integer> roadIds = masterRepositoryImpl.getRoadIdsByBlockAndDivision(distIds, blockIds, divisionIds);
            if(qry != null && qry.length() > 0){
                qry += " and gm.road_id in (:roadIds) ";
                sqlParam.addValue("roadIds", roadIds);
            } else {
                qry += " where gm.road_id in (:roadIds) ";
                sqlParam.addValue("roadIds", roadIds);
            }
        }
        else if(user.getUserLevelId()==3){
            List<Integer> blockIds=userRepositoryImpl.getBlockIdByUserId(filterDto.getUserId());
            List<Integer> roadIds = masterRepositoryImpl.getRoadIdsByBlockIds(blockIds);
            if(roadIds != null && roadIds.size() > 0) {
                if(qry != null && qry.length() > 0){
                    qry += " and gm.road_id in (:roadIds) ";
                    sqlParam.addValue("roadIds", roadIds);
                } else {
                    qry += " where gm.roadId in (:roadIds) ";
                    sqlParam.addValue("roadIds", roadIds);
                }
            }
        }
        else if(user.getUserLevelId()==4){
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByUserId(filterDto.getUserId());
            List<Integer> roadIds = userRepositoryImpl.getRoadIdByDivisionId(divisionIds);
            if(roadIds != null && roadIds.size() > 0){
                if(qry != null && qry.length() > 0){
                    qry += " and gm.road_id in(:roadIds) ";
                    sqlParam.addValue("roadIds", roadIds);
                } else {
                    qry += " where gm.road_id in(:roadIds) ";
                    sqlParam.addValue("roadIds", roadIds);
                }
            }
        }

        resultCount = count(qry, sqlParam);
        if (filterDto.getLimit() > 0){
            qry += " Order by ad.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
        }

        List<AlertCountDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }



    public List<VtuLocationDto>  GeoFenceIntersectedRecords(String geom, List<VtuLocationDto> vtuLocationDto) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        List<VtuLocationDto> vtuLocationDto1=new ArrayList<>();

        VtuLocationDto vtuLocationDto2=new VtuLocationDto();

        for (VtuLocationDto item:vtuLocationDto) {
            String geomText ="'"+geom+"'";
            String qry = "  select ST_Intersects(st_setsrid(st_geomfromtext(  st_astext(ST_BUFFER("+geomText+"::geography,50)::geometry)  " +
                    " ),4326),  'SRID=4326;POINT("+item.getLongitude()+" "+item.getLongitude()+" )'::geometry) as inFlag" ;
           Boolean b= namedJdbc.queryForObject(qry, sqlParam,(Boolean.class));
           if (b){
               vtuLocationDto2.setLatitude(item.getLatitude());
               vtuLocationDto2.setLongitude(item.getLongitude());
               vtuLocationDto1.add(vtuLocationDto2);
               //Filter and push geo fenced Lat lon
           }
        }


        return vtuLocationDto1;
    }

    public List<AlertCountDto> getVehicleAlertForReport(AlertFilterDto filterDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select alert.imei,alert.id as alertId, alert_type_id,type.alert_type as alertType,latitude,longitude,altitude,accuracy,speed,gps_dtm,vdm.vehicle_id as vehicleId, \n" +
                " is_resolve,resolved_by as resolvedBy,userM.first_name as resolvedByUser, count(alert.id) over (partition by alert.alert_type_id)    \n" +
                " from  rdvts_oltp.alert_data  as alert \n" +
                " left join rdvts_oltp.alert_type_m as type on type.id=alert.alert_type_id and type.is_active=true   \n" +
                " left join rdvts_oltp.user_m as userM on userM.id=alert.resolved_by and userM.is_active=true    \n" +
                " left join rdvts_oltp.device_m as dm on dm.imei_no_1=alert.imei and dm.is_active=true \n" +
                " left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.device_id=dm.id and vdm.is_active=true \n" +
                " left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id=vdm.vehicle_id and vam.is_active=true \n" +
//                " left join rdvts_oltp.activity_work_mapping as awm on awm.activity_id=vam.activity_id  and awm.is_active=true \n" +
//                "  left join rdvts_oltp.geo_master as gm on gm.work_id=awm.work_id  where alert.is_active=true   " ;
                " left join rdvts_oltp.geo_mapping as gm on gm.id=vam.geo_mapping_id  where alert.is_active=true ";
//        if(filterDto.getBlockId() != null && filterDto.getBlockId() > 0){
//            qry += " and gm.block_id=:blockIds ";
//            sqlParam.addValue("blockId", filterDto.getBlockId());
//        }

        if (filterDto.getVehicleId() != null && filterDto.getVehicleId() > 0) {
            qry += " and vdm.vehicle_id=:vehicleId ";
            sqlParam.addValue("vehicleId", filterDto.getVehicleId());
        }
        if (filterDto.getPackageId() != null && filterDto.getPackageId() > 0) {
            qry += " AND gm.package_id = :pkgId ";
            sqlParam.addValue("pkgId", filterDto.getPackageId());
        }
        if (filterDto.getWorkId() != null && filterDto.getWorkId() > 0) {
            qry += " AND gm.work_id = :workId";
            sqlParam.addValue("workId", filterDto.getWorkId());
        }
        if (filterDto.getRoadId() != null && filterDto.getRoadId() > 0) {
            qry += " AND gm.road_id = :roadId";
            sqlParam.addValue("roadId", filterDto.getRoadId());
        }
        if (filterDto.getAlertTypeId() != null && filterDto.getAlertTypeId() > 0) {
            qry += " AND alert.alert_type_id=:alertTypeId ";
            sqlParam.addValue("alertTypeId", filterDto.getAlertTypeId());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (filterDto.getStartDate() != null && !filterDto.getStartDate().isEmpty()) {
            qry += " AND date(alert.gps_dtm) >= :startDate ";
            Date startDate = null;
            try {
                startDate = format.parse(filterDto.getStartDate());
            } catch (Exception exception) {
                log.info("From Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("startDate", startDate, Types.DATE);
        }
        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
            qry += " AND date(alert.gps_dtm) <= :endDate ";
            Date endDate = null;
            try {
                endDate = format.parse(filterDto.getEndDate());
            } catch (Exception exception) {
                log.info("To Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("endDate", endDate, Types.DATE);
        }
        UserInfoDto user = userRepositoryImpl.getUserByUserId(filterDto.getUserId());
        if (user.getUserLevelId() == 5) {
            qry += " AND gm.contractor_id=:contractorId ";
            sqlParam.addValue("contractorId", user.getUserLevelId());
        }

        else if (user.getUserLevelId() == 2) {
            List<Integer> distIds = userRepositoryImpl.getDistIdByUserId(filterDto.getUserId());
            List<Integer> blockIds = userRepositoryImpl.getBlockIdByDistId(distIds);
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByDistId(distIds);
            List<Integer> vehicleIds = masterRepositoryImpl.getVehicleIdsByBlockAndDivision(blockIds, divisionIds, distIds);
            if (qry != null && qry.length() > 0) {
                qry += " AND  vdm.vehicle_id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds", vehicleIds);
            } else {
                qry += " where vdm.vehicle_id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds", vehicleIds);
            }
        }

        else if(user.getUserLevelId()==3){
            List<Integer> blockIds=userRepositoryImpl.getBlockIdByUserId(filterDto.getUserId());
            List<Integer> vehicleId = masterRepositoryImpl.getVehicleIdsByBlockIds(blockIds);
            if(vehicleId != null && vehicleId.size() > 0) {
                if(qry != null && qry.length() > 0){
                    qry += " AND vdm.vehicle_id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds",vehicleId);
                } else {
                    qry += " where vdm.vehicle_id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds",vehicleId);
                }
            }
        }

        else if(user.getUserLevelId()==4){
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByUserId(filterDto.getUserId());
            List<Integer> vehicleIds = userRepositoryImpl.getVehicleIdByDivisionId(divisionIds);
            if(vehicleIds != null && vehicleIds.size() > 0){
                if(qry != null && qry.length() > 0){
                    qry += " AND vdm.vehicle_id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds", vehicleIds);
                } else {
                    qry += " where vdm.vehicle_id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds", vehicleIds);
                }
            }
        }
        qry += " order by alert.alert_type_id, alert.imei, alert.gps_dtm desc ";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));

    }


    public WorkCronEntity getWorkCronByWorkId(Integer workId) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = "SELECT id, imei_no, total_speed_work, avg_speed_today, total_active_vehicle, total_distance, today_distance, work_id " +
                "FROM rdvts_oltp.work_cron where work_id=:workId " ;
        sqlParam.addValue("workId", workId);

        try {
            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(WorkCronEntity.class));
        } catch (Exception exception) {
            return null;
        }


    }
    public PackageDto getPackageData(AlertFilterDto filter) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = "select distinct package.id as packageId,package.package_no as packageNo,piu.name as piuName, " +
                "geoMapping.award_date,geoMapping.pmis_finalize_date,geoMapping.completion_date,(case when (geoMapping.completion_date is null) then 'IN-PROGRESS' else 'COMPLETED' end) as workStatusName " +
                "from rdvts_oltp.package_m as package " +
                "left join rdvts_oltp.geo_mapping as geoMapping on geoMapping.package_id=package.id " +
                "left join rdvts_oltp.piu_id as piu on piu.id=geoMapping.piu_id  where package.id=:packageId and geoMapping.award_date is not null" ;
        sqlParam.addValue("packageId", filter.getPackageId());

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        if (filter.getStartDate() != null && !filter.getStartDate().isEmpty()) {
//            qry += " AND date(contract.contract_date) >= :uploadFromDate";
//            Date uploadFromDate = null;
//            try {
//                uploadFromDate = format.parse(filter.getStartDate());
//            } catch (Exception exception) {
//                log.info("From Date Parsing exception : {}", exception.getMessage());
//            }
//            sqlParam.addValue("uploadFromDate", uploadFromDate, Types.DATE);
//        }
//        if (filter.getEndDate() != null && !filter.getEndDate().isEmpty()) {
//            qry += " AND date(contract.contract_date) <= :uploadToDate";
//            Date uploadToDate = null;
//            try {
//                uploadToDate = format.parse(filter.getEndDate());
//            } catch (Exception exception) {
//                log.info("To Date Parsing exception : {}", exception.getMessage());
//            }
//            sqlParam.addValue("uploadToDate", uploadToDate, Types.DATE);
//        }

        try
        {
            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(PackageDto.class));
        }
        catch (Exception exception)
        {
            return null;
        }
    }
    public List<Integer> getVehicleByRoadId(Integer roadId){
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = "Select distinct vam.vehicle_id \n" +
                "from rdvts_oltp.road_m as rm\n" +
                "left join rdvts_oltp.geo_mapping as gm on gm.road_id = rm.id and gm.is_active ='true'\n" +
                "left join rdvts_oltp.package_m as pm on pm.id = gm.package_id and gm.is_active ='true'\n" +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id = gm.id and vam.is_active ='true'\n" +
                "left join rdvts_oltp.vehicle_m as vm on vam.vehicle_id = vm.id and vm.is_active ='true'\n" +
                "where rm.id = :roadId and vam.vehicle_id is not null" ;
        sqlParam.addValue("roadId", roadId);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }
    public List<VehicleMasterDto> getVehicleByPackageIdAndFilter(AlertFilterDto filter,Integer roadId) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        List<Integer> vehicleIdList= new ArrayList<>();
        if(filter.getVehicleId()==null )
        {
          List<Integer> vehicle= getVehicleByRoadId(roadId);
            vehicleIdList.addAll(vehicle);
        }
        String qry = "Select * from  rdvts_oltp.vehicle_m  where is_active=true ";
        if(vehicleIdList!=null && vehicleIdList.size()>0){
            qry+= " and id in(:vehicleIds) ";
            sqlParam.addValue("vehicleIds", vehicleIdList);
        }
        else
        {
            if( filter.getVehicleId()!=null &&   filter.getVehicleId() >0)
            {

            qry+= " and id=:vehicleId ";
            sqlParam.addValue("vehicleId", filter.getVehicleId());
            }
            else
            {
                qry+= "and id = 0 ";
            }
        }

        try {
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        } catch (Exception exception)
        {
            return null;
        }
    }

    public List<ActivityDto> getActivityByVehicleId(Integer  vehicleId) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = " select * from rdvts_oltp.activity_m where id in(select activity_id from rdvts_oltp.vehicle_activity_mapping where vehicle_id=:vehicleId)" ;
        sqlParam.addValue("vehicleId", vehicleId);

        try {
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));
        } catch (Exception exception) {
            return null;
        }
    }
    public List<AlertDto> getAlertByVehicleId(Integer  vehicle, Integer alertId) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = " select ad.id, ad.alert_type_id, alt.alert_type as alert_type_name, ad.imei,ad.latitude, ad.longitude, ad.altitude, ad.gps_dtm " +
                "from rdvts_oltp.alert_data as ad " +
                "left join rdvts_oltp.alert_type_m as alt on alt.id = ad.alert_type_id " +
                "where imei in " +
                "(select imei_no_1 from rdvts_oltp.device_m where id in(select device_id from rdvts_oltp.vehicle_device_mapping where vehicle_id=:vehicleId and is_active=true)) " ;
        sqlParam.addValue("vehicleId", vehicle);

        if(alertId!=null && alertId>0)
        {
            qry+= " and alert_type_id = :alertTypeId";
            sqlParam.addValue("alertTypeId", alertId);
        }
        try {
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class));
        } catch (Exception exception) {
            return null;
        }
    }
    public List<ActivityDto> getActivityByRoadId(Integer packageId, Integer  roadId, Integer activityId) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        String qry = "select distinct activityStatus.name as status,\n" +
                "activity.id,COALESCE(activity.activity_name,' ') as activityName,activity_start_date,activity_completion_date,actual_activity_start_date,actual_activity_completion_date,roadm.road_name\n" +
                "from rdvts_oltp.geo_mapping as geoMapping\n" +
                "left join rdvts_oltp.activity_m as activity on geoMapping.activity_id=activity.id\n" +
                "left join rdvts_oltp.activity_status_m as activityStatus on activityStatus.id=geoMapping.activity_status\n" +
                "left join rdvts_oltp.road_m as roadm on roadm.id = geoMapping.road_id\n" +
                "where geoMapping.road_id= :roadId and geoMapping.package_id=:packageId" ;
        sqlParam.addValue("roadId", roadId);
        sqlParam.addValue("packageId", packageId);

        if(activityId!=null && activityId>0)
        {
            qry+= " and activity.id= :activityId";
            sqlParam.addValue("activityId", activityId);
        }
        try {
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));
        } catch (Exception exception) {
            return null;
        }
    }
    public List<VehicleMasterDto> getVehicleByActivityId(Integer packageId, Integer  activity, Integer vehicleId,Integer roadId,String startDate, String endDate)
    {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

//        String qry = " select distinct vehicle.id as vehicleId,vehicle_no,type.name as vehicleTypeName,contractor.name as ownerName,speed_limit,chassis_no,engine_no,model,device.imei_no_1 as imeiNo1 from rdvts_oltp.vehicle_m as vehicle\n" +
//                "left join rdvts_oltp.vehicle_activity_mapping as vehicleActivity on vehicleActivity.vehicle_id=vehicle.id\n" +
//                "left join rdvts_oltp.vehicle_type as type on type.id=vehicle.vehicle_type_id\n" +
//                "left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vehicle.id\n" +
//                "left join rdvts_oltp.contractor_m as contractor on contractor.id=owner.contractor_id\n" +
//                "left join rdvts_oltp.vehicle_device_mapping as vehicleDevice on vehicleDevice.vehicle_id=vehicle.id\n" +
//                "left join rdvts_oltp.device_m as device on device.id=vehicleDevice.device_id\n" +
//                "left join rdvts_oltp.geo_mapping as gm on gm.id=vehicleActivity.geo_mapping_id" +
//                " where  vehicle.is_active=true and vehicleActivity.is_active=true and type.is_active=true \n" +
//                " and owner.is_active=true and contractor.is_active=true and vehicleDevice.is_active=true and device.is_active=true \n" +
//                " and gm.activityId=:activityId and gm.packageId=:packageId" ;

        String qry = "select distinct vehicle.id as vehicleId,vehicle_no,type.name as vehicleTypeName,contractor.name as ownerName,speed_limit,chassis_no,engine_no," +
                    " model,device.imei_no_1 as imeiNo1" +
                    " from rdvts_oltp.geo_mapping as geoMapping" +
                    " left join rdvts_oltp.activity_m as am on geoMapping.activity_id = am.id" +
                    " left join rdvts_oltp.activity_status_m as activityStatus on activityStatus.id = geoMapping.activity_status" +
                    " left join rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id = geoMapping.activity_id" +
                    " left join rdvts_oltp.vehicle_m as vehicle on vam.vehicle_id = vehicle.id" +
                    " left join rdvts_oltp.vehicle_type as type on type.id = vehicle.vehicle_type_id" +
                    " left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id = vehicle.id" +
                    " left join rdvts_oltp.contractor_m as contractor on contractor.id = owner.contractor_id" +
                    " left join rdvts_oltp.vehicle_device_mapping as vehicleDevice on vehicleDevice.vehicle_id = vehicle.id" +
                    " left join rdvts_oltp.device_m as device on device.id = vehicleDevice.device_id" +
                    " where geoMapping.is_active=true" +
                    " and geoMapping.package_id=:packageId and geoMapping.activity_id=:activityId  and vam.vehicle_id is not null";


//        String qry = " select distinct vam.vehicle_id,vehicle.vehicle_no, " +
//                " type.name as vehicleTypeName,contractor.name as ownerName,vehicle.speed_limit,vehicle.chassis_no,vehicle.engine_no,vehicle.model,device.imei_no_1 as imeiNo1 " +
//                " from rdvts_oltp.vehicle_activity_mapping as vam " +
//                " left join rdvts_oltp.geo_mapping as geoMapping on vam.geo_mapping_id=geoMapping.id and geoMapping.is_active=true " +
//                " left join rdvts_oltp.vehicle_m as vehicle on vehicle.id=vam.vehicle_id " +
//                " left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.vehicle_id=vam.vehicle_id " +
//                " left join rdvts_oltp.device_m as device on device.id=vdm.device_id " +
//                " left join rdvts_oltp.vehicle_type as type on type.id = vehicle.vehicle_type_id " +
//                " left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id = vehicle.id " +
//                " left join rdvts_oltp.contractor_m as contractor on contractor.id = owner.contractor_id " +
//                " where vam.is_active=true and geoMapping.package_id=:packageId and geoMapping.road_id=:roadId  and geoMapping.activity_id=:activityId " +
//                " order by vam.vehicle_id";

        sqlParam.addValue("activityId", activity);
        sqlParam.addValue("packageId", packageId);
        sqlParam.addValue("roadId", roadId);

        if(vehicleId!=null && vehicleId>0)
        {
            qry+= " and vehicle.id= :vehicleId";
            sqlParam.addValue("vehicleId", vehicleId);
        }
//        if(startDate!=null && !startDate.isEmpty())
//        {
//            qry+= " and vehicleDevice.installation_date = :startDate";
//            sqlParam.addValue("startDate", startDate);
//        }
//        if(endDate!=null && !endDate.isEmpty())
//        {
//            qry+= " and vehicleDevice.unassigned_date = :endDate";
//            sqlParam.addValue("endDate", endDate);
//        }else{
//            qry+= " and vehicleDevice.installation_date= now()";
//        }
        try {
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        } catch (Exception exception) {
            return null;
        }
    }


//    public List<AlertCountDto> getTotalAlertToday(int id) {
//    }
}
