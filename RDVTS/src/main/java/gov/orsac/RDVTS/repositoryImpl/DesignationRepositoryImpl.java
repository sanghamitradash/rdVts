package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.DesignationDto;
import gov.orsac.RDVTS.dto.RoleDto;
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
            pageable = PageRequest.of(designationDto.getOffSet(), designationDto.getLimit(), Sort.Direction.fromString("desc"),"id");
            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        int resultCount = 0;
        String qry = "select * from rdvts_oltp.designation_m where is_active = true";

        resultCount = count(qry, sqlParam);
        if (designationDto.getLimit() > 0) {
            qry += " LIMIT " + designationDto.getLimit() + " OFFSET " + designationDto.getOffSet();
        }

        List<DesignationDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DesignationDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }


    public List<DesignationDto> getDesignationByUserLevelId(int userLevelId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        if (userLevelId == -1) {
            qry += "SELECT id, name, description, parent_id,created_by,created_on,updated_by,updated_on, user_level_id as userLevelId,is_active as active FROM rdvts_oltp.designation_m ";
            /* " WHERE true AND id>1 Order BY id";*/
        } else {
            qry += "SELECT id, name, description, parent_id, created_by,created_on,updated_by,updated_on, user_level_id as userLevelId,is_active as active FROM rdvts_oltp.designation_m  " +
                    " WHERE true AND user_level_id =:userLevelId  ";
            /*   "AND id>1 ORDER BY id";*/
            sqlParam.addValue("userLevelId", userLevelId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DesignationDto.class));
    }
}
