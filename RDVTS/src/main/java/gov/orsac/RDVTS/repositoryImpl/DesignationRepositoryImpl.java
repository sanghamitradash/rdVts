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
//        PageRequest pageable = null;
//        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
//            pageable = PageRequest.of(designationDto.getOffSet(), designationDto.getLimit(), Sort.Direction.fromString("desc"),"id");
//            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        int pageNo = designationDto.getOffSet()/designationDto.getLimit();
        PageRequest pageable = PageRequest.of(pageNo, designationDto.getLimit(),Sort.Direction.fromString("asc"), "id");
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0): new Sort.Order(Sort.Direction.DESC,"id");

        int resultCount = 0;
        String qry = "select ac.*,b.name as parent_name,ul.name as user_level_name " +
                "from rdvts_oltp.designation_m ac " +
                "left join rdvts_oltp.designation_m b on ac.parent_id=b.id " +
                "left join rdvts_oltp.user_level_m as ul on ul.id=ac.user_level_id " +
                "where ac.is_active='t' ";
        if (designationDto.getId() > 0 ){
            qry+= "and ac.id = :id";
            sqlParam.addValue("id", designationDto.getId());
        }
        else if (designationDto.getUserLevelId() > 0 ){
            qry+= "and ac.user_level_id = :userLevelId";
            sqlParam.addValue("userLevelId", designationDto.getUserLevelId());
            if (designationDto.getParentId() > 0){
                qry+= " and ac.parent_id = :parentId";
                sqlParam.addValue("parentId", designationDto.getParentId());
            }
        }
        else if (designationDto.getParentId() > 0){
            qry+= "and ac.parent_id = :parentId";
            sqlParam.addValue("parentId", designationDto.getParentId());
            if (designationDto.getUserLevelId() > 0 ){
                qry+= " and ac.user_level_id = :userLevelId";
                sqlParam.addValue("userLevelId", designationDto.getUserLevelId());
            }
        }

        resultCount = count(qry, sqlParam);
        if (designationDto.getLimit() > 0) {
            qry += " LIMIT " + designationDto.getLimit() + " OFFSET " + designationDto.getOffSet();
        }
//        System.out.println(qry);

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

    public List<DesignationDto> getDesignationById(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        if (id == -1) {
            qry += "select ac.*,b.name as parent_name,ul.name as user_level_name from \n" +
                    "rdvts_oltp.designation_m ac \n" +
                    "left join rdvts_oltp.designation_m b on ac.parent_id=b.id \n" +
                    "left join rdvts_oltp.user_level_m as ul on ul.id=ac.user_level_id \n" +
                    "where ac.is_active='t' order by id ";
            /* " WHERE true AND id>1 Order BY id";*/
        } else {
            qry += "select ac.*,b.name as parent_name,ul.name as user_level_name from \n" +
                    "rdvts_oltp.designation_m ac \n" +
                    "left join rdvts_oltp.designation_m b on ac.parent_id=b.id \n" +
                    "left join rdvts_oltp.user_level_m as ul on ul.id=ac.user_level_id \n" +
                    "where ac.is_active='t' and ac.id=:id order by id ";
            /*   "AND id>1 ORDER BY id";*/
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DesignationDto.class));
    }
}
