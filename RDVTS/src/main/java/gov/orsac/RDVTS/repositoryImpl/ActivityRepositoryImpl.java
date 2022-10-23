package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
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
                "am.actual_activity_completion_date,am.executed_quantity,am.work_id,am.activity_status,status.name,work.g_work_name,work.work_status  " +
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
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        pageable = PageRequest.of(activity.getDraw() - 1, activity.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        int resultCount = 0;
        String qry = "select activity.id, activity_name,work.g_work_name as workName,construction.road_name,status.name as status,"+
                "activity.activity_start_date as startDate,activity.activity_completion_date as  endDate " +
                "from rdvts_oltp.activity_m as activity " +
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
            qry+="and activity.activity_status=:activityStatus ";
            sqlParam.addValue("activityStatus",activity.getActivityStatus());
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
            qry += " LIMIT " + activity.getLimit() + " OFFSET " + activity.getOffSet();
        }
        List<ActivityDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }
}



