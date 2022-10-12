package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.DesignationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class DesignationRepositoryImpl {

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

    public boolean activateAndDeactivateDesignation(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.designation_m SET is_active = false WHERE id= :id ";
        sqlParam.addValue("id", id);
        int update = namedJdbc.update(qry, sqlParam);
        return update > 0;
    }

    public Page<DesignationDto> getDesignationList(DesignationDto designationDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        if (designationDto != null) {
            pageable = PageRequest.of(designationDto.getPage(), designationDto.getSize(), Sort.Direction.fromString(designationDto.getSortOrder()), designationDto.getSortBy());
            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        }
        int resultCount = 0;
        String qry = "select * from rdvts_oltp.designation_m where is_active = true";

        resultCount = count(qry, sqlParam);
        if (designationDto.getSize() > 0) {
            qry += " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();
        }

        List<DesignationDto> designationDtos = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DesignationDto.class));
        return new PageImpl<>(designationDtos, pageable, resultCount);
    }
}
