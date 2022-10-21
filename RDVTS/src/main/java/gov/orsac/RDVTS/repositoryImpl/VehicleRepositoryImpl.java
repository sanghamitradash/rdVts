package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.entities.VehicleWorkMappingEntity;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class VehicleRepositoryImpl implements VehicleRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private HelperServiceImpl helperServiceImpl;
    @Autowired
    private MasterRepositoryImpl masterRepositoryImpl;

    @Autowired
    private GeoMasterRepositoryImpl geoMasterRepositoryImpl;
    @Autowired
    private ContractorRepositoryImpl contractorRepositoryImpl;


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
        String qry = "SELECT vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicleTypeName,vm.model,vm.speed_limit," +
                "vm.chassis_no,vm.engine_no,vm.is_active as active," +
                "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on " +
                "FROM rdvts_oltp.vehicle_m as vm left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id ";
        if (vehicleId > 0) {
            qry += " where vm.id=:vehicleId";
        }
        sqlParam.addValue("vehicleId", vehicleId);
        try {
            vehicle = namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return vehicle;
    }

    @Override
    public VehicleDeviceInfo getVehicleDeviceMapping(Integer vehicleId) {
        VehicleDeviceInfo vehicleDevice = new VehicleDeviceInfo();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT vd.id, vd.vehicle_id, vd.device_id, vd.installation_date,vd.installed_by,device.imei_no_1 as imeiNo1,device.sim_icc_id_1 as simIccId1," +
                "device.mobile_number_1 as mobileNumber1,device.imei_no_2 as imeiNo2,device.sim_icc_id_2 as simIccId2," +
                "device.mobile_number_2 as mobileNumber2,device.model_name,device.device_no as deviceNo " +
                "FROM rdvts_oltp.vehicle_device_mapping as vd " +
                "left join rdvts_oltp.device_m as device on vd.device_id=device.id where vehicle_id=:vehicleId and vd.is_active=true  ";

        sqlParam.addValue("vehicleId", vehicleId);
        try {
            vehicleDevice = namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceInfo.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return vehicleDevice;
    }

    public List<VehicleDeviceInfo> getVehicleDeviceMappingAssignedList(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT vd.id, vd.vehicle_id, vd.device_id, vd.installation_date,vd.installed_by,device.imei_no_1 as imeiNo1,device.sim_icc_id_1 as simIccId1," +
                "device.mobile_number_1 as mobileNumber1,device.imei_no_2 as imeiNo2,device.sim_icc_id_2 as simIccId2," +
                "device.mobile_number_2 as mobileNumber2,device.model_name,device.device_no as deviceNo " +
                "FROM rdvts_oltp.vehicle_device_mapping as vd " +
                "left join rdvts_oltp.device_m as device on vd.device_id=device.id where vehicle_id=:vehicleId and vd.is_active=false  ";

        sqlParam.addValue("vehicleId", vehicleId);

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceInfo.class));

    }

    @Override
    public List<VehicleDeviceMappingDto> getVehicleDeviceMappingList(List<Integer> vehicleId) {
        List<VehicleDeviceMappingDto> vehicleDevice = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id, vehicle_id, device_id, installation_date, installed_by, is_active, created_by, created_on, updated_by, updated_on " +
                "FROM rdvts_oltp.vehicle_device_mapping where vehicle_id IN (:vehicleId) ";

        sqlParam.addValue("vehicleId", vehicleId);
        try {
            vehicleDevice = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return vehicleDevice;
    }

    public List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId, Date vehicleWorkStartDate, Date vehicleWorkEndDate) {
        List<VehicleDeviceMappingDto> vehicleDevice = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select id, vehicle_id, device_id, installation_date, installed_by, is_active, created_by, created_on, updated_by, updated_on, deactivation_date " +
                "FROM rdvts_oltp.vehicle_device_mapping where is_active=true ";


        if (vehicleId > 0) {
            qry += " and vehicle_id =:vehicleId ";
            sqlParam.addValue("vehicleId", vehicleId);
        }

        if (vehicleWorkEndDate == null) {
            vehicleWorkEndDate = new Date();
        }
        if (vehicleWorkStartDate != null && vehicleWorkEndDate != null) {
            qry += "  and deactivation_date BETWEEN :vehicleWorkStartDate AND :vehicleWorkEndDate ";
            sqlParam.addValue("vehicleWorkStartDate", vehicleWorkStartDate);
            sqlParam.addValue("vehicleWorkEndDate", vehicleWorkEndDate);
        }

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
    }

    @Override
    public List<VehicleWorkMappingDto> getVehicleWorkMapping(Integer vehicleId) {
        List<VehicleWorkMappingDto> vehicleWork = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT workM.id,workM.vehicle_id,workM.work_id,workM.start_time,workM.end_time,workM.start_date," +
                "workM.end_date,work.g_work_name as workName,workM.is_active as active " +
                "FROM rdvts_oltp.vehicle_work_mapping  as workM " +
                "left join rdvts_oltp.work_m as work on work.id=workM.work_id where vehicle_id=:vehicleId and workM.is_active=true ";

        sqlParam.addValue("vehicleId", vehicleId);

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));
    }

    public List<VehicleWorkMappingDto> getVehicleWorkMappingList(Integer vehicleId) {
        List<VehicleWorkMappingDto> vehicleWork = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT workM.id,workM.vehicle_id,workM.work_id,workM.start_time,workM.end_time,workM.start_date," +
                " workM.end_date,work.g_work_name as workName,workM.is_active as active " +
                " FROM rdvts_oltp.vehicle_work_mapping  as workM " +
                " left join rdvts_oltp.work_m as work on work.id=workM.work_id where vehicle_id=:vehicleId and workM.is_active=false ";

        sqlParam.addValue("vehicleId", vehicleId);

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));
    }

    @Override
    public Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        pageable = PageRequest.of(vehicle.getDraw() - 1, vehicle.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        int resultCount = 0;
        String qry = "SELECT distinct vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicleTypeName,vm.model, vm.chassis_no,  " +
                "vm.engine_no,vm.is_active as active,device.device_id as deviceId, vm.created_by,vm.created_on,vm.updated_by,vm.updated_on ,  " +
                "concat(userM.first_name,' ',userM.middle_name,' ',userM.last_name) as ownerName " +
                "FROM rdvts_oltp.vehicle_m as vm left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id  " +
                "left join rdvts_oltp.vehicle_device_mapping as device on device.vehicle_id=vm.id  " +
                "left join rdvts_oltp.vehicle_activity_mapping as activity on vm.id = activity.vehicle_id  " +
                "left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vm.id  " +
                "left join rdvts_oltp.user_m as userM on  userM.id=owner.user_id where vm.is_active=true ";

        if (vehicle.getVehicleTypeId() > 0) {
            qry += " and vm.vehicle_type_id=:vehicleTypeId ";
            sqlParam.addValue("vehicleTypeId", vehicle.getVehicleTypeId());
        }
        if (vehicle.getDeviceId() > 0) {
            qry += " and device.device_id=:deviceId ";
            sqlParam.addValue("deviceId", vehicle.getDeviceId());
        }
      /*  if (vehicle.getWorkId() > 0) {
            qry += " and work.work_id=:workId ";
            sqlParam.addValue("workId", vehicle.getWorkId());
        }*/

        if (vehicle.getActivityId() > 0) {
            qry += " and activity.activity_id=:activityId ";
            sqlParam.addValue("activityId", vehicle.getActivityId());
        }

        //Validation on basis of userLevel and lower level user
      /*  UserInfoDto user=userRepositoryImpl.getUserByUserId(vehicle.getUserId());
        if(user.getUserLevelId()==5){
            qry+="and owner.contractor_id=:contractorId ";
            sqlParam.addValue("contractorId",vehicle.getUserId());
        }
       *//* else if(user.getUserLevelId()==1){
         *//**//*    List<Integer> userIdList= helperServiceImpl.getLowerUserByUserId(vehicle.getUserId());*//**//*
           qry+=" ";
        }*//*
        else if(user.getUserLevelId()==2){
            List<Integer> distId=userRepositoryImpl.getDistIdByUserId(vehicle.getUserId());
             List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(distId);
             List<Integer> vehicleId  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
             qry+=" and vm.id in(:vehicleIds)";
             sqlParam.addValue("vehicleIds",vehicle);
        }
        else if(user.getUserLevelId()==3){
            List<Integer> blockId=userRepositoryImpl.getBlockIdByUserId(vehicle.getUserId());
            List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByBlockList(blockId);
            List<Integer> vehicleId  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
            qry+=" and vm.id in(:vehicleIds)";
            sqlParam.addValue("vehicleIds",vehicleId);
        }
        else if(user.getUserLevelId()==4){
            List<Integer> divisionId=userRepositoryImpl.getDivisionByUserId(vehicle.getUserId());
            List<Integer> districtId=userRepositoryImpl.getDistrictByDivisionId(divisionId);
            List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(districtId);
            List<Integer> vehicleId  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
            qry+=" and vm.id in(:vehicleIds)";
            sqlParam.addValue("vehicleIds",vehicleId);
        }
*/

        resultCount = count(qry, sqlParam);
        if (vehicle.getLimit() > 0) {
            qry += " LIMIT " + vehicle.getLimit() + " OFFSET " + vehicle.getOffSet();
        }
        List<VehicleMasterDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }

    @Override
    public List<VehicleTypeDto> getVehicleTypeList() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id, name FROM rdvts_oltp.vehicle_type where is_active=true ";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleTypeDto.class));
    }

    @Override
    public List<VehicleMasterDto> getUnAssignedVehicleData(List<Integer> userIdList, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select vm.* from rdvts_oltp.vehicle_m as vm  " +
                " LEFT join rdvts_oltp.vehicle_owner_mapping as vom on vom.vehicle_id=vm.id " +
                "where vm.id not in(select distinct vehicle_id from rdvts_oltp.vehicle_device_mapping " +
                " where is_active=true)  ";
        if (userIdList != null && userIdList.size() > 0) {
            qry += "or vom.user_id in(:userIdList)";
            sqlParam.addValue("userIdList", userIdList);
        } else {
            qry += " and is_contractor=true and vom.contractor_id =:contractorId";
            sqlParam.addValue("contractorId", userId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }

    @Override
    public List<VehicleMasterDto> getVehicleById(Integer id, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<VehicleMasterDto> vehicle;
        String qry = "SELECT ve.id, ve.vehicle_no, ve.vehicle_type_id, vt.name as vehicleTypeName , ve.model, ve.speed_limit, ve.chassis_no, ve.engine_no, ve.engine_no, ve.is_active, ve.is_active, ve.created_by, " +
                "ve.created_on, ve.updated_by, ve.updated_on " +
                "FROM rdvts_oltp.vehicle_m AS ve " +
                "LEFT JOIN rdvts_oltp.vehicle_type AS vt ON vt.id=ve.vehicle_type_id " +
                "WHERE ve.is_active = true";

        if (id > 0) {
            qry += " AND ve.id=:id";
        }
        sqlParam.addValue("id", id);
        sqlParam.addValue("userId", userId);
        try {
            vehicle = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return vehicle;
    }

    public List<Integer> getVehicleByWorkIdList(List<Integer> workId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "select distinct vehicle_id from rdvts_oltp.vehicle_work_mapping where work_id in(:workId)";
        sqlParam.addValue("workId", workId);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public boolean getDeviceAssignedOrNot(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer count = 0;
        boolean device = false;
        String qry = "select count(id)  from  rdvts_oltp.vehicle_device_mapping " +
                "where vehicle_id=:vehicleId and is_active=true and " +
                "deactivation_date is null and created_on <=now()";
        sqlParam.addValue("vehicleId", vehicleId);
        count = namedJdbc.queryForObject(qry, sqlParam, Integer.class);
        if (count > 0) {
            device = true;
        }
        return device;
    }

    public boolean getWorkAssignedOrNot(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer count = 0;
        boolean work = false;
        String qry = "select count(id)  from  rdvts_oltp.vehicle_work_mapping " +
                "where vehicle_id=5 and is_active=true and start_time <=now() " +
                //"deactivation_date is null and start_time <=now()";
        sqlParam.addValue("vehicleId", vehicleId);
        count = namedJdbc.queryForObject(qry, sqlParam, Integer.class);
        if (count > 0) {
            work = true;
        }
        return work;
    }

    public boolean getTrackingLiveOrNot(Long imeiNo) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer count = 0;
        boolean tracking = false;
        String qry = "select * from rdvts_oltp.vtu_location where imei=:imeiNo and date_time=now()";
        sqlParam.addValue("imeiNo", imeiNo);
        count = namedJdbc.queryForObject(qry, sqlParam, Integer.class);
        if (count > 0) {
            tracking = true;
        }
        return tracking;
    }

    @Override
    public Integer deactivateVehicleWork(List<Integer> workIds, List<Integer> vehicleIds) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String currentDateTime = dateFormat.format(new Date());
        Date date = dateFormat.parse(currentDateTime);
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        int resultCount = 0;

        String qry = "UPDATE rdvts_oltp.vehicle_work_mapping " +
                "SET is_active=false,deactivation_date=now()  WHERE work_id IN (:workIds) or vehicle_id IN (:vehicleIds) ";
        sqlParam.addValue("workIds", workIds);
        sqlParam.addValue("vehicleIds", vehicleIds);

        return namedJdbc.update(qry, sqlParam);
    }

    public LocationDto getLatestLocationByDeviceId(Long imeiNo) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        LocationDto locationDto = null;

        String qry = "select  * from rdvts_oltp.vtu_location  where imei=:imeiNo order by date_time desc limit 1 ";
        sqlParam.addValue("imeiNo", imeiNo);
        try {
            locationDto = namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(LocationDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }


        return locationDto;
    }

    public List<AlertDto> getAlertList(Long imeiNo) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select imei,alert_type_id,type.alert_type as alertTypeName,latitude,longitude,altitude,accuracy,speed,gps_dtm," +
                "is_resolve,resolved_by,userM.first_name as resolvedByUser from  rdvts_oltp.alert_data  as alert " +
                "left join rdvts_oltp.alert_type_m as type on type.id=alert.alert_type_id " +
                "left join rdvts_oltp.user_m as userM on userM.id=alert.resolved_by where imei=:imeiNo";
        sqlParam.addValue("imeiNo", imeiNo);


        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class));
    }

    public List<UserInfoDto> getUserDropDownForVehicleOwnerMapping(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select  id,concat(first_name,' ',middle_name,' ',last_name)as userName,email,mobile_1 as mobile1,mobile_2 as mobile2 " +
                "from rdvts_oltp.user_m order by id ";


        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UserInfoDto.class));
    }

    public List<VehicleActivityMappingDto> getVehicleByActivityId(Integer activityId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT vam.id, vam.vehicle_id, vam.activity_id, vam.start_time, vam.end_time, vam.start_date, vam.end_date, vam.is_active, vam.created_by, " +
                "vam.created_on, vam.updated_by, vam.updated_on, vam.deactivation_date, vam.g_activity_id, vm.vehicle_no as vehicleNo, vm.vehicle_type_id as vehicleTypeId, vm.model as model, vm.speed_limit as speedLimit, vm.chassis_no as chassisNo, " +
                "vm.engine_no as engineNo " +
                "FROM rdvts_oltp.vehicle_activity_mapping as vam " +
                "LEFT JOIN rdvts_oltp.vehicle_m as vm on vm.id=vam.vehicle_id WHERE vam.is_active=true";
        if (activityId > 0 && activityId != null) {
            qry += " AND vam.activity_id=:activityId ";
            sqlParam.addValue("activityId", activityId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleActivityMappingDto.class));

    }

    public List<VehicleMasterDto> getVehicleHistoryList(int id){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicleTypeName,vm.model,vm.speed_limit,vm.chassis_no,vm.engine_no,vm.is_active as active, " +
                "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on from rdvts_oltp.activity_m as act " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id= act.id " +
                "left join rdvts_oltp.vehicle_m as vm on vam.vehicle_id = vm.id " +
                "left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id " +
                "where vm.is_active = true " ;
        if (id > 0){
            qry +=" and act.work_id = :id " ;
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }

    public List<RoadMasterDto> getRoadArray(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="select gcm.*,gm.work_id from rdvts_oltp.geo_construction_m as gcm left join rdvts_oltp.geo_master as gm on gm.id = gcm.geo_master_id " +
                "where gcm.is_active = true ";
        if (id > 0){
            qry +=" and gm.work_id = :id " ;
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
    }
}
