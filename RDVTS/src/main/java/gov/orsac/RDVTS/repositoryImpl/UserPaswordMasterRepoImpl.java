package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.UserPasswordMasterDto;
import gov.orsac.RDVTS.entities.UserPasswordMasterEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserPaswordMasterRepoImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public UserPasswordMasterDto getPasswordByUserId(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT  userPassM.id, userPassM.user_id, userPassM.password, userPassM.is_active, userPassM.created_by, userPassM.created_on, userPassM.updated_by, " +
                "userPassM.updated_on FROM rdvts_oltp.user_password_m AS userPassM " +
                "WHERE userPassM.user_id =:userId";
        sqlParam.addValue("userId", userId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(UserPasswordMasterDto.class));
    }

    public UserPasswordMasterDto getPasswordById(Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT  userPassM.id, userPassM.user_id, userPassM.password, userPassM.is_active, userPassM.created_by, userPassM.created_on, userPassM.updated_by, " +
                "userPassM.updated_on FROM rdvts_oltp.user_password_m AS userPassM " +
                "WHERE userPassM.id =:id";
        sqlParam.addValue("id", id);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(UserPasswordMasterDto.class));
    }

    public int savePasswordInHistory(Integer userId, UserPasswordMasterDto userPasswordMasterDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        UserPasswordMasterEntity userPasswordMasterEntity = new UserPasswordMasterEntity();
            String qry = "INSERT INTO rdvts_oltp.user_password_log(user_id, password, is_active, created_by, updated_by, updated_on) " +
                    "select user_id,password, is_active, created_by, updated_by, updated_on from rdvts_oltp.user_password_m where user_id =:userId ";
            sqlParam.addValue("userId", userId);
    //        sqlParam.addValue("password", userPasswordMasterDto.getPassword());
    //        sqlParam.addValue("isActive", userPasswordMasterDto.getIsActive());
    //        sqlParam.addValue("createdBy", userPasswordMasterDto.getCreatedBy());
    //        sqlParam.addValue("updatedBy", userPasswordMasterDto.getUpdatedBy());
    //        sqlParam.addValue("updatedOn", userPasswordMasterDto.getUpdatedOn());
            int update = namedJdbc.update(qry, sqlParam);
            if(update>0){
                return 1;
            }
            return 0;
        }
}


