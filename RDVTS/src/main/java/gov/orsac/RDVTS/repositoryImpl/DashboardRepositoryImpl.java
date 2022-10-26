package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;
import gov.orsac.RDVTS.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    @Override
    public Integer getTotalVehicle() {
            MapSqlParameterSource sqlParam = new MapSqlParameterSource();
            String qry = "select count(id) from rdvts_oltp.vehicle_m " ;
            return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }

    @Override
    public Integer getTotalActive() {
            MapSqlParameterSource sqlParam = new MapSqlParameterSource();
            String qry = "select count(id) from rdvts_oltp.vehicle_device_mapping where is_active=true and deactivation_date is null" ;
            return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }

    @Override
    public Integer getTotalWork() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select count(id) from rdvts_oltp.work_m where is_active=true " ;
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }

    @Override
    public Integer getCompletedWork() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select count(id) from rdvts_oltp.work_m where is_active=true and work_status is null " ;
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }
}
