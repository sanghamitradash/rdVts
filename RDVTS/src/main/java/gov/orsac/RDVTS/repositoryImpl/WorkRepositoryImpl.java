package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.WorkDto;
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
            pageable = PageRequest.of(workDto.getPage(), workDto.getSize(), Sort.Direction.fromString(workDto.getSortOrder()), workDto.getSortBy());
            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        }
        int resultCount = 0;
        String qry = "select id, g_work_id as workId,g_work_name as workName,is_active,created_by,updated_by,created_on,updated_on from rdvts_oltp.work_m where is_active = true";

        resultCount = count(qry, sqlParam);
        if (workDto.getSize() > 0) {
            qry += " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();
        }

        List<WorkDto> workDtoList = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkDto.class));
        return new PageImpl<>(workDtoList, pageable, resultCount);
    }
}
