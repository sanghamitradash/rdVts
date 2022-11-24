package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.AlertTypeEntity;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Slf4j
@Repository
public class AlertRepositoryImpl {

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

    public List<AlertCountDto> getTotalAlertToday(/*AlertFilterDto filterDto,*/ Integer id, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
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
                "where awm.is_active=true and wm.id=:workId and date(ad.gps_dtm)=date(now())  ";
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
        AlertDto alertDto=new AlertDto();
        String qry = " select case when count(*) >0 then true else false  end as bool FROM rdvts_oltp.alert_data  " +
                "WHERE imei=:imei AND alert_type_id=:noDataAlertId AND date(gps_dtm)=date(now()) AND is_resolve = false  " ;


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

    public List<VtuLocationDto> getAlertLocationOverSpeed(Long imei, double speedLimit) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on " +
                " FROM rdvts_oltp.vtu_location where is_active=true ";
        qry += " and imei =:imei1 and gps_fix::numeric =1 ";

        Date currentDateMinus = new Date(System.currentTimeMillis() - 900 * 1000); //5 minute in millisecond 60*5
//        DateTime dateTime = new DateTime();
//        dateTime = dateTime.minusMinutes(5);
//        Date modifiedDate = dateTime.toDate();
        Date currentDate=new Date();


        qry += "   AND date(date_time)=date(now()) AND  date_time BETWEEN :currentDateMinus AND :currentDate  order by date_time ASC ";
        sqlParam.addValue("imei1", imei);

        sqlParam.addValue("currentDateMinus", currentDateMinus);
        sqlParam.addValue("speedLimit", speedLimit);
        sqlParam.addValue("currentDate", currentDate);

        List<VtuLocationDto> vtuLocationDtoList= namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
      return vtuLocationDtoList;


    }

    public AlertTypeEntity getAlertTypeDetails(int i) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "SELECT * FROM rdvts_oltp.alert_type_m where is_active=true and id=:id" ;

        sqlParam.addValue("id", i);

        return namedJdbc.queryForObject(qry, sqlParam,new BeanPropertyRowMapper<>(AlertTypeEntity.class));
    }

    public Page<AlertCountDto> getAlertToday(AlertFilterDto filterDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
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
                "where awm.is_active=true and wm.id=:workId and date(ad.gps_dtm)=date(now())  ";
        sqlParam.addValue("workId", filterDto.getWorkId());
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

    public Page<AlertCountDto> getAlertTotal(AlertFilterDto filterDto) {
        PageRequest pageable = null;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        pageable = PageRequest.of(filterDto.getDraw()-1,filterDto.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "  select distinct wm.id as workId, ad.id as alertId, ad.alert_type_id as alertTypeId,ad.speed as speed,atm.alert_type as alertType, ad.latitude, ad.longitude, ad.accuracy, ad.speed, ad.altitude,   \n" +
                "  ad.gps_dtm as gpsDtm,count(ad.id) over (partition by ad.alert_type_id,wm.id) ,\n" +
                "  vdm.vehicle_id,vdm.device_id,awm.activity_id, awm.work_id,gm.dist_id,gm.division_id\n" +
                " from rdvts_oltp.work_m as wm  \n" +
                " left join rdvts_oltp.activity_work_mapping as awm on awm.work_id=wm.id   \n" +
                " left join rdvts_oltp.vehicle_activity_mapping as vam on awm.activity_id=vam.activity_id and vam.is_active=true   \n" +
                " left join rdvts_oltp.vehicle_device_mapping as vdm on vam.vehicle_id=vdm.vehicle_id and vdm.is_active=true   \n" +
                " left join rdvts_oltp.device_m as dm on vdm.device_id=dm.id and dm.is_active=true   \n" +
                " left join rdvts_oltp.geo_master as gm on gm.work_id=awm.work_id\n" +
                " left join rdvts_oltp.alert_data as ad on dm.imei_no_1=ad.imei and dm.is_active=true   \n" +
                " left join rdvts_oltp.alert_type_m as atm on atm.id=ad.alert_type_id  \n" +
                " where awm.is_active=true and wm.id=:workId    "  ;
        sqlParam.addValue("workId", filterDto.getWorkId());
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


        resultCount = count(qry, sqlParam);
        if (filterDto.getLimit() > 0){
            qry += " order by wm.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
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

        String qry = "select imei,alert.id as alertId, alert_type_id,type.alert_type as alertTypeName,latitude,longitude,altitude,accuracy,speed,gps_dtm,vdm.vehicle_id as vehicleId, " +
                " is_resolve,resolved_by as resolvedBy,userM.first_name as resolvedByUser , gm.road_id " +
                " from  rdvts_oltp.alert_data  as alert  " +
                " left join rdvts_oltp.alert_type_m as type on type.id=alert.alert_type_id    " +
                " left join rdvts_oltp.user_m as userM on userM.id=alert.resolved_by    " +
                " left join rdvts_oltp.device_m as dm on dm.imei_no_1=alert.imei  " +
                " left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.device_id=dm.id  " +
                " left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id=vdm.vehicle_id " +
                " left join rdvts_oltp.activity_work_mapping as awm on awm.activity_id=vam.activity_id  " +
                " left join rdvts_oltp.geo_master as gm on gm.work_id=awm.work_id " +
                " where vdm.vehicle_id=:vehicleId ";

        sqlParam.addValue("vehicleId", filterDto.getVehicleId());

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
            qry += " AND date(alert.gps_dtm) >= :startDate";
            Date startDate = null;
            try {
                startDate = format.parse(filterDto.getStartDate());
            } catch (Exception exception) {
                log.info("From Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("startDate", startDate, Types.DATE);
        }
        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
            qry += " AND date(alert.gps_dtm) <= :endDate";
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
        String qry = "  select distinct road.id as roadId, ad.id as alertId, ad.alert_type_id as alertTypeId, atm.alert_type as alertType, ad.gps_dtm, ad.latitude, ad.longitude, ad.altitude, ad.accuracy, ad.speed, ad.is_resolve, \n" +
                " ad.resolved_by, count(ad.id) over (partition by ad.alert_type_id,road.id) \n" +
                " from rdvts_oltp.geo_construction_m as road  \n" +
                " left join rdvts_oltp.geo_master as gm on gm.road_id=road.id  \n" +
                " left join rdvts_oltp.activity_work_mapping as awm on awm.work_id=gm.work_id  \n" +
                " left join rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id=awm.activity_id \n" +
                " left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.vehicle_id = vam.vehicle_id  \n" +
                " left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id  \n" +
                " left join rdvts_oltp.alert_data as ad on ad.imei=dm.imei_no_1  \n" +
                " left join rdvts_oltp.alert_type_m as atm on atm.id=ad.alert_type_id where gm.is_active=true and road.id=:roadId ";
        sqlParam.addValue("roadId", filterDto.getRoadId());
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

        resultCount = count(qry, sqlParam);
        if (filterDto.getLimit() > 0){
            qry += " Order by road.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
        }

        List<AlertCountDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertCountDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }

//    public List<AlertCountDto> getTotalAlertToday(int id) {
//    }
}
