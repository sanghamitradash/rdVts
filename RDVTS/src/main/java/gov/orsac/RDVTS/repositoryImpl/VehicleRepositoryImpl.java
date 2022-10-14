package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
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

import java.util.ArrayList;
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
    public VehicleDeviceMappingDto getVehicleDeviceMapping(Integer vehicleId) {
        VehicleDeviceMappingDto vehicleDevice = new VehicleDeviceMappingDto();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT id, vehicle_id, device_id, installation_date, installed_by, is_active, created_by, created_on, updated_by, updated_on " +
                "FROM rdvts_oltp.vehicle_device_mapping where vehicle_id=:vehicleId ";

        sqlParam.addValue("vehicleId", vehicleId);
        try {
            vehicleDevice = namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        return vehicleDevice;
    }
    @Override
    public List<VehicleWorkMappingDto> getVehicleWorkMapping(Integer vehicleId) {
        List<VehicleWorkMappingDto> vehicleWork = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT id, vehicle_id, work_id, start_time, end_time, start_date, end_date, is_active, created_by, created_on, updated_by, updated_on " +
                "FROM rdvts_oltp.vehicle_work_mapping where vehicle_id=:vehicleId";

        sqlParam.addValue("vehicleId", vehicleId);

        return   namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));


    }

    @Override
    public Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
            pageable = PageRequest.of(vehicle.getOffSet(),vehicle.getLimit(), Sort.Direction.fromString("desc"), "id");
            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
        int resultCount=0;
        String qry ="SELECT distinct vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicleTypeName,vm.model,vm.speed_limit," +
                "vm.chassis_no,vm.engine_no,vm.is_active as active," +
                "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on " +
                "FROM rdvts_oltp.vehicle_m as vm left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id " +
                "left join rdvts_oltp.vehicle_device_mapping as device on device.vehicle_id=vm.id " +
                "left join rdvts_oltp.vehicle_work_mapping as work on vm.id=work.vehicle_id where vm.is_active=true ";
        if(vehicle.getVehicleTypeId()>0){
            qry+=" and vm.vehicle_type_id=:vehicleTypeId ";
            sqlParam.addValue("vehicleTypeId",vehicle.getVehicleTypeId());
        }
        if(vehicle.getDeviceId()>0){
            qry+=" and device.device_id=:deviceId ";
            sqlParam.addValue("deviceId",vehicle.getDeviceId());
        }
        if(vehicle.getWorkId()>0){
            qry+=" and work.work_id=:workId ";
            sqlParam.addValue("workId",vehicle.getWorkId());
        }



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

    @Override
    public List<VehicleMasterDto> getUnAssignedVehicleData(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="select id,vehicle_no,model,chassis_no,engine_no from rdvts_oltp.vehicle_m " +
                "where id NOT IN " +
                "(select vehicle_id from rdvts_oltp.vehicle_device_mapping)";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }
}
