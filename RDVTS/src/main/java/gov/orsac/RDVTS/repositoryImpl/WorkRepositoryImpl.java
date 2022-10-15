package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RoleDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.WorkDto;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class WorkRepositoryImpl {

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


    public Page<WorkDto> getWorkList(WorkDto workDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        if (workDto != null) {
            pageable = PageRequest.of(workDto.getOffSet(), workDto.getLimit(), Sort.Direction.fromString("desc"), "id");
            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        }
        int resultCount = 0;
        String qry = "select id, g_work_id as workId,g_work_name as workName,is_active,created_by,updated_by,created_on,updated_on from rdvts_oltp.work_m where is_active = true";
        if (workDto.getWorkId() > 0) {
            qry += " AND work_id = :workId";
        }
        if (workDto.getId() > 0) {
            qry += " AND id = :id";
        }
        resultCount = count(qry, sqlParam);
        if (workDto.getLimit() > 0) {
            qry += " LIMIT " + workDto.getLimit() + " OFFSET " + workDto.getOffSet();
        }

        List<WorkDto> workDtoList = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkDto.class));
        return new PageImpl<>(workDtoList, pageable, resultCount);
    }


    public List<WorkDto> getWorkById(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        if (id == -1) {
            qry += "SELECT id, g_work_id as workId, g_work_name as workName, created_by,updated_by,created_on,updated_on,is_active as active FROM rdvts_oltp.work_m ";
            /* " WHERE true AND id>1 Order BY id";*/
        } else {
            qry += "SELECT id, g_work_id as workId, g_work_name as workName, created_by,updated_by,created_on,updated_on,is_active as active FROM rdvts_oltp.work_m " +
                    " WHERE true AND id =:id ";
            /*   "AND id>1 ORDER BY id";*/
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkDto.class));
    }

    public List<VehicleWorkMappingDto> getVehicleBywork(List<Integer> workIds){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, vehicle_id, work_id, start_time, end_time, start_date, end_date, is_active, created_by, created_on, updated_by, updated_on \n" +
                " FROM rdvts_oltp.vehicle_work_mapping where work_id IN(:workIds) ";
        /*   "AND id>1 ORDER BY id";*/
        sqlParam.addValue("workIds", workIds);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));

    }
}
