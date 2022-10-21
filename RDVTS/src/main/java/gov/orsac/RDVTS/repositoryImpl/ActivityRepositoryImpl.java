package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.ActivityDto;
import gov.orsac.RDVTS.dto.ContractorDto;
import gov.orsac.RDVTS.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityRepositoryImpl implements ActivityRepository {


    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    @Override
    public ActivityDto getActivityById(Integer activityId, Integer userId) {

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

           // activity = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));

        return null;
    }


    }

