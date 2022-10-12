package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.RoleMenuInfo;
import gov.orsac.RDVTS.dto.VehicleFilterDto;
import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.dto.VehicleTypeDto;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleRepositoryImpl implements VehicleRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;
    @Override
    public VehicleMasterDto getVehicleByVId(Integer vehicleId) {
        VehicleMasterDto vehicle = new VehicleMasterDto();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicleTypeName,vm.model,vm.speed_limit," +
                "vm.chassis_no,vm.engine_no,vm.is_active as active," +
                "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on " +
                "FROM rdvts_oltp.vehicle_m as vm left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id ";
        if(vehicleId>0){
            qry+=" where vm.id=:vehicleId";
        }
        sqlParam.addValue("vehicleId", vehicleId);
        try {
            vehicle = namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        return vehicle;
    }

    @Override
    public List<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicleTypeName,vm.model,vm.speed_limit," +
        "vm.chassis_no,vm.engine_no,vm.is_active as active," +
        "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on " +
        "FROM rdvts_oltp.vehicle_m as vm left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }

    @Override
    public List<VehicleTypeDto> getVehicleTypeList() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT id, name FROM rdvts_oltp.vehicle_type where is_active=true ";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleTypeDto.class));
    }
}
