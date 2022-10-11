package gov.orsac.RDVTS.repositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DesignationRepositoryImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public boolean activateAndDeactivateDesignation(int id){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.designation_m SET is_active = false WHERE id= :id ";
        sqlParam.addValue("id",id);
        int update = namedJdbc.update(qry, sqlParam);
        return update>0;
    }
}
