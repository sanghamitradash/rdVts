package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.VehicleActivityMappingEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityRepositoryImpl implements ActivityRepository {


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



    @Override
    public List<ActivityDto> getActivityById(Integer activityId, Integer userId) {

        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT am.id,am.activity_name,am.activity_quantity,am.activity_start_date,am.activity_completion_date,am.actual_activity_start_date, " +
                "am.actual_activity_completion_date,am.executed_quantity,am.work_id,am.activity_status,status.name,work.g_work_name,work.work_status,am.g_activity_id  " +
                "from rdvts_oltp.activity_m as am  " +
                "left join rdvts_oltp.activity_status_m as status on status.id = am.activity_status  " +
                "left join rdvts_oltp.work_m as work on work.id =am.work_id   " +
                "WHERE am.is_active = true   " ;

        if(activityId>0){
            qry+=" AND am.id=:activityId";
        }
        sqlParam.addValue("activityId", activityId);
        sqlParam.addValue("userId",userId);

            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));

    }
    public List<ActivityDto> getActivityDD() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id, activity_name  " +
                " FROM rdvts_oltp.activity_m";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));
    }


    @Override
    public Page<ActivityDto> getActivityList(ActivityListDto activity) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        int pageNo = activity.getOffSet()/activity.getLimit();
        PageRequest pageable = PageRequest.of(pageNo, activity.getLimit(), Sort.Direction.fromString("asc"), "id");
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");

        int resultCount = 0;
        String qry = "select distinct activity.id, activity_name,work.g_work_name as workName,construction.road_name,status.id as activityStatus,status.name as status," +
                "activity.activity_start_date as startDate,activity.activity_completion_date as  endDate " +
                "from rdvts_oltp.activity_m as activity " +
                "left join rdvts_oltp.vehicle_activity_mapping as vehicleActivity on vehicleActivity.activity_id=activity.id " +
                "left join rdvts_oltp.activity_status_m as status on status.id=activity.activity_status " +
                "left join rdvts_oltp.work_m as work on activity.work_id=work.id " +
                "left join rdvts_oltp.geo_master as master on master.work_id=work.id " +
                "left join rdvts_oltp.geo_construction_m as construction on construction.id=master.road_id where activity.is_active=true ";
        if(activity.getWorkId()!=null && activity.getWorkId() >0){
            qry+=" and work.id=:workId ";
            sqlParam.addValue("workId",activity.getWorkId());
        }
        if(activity.getRoadId()!=null && activity.getRoadId() >0){
            qry+="and construction.id=:roadId ";
            sqlParam.addValue("roadId",activity.getRoadId());
        }
        if(activity.getActivityId()!=null && activity.getActivityId() >0){
            qry+="and activity.id=:activityId ";
            sqlParam.addValue("activityId",activity.getActivityId());
        }
        if(activity.getActivityStatus()!=null && activity.getActivityStatus() >0){
            qry+=" and activity.activity_status=:activityStatus ";
            sqlParam.addValue("activityStatus",activity.getActivityStatus());
        }
        if(activity.getVehicleId()!=null && activity.getVehicleId() >0){
            qry+=" and vehicleActivity.vehicle_id=:vehicleId ";
            sqlParam.addValue("vehicleId",activity.getVehicleId());
        }
        if(activity.getStartDate() != null && !activity.getStartDate().isEmpty()){
            qry+=" and activity.activity_start_date >=:startDate ";
            sqlParam.addValue("startDate",activity.getStartDate());
        }
        if(activity.getEndDate() != null && !activity.getEndDate().isEmpty()){
            qry+=" activity.activity_completion_date <=:endDate ";
            sqlParam.addValue("endDate",activity.getEndDate());
        }
        if(activity.getActualStartDate() != null && !activity.getActualStartDate().isEmpty()){
            qry+=" and activity.actual_activity_start_date >=:actualStartDate ";
            sqlParam.addValue("actualStartDate",activity.getActualStartDate());
        }
        if(activity.getActualEndDate() != null && !activity.getActualEndDate().isEmpty()){
            qry+=" and activity.actual_activity_completion_date <=:actualEndDate ";
            sqlParam.addValue("actualEndDate",activity.getActualEndDate());
        }

        resultCount = count(qry, sqlParam);
        if (activity.getLimit() > 0) {
            qry +=  " LIMIT " + activity.getLimit() + " OFFSET " + activity.getOffSet();
        }
        List<ActivityDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }

    public Integer updateWorkId(Integer workId, Integer activityId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry="UPDATE rdvts_oltp.activity_m " +
                " SET work_id=:workId " +
                " WHERE id=:activityId";
        sqlParam.addValue("workId", workId);
        sqlParam.addValue("activityId", activityId);
        return namedJdbc.update(qry, sqlParam);
    }

    public Integer updateWorkActivity(Integer workId, Integer activityId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry="UPDATE rdvts_oltp.activity_m " +
                " SET work_id=:workId " +
                " WHERE id=:activityId";
        sqlParam.addValue("workId", workId);
        sqlParam.addValue("activityId", activityId);
        sqlParam.addValue("userId", userId);
        return namedJdbc.update(qry, sqlParam);
    }

    public Boolean workActivityDeassign(Integer activityId, Integer workId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry= " UPDATE rdvts_oltp.activity_m " +
                "SET  work_id=null " +
                " WHERE id=:activityId and work_id=:workId";
        sqlParam.addValue("activityId", activityId);
        sqlParam.addValue("workId", workId);
        sqlParam.addValue("userId", userId);
        int update = namedJdbc.update(qry, sqlParam);
        boolean result = false;
        if (update > 0) {
            result = true;
        }
        return result;
    }

    public Boolean vehicleActivityDeassign(Integer activityId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry= " UPDATE rdvts_oltp.vehicle_activity_mapping " +
                " SET  is_active=false " +
                " WHERE activity_id=:activityId ; ";
        sqlParam.addValue("activityId", activityId);
        int update = namedJdbc.update(qry, sqlParam);
        boolean result = false;
        if (update >= 0) {
            result = true;
        }
        return result;
    }

    public List<VehicleMaster> unassignVehicleByVehicleTypeId(Integer activityId, Integer vehicleTypeId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select id, vehicle_no from rdvts_oltp.vehicle_m " +
                "where id not in (select vehicle_id from rdvts_oltp.vehicle_activity_mapping where activity_id=:activityId) " +
                "and vehicle_type_id=:vehicleTypeId";
        sqlParam.addValue("activityId", activityId);
        sqlParam.addValue("vehicleTypeId", vehicleTypeId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMaster.class));
    }


    public List<ActivityDto> unassignedActivity() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT id, activity_name FROM rdvts_oltp.activity_m where work_id is null ";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));
    }

    public Boolean activityVehicleDeassign(Integer vehicleId, Integer activityId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "update rdvts_oltp.vehicle_activity_mapping " +
                "set is_active=false " +
                "where activity_id=:activityId and vehicle_id=:vehicleId";
        sqlParam.addValue("activityId", activityId);
        sqlParam.addValue("vehicleId", vehicleId);
        int update = namedJdbc.update(qry, sqlParam);
        Boolean result = false;
        if(update>0){
            result=true;
        }
        return  result;
    }

    public List<ActivityStatusDto> activityStatusDD() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry =  "select * from rdvts_oltp.activity_status_m ";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityStatusDto.class));
    }

    public List<VehicleMasterDto> getVehicleByActivityId(Integer activityId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT distinct vm.id,vm.vehicle_no,vm.vehicle_type_id,type.name as vehicleTypeName,vm.model,vm.speed_limit,vm.chassis_no,vm.engine_no  " +
                "from rdvts_oltp.vehicle_m as vm  " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id = vm.id  " +
                "left join rdvts_oltp.vehicle_type as type on type.id = vm.vehicle_type_id  " +
                "WHERE vm.is_active = true AND  vam.activity_id=:activityId  " ;
        sqlParam.addValue("activityId",activityId);
        sqlParam.addValue("userId",userId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }
}



