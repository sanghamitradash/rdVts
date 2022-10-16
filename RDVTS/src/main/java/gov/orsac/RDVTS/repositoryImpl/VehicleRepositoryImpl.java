package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.serviceImpl.HelperServiceImpl;
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
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private HelperServiceImpl helperServiceImpl;


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
    public VehicleDeviceInfo getVehicleDeviceMapping(Integer vehicleId) {
        VehicleDeviceInfo vehicleDevice = new VehicleDeviceInfo();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT vd.id, vd.vehicle_id, vd.device_id, vd.installation_date,vd.installed_by,device.imei_no_1 as imeiNo1,device.sim_icc_id_1 as simIccId1,"+
                "device.mobile_number_1 as mobileNumber1,device.imei_no_2 as imeiNo2,device.sim_icc_id_2 as simIccId2,"+
                "device.mobile_number_2 as mobileNumber2,device.model_name,device.device_no as deviceNo " +
                "FROM rdvts_oltp.vehicle_device_mapping as vd " +
                "left join rdvts_oltp.device_m as device on vd.device_id=device.id where vehicle_id=:vehicleId ";

        sqlParam.addValue("vehicleId", vehicleId);
        try {
            vehicleDevice = namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceInfo.class));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        return vehicleDevice;
    }

    @Override
    public List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId) {
        List<VehicleDeviceMappingDto> vehicleDevice = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT id, vehicle_id, device_id, installation_date, installed_by, is_active, created_by, created_on, updated_by, updated_on " +
                "FROM rdvts_oltp.vehicle_device_mapping where vehicle_id IN (:vehicleId) ";

        sqlParam.addValue("vehicleId", vehicleId);
        try {
            vehicleDevice = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        return vehicleDevice;
    }

    public List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId) {
        List<VehicleDeviceMappingDto> vehicleDevice = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT id, vehicle_id, device_id, installation_date, installed_by, is_active, created_by, created_on, updated_by, updated_on " +
                "FROM rdvts_oltp.vehicle_device_mapping where vehicle_id =:vehicleId ";

        sqlParam.addValue("vehicleId", vehicleId);

        return  namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
    }

    @Override
    public List<VehicleWorkMappingDto> getVehicleWorkMapping(Integer vehicleId) {
        List<VehicleWorkMappingDto> vehicleWork = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT workM.id,workM.vehicle_id,workM.work_id,workM.start_time,workM.end_time,workM.start_date," +
                "workM.end_date,work.g_work_name as workName,workM.is_active as active " +
                "FROM rdvts_oltp.vehicle_work_mapping  as workM " +
                "left join rdvts_oltp.work_m as work on work.id=workM.work_id where vehicle_id=:vehicleId";

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
                "left join rdvts_oltp.vehicle_work_mapping as work on vm.id=work.vehicle_id " +
                "left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vm.id where vm.is_active=true ";
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

        //Validation on basis of userLevel and lower level user
      /*  UserInfoDto user=userRepositoryImpl.getUserByUserId(vehicle.getUserId());
        if(user.userLevelId==5){
            qry+="and owner.contractor_id=:contractorId ";
            sqlParam.addValue("contractorId",vehicle.getUserId());
        }
        else{
           List<Integer> userIdList= helperServiceImpl.getLowerUserByUserId(vehicle.getUserId());
           qry+=" and owner.user_id IN (:userIdList) ";
           sqlParam.addValue("userIdList",userIdList);
        }*/



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
    public List<VehicleMasterDto> getUnAssignedVehicleData(List<Integer> userIdList,Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="select * from rdvts_oltp.vehicle_m as vm join " +
                "rdvts_oltp.vehicle_owner_mapping as vom on vom.vehicle_id=vm.id " +
                "where vm.id not in(select distinct vehicle_id from rdvts_oltp.vehicle_device_mapping "+
                " where is_active=true) and is_contractor=true ";
        if(userIdList!=null && userIdList.size()>0){
            qry+="or vom.user_id in(:userIdList)";
            sqlParam.addValue("userIdList",userIdList);
        }
        else{
            qry+="and vom.contractor_id =:contractorId";
            sqlParam.addValue("contractorId",userId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }

    @Override
    public List<VehicleMasterDto> getVehicleById(Integer id, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<VehicleMasterDto> vehicle;
        String qry = "SELECT ve.id, ve.vehicle_no, ve.vehicle_type_id, ve.model, ve.speed_limit, ve.chassis_no, ve.engine_no, ve.engine_no, ve.is_active, ve.is_active, ve.created_by, " +
                "ve.created_on, ve.updated_by, ve.updated_on " +
                "FROM rdvts_oltp.vehicle_m AS ve WHERE ve.is_active = true";

        if(id>0) {
            qry += " AND ve.id=:id";
        }
        sqlParam.addValue("id", id);
        sqlParam.addValue("userId", userId);
        try {
            vehicle = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        return vehicle;
    }

}
