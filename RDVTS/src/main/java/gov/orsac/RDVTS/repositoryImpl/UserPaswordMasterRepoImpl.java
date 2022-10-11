package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.UserPasswordMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserPaswordMasterRepoImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public UserPasswordMasterDto getPasswordByUserId(Integer userId){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT  userPassM.id, userPassM.user_id, userPassM.password, userPassM.is_active, userPassM.created_by, userPassM.created_on, userPassM.updated_by, " +
                "userPassM.updated_on FROM rdvts_oltp.user_password_m AS userPassM " +
                "WHERE userPassM.user_id =:userId";
        sqlParam.addValue("userId", userId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(UserPasswordMasterDto.class));
    }

    public UserPasswordMasterDto getPasswordById(Integer id){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT  userPassM.id, userPassM.user_id, userPassM.password, userPassM.is_active, userPassM.created_by, userPassM.created_on, userPassM.updated_by, " +
                "userPassM.updated_on FROM rdvts_oltp.user_password_m AS userPassM " +
                "WHERE userPassM.id =:id";
        sqlParam.addValue("id", id);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(UserPasswordMasterDto.class));
    }

}
