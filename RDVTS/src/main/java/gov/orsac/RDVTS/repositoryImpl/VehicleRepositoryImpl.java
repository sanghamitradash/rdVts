package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.RoleMenuInfo;
import gov.orsac.RDVTS.dto.VehicleFilterDto;
import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.dto.VehicleTypeDto;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleRepositoryImpl implements VehicleRepository {

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
    public Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
            pageable = PageRequest.of(vehicle.getOffSet(),vehicle.getLimit(), Sort.Direction.fromString("desc"), "id");
            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;
        String qry ="SELECT vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicleTypeName,vm.model,vm.speed_limit," +
        "vm.chassis_no,vm.engine_no,vm.is_active as active," +
        "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on " +
        "FROM rdvts_oltp.vehicle_m as vm left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id";


        resultCount = count(qry, sqlParam);
        if (vehicle.getLimit() > 0){
            qry += " LIMIT " +vehicle.getLimit() + " OFFSET " + vehicle.getOffSet();
        }
        List<VehicleMasterDto> list=namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }

    @Override
    public List<VehicleTypeDto> getVehicleTypeList() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT id, name FROM rdvts_oltp.vehicle_type where is_active=true ";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleTypeDto.class));
    }
}
