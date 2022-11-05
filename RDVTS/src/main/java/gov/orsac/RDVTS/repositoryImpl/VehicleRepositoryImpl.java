package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
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
                "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on ," +
                "owner.user_id as userId,concat(userM.first_name,' ',userM.middle_name,' ',userM.last_name) as ownerName," +
                "owner.contractor_id as contractorId,contractor.name as contractorName " +
                "FROM rdvts_oltp.vehicle_m as vm " +
                "left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id " +
                "left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vm.id " +
                "left join rdvts_oltp.user_m as userM on  userM.id=owner.user_id " +
                "left join rdvts_oltp.contractor_m as contractor on contractor.id=owner.contractor_id ";
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
        String qry = "SELECT vd.id, vd.vehicle_id, vd.device_id, vd.installation_date,vd.installed_by as installedBy,device.imei_no_1 as imeiNo1,device.sim_icc_id_1 as simIccId1," +
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
        String qry = "SELECT vd.id, vd.vehicle_id, vd.device_id, vd.installation_date,vd.installed_by as installedBy,device.imei_no_1 as imeiNo1,device.sim_icc_id_1 as simIccId1," +
                "device.mobile_number_1 as mobileNumber1,device.imei_no_2 as imeiNo2,device.sim_icc_id_2 as simIccId2," +
                "device.mobile_number_2 as mobileNumber2,device.model_name,device.device_no as deviceNo,vd.deactivation_date as deactivationDate " +
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

    public List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId, Date vehicleWorkStartDate, Date vehicleWorkEndDate,Integer userId) throws ParseException {
        List<VehicleDeviceMappingDto> vehicleDevice = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select id, vehicle_id, device_id, installation_date, installed_by, is_active, created_by, created_on, updated_by, updated_on, deactivation_date " +
                "FROM rdvts_oltp.vehicle_device_mapping where is_active=true ";


        if (vehicleId!=null && vehicleId > 0) {
            qry += " and vehicle_id =:vehicleId ";
            sqlParam.addValue("vehicleId", vehicleId);
        }

        if (vehicleWorkStartDate != null && vehicleWorkEndDate == null) {
            qry += "  and created_on BETWEEN :vehicleWorkStartDate AND now() or deactivation_date BETWEEN :vehicleWorkStartDate AND now() ";
            sqlParam.addValue("vehicleWorkStartDate", vehicleWorkStartDate);

        }
        if(vehicleWorkStartDate != null && vehicleWorkEndDate != null){
            qry += "  and created_on BETWEEN :vehicleWorkStartDate AND :vehicleWorkEndDate or deactivation_date BETWEEN :vehicleWorkStartDate AND :vehicleWorkEndDate ";
            sqlParam.addValue("vehicleWorkStartDate", vehicleWorkStartDate);
            sqlParam.addValue("vehicleWorkEndDate", vehicleWorkEndDate);
        }


        UserInfoDto user=userRepositoryImpl.getUserByUserId(userId);
        if(user.getUserLevelId()==5){
            if(qry.length()<=0) {
                qry += " WHERE  owner.contractor_id=:contractorId ";
                sqlParam.addValue("contractorId",userId);
            }
            else{
                qry += " and owner.contractor_id=:contractorId  ";
                sqlParam.addValue("contractorId",userId);
            }
        }
      else if(user.getUserLevelId()==1){
        List<Integer> userIdList= helperServiceImpl.getLowerUserByUserId(userId);
        qry+=" ";
    }
            else if(user.getUserLevelId()==2){
        List<Integer> distId=userRepositoryImpl.getDistIdByUserId(userId);
        List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(distId);
        List<Integer> vehicleIds  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
        if(qry.length()<=0) {
            qry += " WHERE  vm.id in(:vehicleIds) ";
            sqlParam.addValue("vehicleIds",vehicleIds);;
        }
        else{
            qry += " and vm.id in(:vehicleIds)  ";
            sqlParam.addValue("vehicleIds",vehicleIds);;
        }
    }
        else if(user.getUserLevelId()==3){
        List<Integer> blockId=userRepositoryImpl.getBlockIdByUserId(userId);
        List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByBlockList(blockId);
        List<Integer> vehicleIds  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
        if(qry.length()<=0) {
            qry += " WHERE  vm.id in(:vehicleIds) ";
            sqlParam.addValue("vehicleIds",vehicleIds);;
        }
        else{
            qry += " and vm.id in(:vehicleIds) ";
            sqlParam.addValue("vehicleIds",vehicleIds);;
        }
    }
        else if(user.getUserLevelId()==4){
        List<Integer> divisionId=userRepositoryImpl.getDivisionByUserId(userId);
        List<Integer> districtId=userRepositoryImpl.getDistrictByDivisionId(divisionId);
        List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(districtId);
        List<Integer> vehicleIds  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
        if(qry.length()<=0) {
            qry += " WHERE  vm.id in(:vehicleIds) ";
            sqlParam.addValue("vehicleIds",vehicleIds);;
        }
        else{
            qry += " and vm.id in(:vehicleIds) ";
            sqlParam.addValue("vehicleIds",vehicleIds);;
        }
    }






        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
    }

    @Override
    public VehicleWorkMappingDto getVehicleWorkMapping(Integer activityId) {
        List<VehicleWorkMappingDto> vehicleWork = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select work.id as workId,work.g_work_id as gWorkId,work.g_work_name as workName," +
                "work.completion_date as completionDate,work.work_status as workStatusId,status.name as status," +
                "work.approval_status as approvalStatusId, work.pmis_finalize_date as pmisFinalizeDate,work.award_date as awardDate,approvalStatus.name as approvalStatus from rdvts_oltp.work_m as work " +
                "left join rdvts_oltp.activity_m as activity on activity.work_id=work.id " +
                "left join rdvts_oltp.work_status_m as status on status.id=work.work_status " +
                "left join rdvts_oltp.approval_status_m as approvalStatus on approvalStatus.id=work.approval_status " +
                "where activity.id=:activityId and activity.is_active=true ";

        sqlParam.addValue("activityId", activityId);

        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));
    }

    public List<VehicleWorkMappingDto> getVehicleWorkMappingList(List<Integer> activityIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select work.id as workId,work.g_work_id as gWorkId,work.g_work_name as workName," +
                " work.completion_date as completionDate,work.work_status as workStatusId,status.name as status," +
                " work.approval_status as approvalStatusId, work.pmis_finalize_date as pmisFinalizeDate,work.award_date as awardDate,approvalStatus.name as approvalStatus from rdvts_oltp.work_m as work " +
                " left join rdvts_oltp.activity_m as activity on activity.work_id=work.id " +
                " left join rdvts_oltp.work_status_m as status on status.id=work.work_status " +
                " left join rdvts_oltp.approval_status_m as approvalStatus on approvalStatus.id=work.approval_status " +
                " where activity.id in (:activityIds) and activity.is_active=true  ";
        sqlParam.addValue("activityIds", activityIds);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));
    }

    @Override
    public Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle, List<Integer> distIds, List<Integer> divisionIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        PageRequest pageable = null;
//        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
//        pageable = PageRequest.of(vehicle.getDraw() - 1, vehicle.getLimit(), Sort.Direction.fromString("desc"), "id");

        int pageNo = vehicle.getOffSet()/vehicle.getLimit();
        PageRequest pageable = PageRequest.of(pageNo, vehicle.getLimit(), Sort.Direction.fromString("asc"), "id");
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");

        int resultCount = 0;
        String qry = "select * from (SELECT distinct vm.id, vm.vehicle_no, vm.vehicle_type_id as vehicleTypeId,vt.name as vehicleTypeName,vm.model, vm.chassis_no," +
                "vm.engine_no,vm.is_active as active,device.device_id as deviceId, vm.created_by,vm.created_on,vm.updated_by,vm.updated_on ,dam.dist_id, dam.division_id,   " +
                "owner.user_id as userId,concat(userM.first_name,' ',userM.middle_name,' ',userM.last_name) as ownerName,owner.contractor_id as contractorId," +
                "contractor.name as contractorName,am.id  as activityId," +
                "case when vdCount.vehicleCount>0 then true else false end as deviceAssigned," +
                "case when vtuLocation.imeiCount>0 then true else false end as trackingStatus," +
                "case when actCount.activityCount>0 then true else false end as activityAssigned " +
                "FROM rdvts_oltp.vehicle_m as vm " +
                "left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id  " +
                "left join rdvts_oltp.vehicle_device_mapping as device on device.vehicle_id=vm.id and device.is_active=true " +
                "left join rdvts_oltp.device_area_mapping as dam on dam.device_id = device.device_id and dam.is_active =true  "  +
                "left join rdvts_oltp.vehicle_activity_mapping as activity on vm.id = activity.vehicle_id and activity.is_active=true " +
                "left join rdvts_oltp.activity_m as am on am.id = activity.activity_id  " +
                "left join rdvts_oltp.work_m as work on work.id = am.work_id  " +
                "left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vm.id " +
                "left join rdvts_oltp.user_m as userM on  userM.id=owner.user_id " +
                "left join rdvts_oltp.contractor_m as contractor on contractor.id=owner.contractor_id " +
                "left join rdvts_oltp.device_m as dm on dm.id=device.device_id " +
                "left join (select count(id) over (partition by vehicle_id) as vehicleCount,vehicle_id from  rdvts_oltp.vehicle_device_mapping " +
                " where is_active=true and deactivation_date is null) as vdCount on vdCount.vehicle_id=device.vehicle_id " +
                "left join (select count(id) over (partition by imei) as imeiCount,imei from rdvts_oltp.vtu_location where date(date_time)=date(now())) as vtuLocation " +
                "on vtuLocation.imei=dm.imei_no_1 " +
                "left join (select count(id) over (partition by vehicle_id) as activityCount,vehicle_id from rdvts_oltp.vehicle_activity_mapping where is_active=true) as actCount " +
                "on actCount.vehicle_id=activity.vehicle_id) as vehicleList ";
        String subQuery = "";
        if(vehicle.getDeviceAssign()!=null){
            if(subQuery.length()<=0){
                subQuery+=" WHERE deviceAssigned=:deviceAssigned ";
                sqlParam.addValue("deviceAssigned",vehicle.getDeviceAssign());
            }
            else{
                subQuery+=" and deviceAssigned=:deviceAssigned ";
                sqlParam.addValue("deviceAssigned",vehicle.getDeviceAssign());
            }

        }
        if(vehicle.getTrackingAssign()!=null){
            if(subQuery.length()<=0){
                subQuery+=" WHERE trackingStatus=:trackingStatus ";
                sqlParam.addValue("trackingStatus",vehicle.getTrackingAssign());
            }
            else{
                subQuery+=" and trackingStatus=:trackingStatus ";
                sqlParam.addValue("trackingStatus",vehicle.getTrackingAssign());
            }

        }
        if(vehicle.getActivityAssign()!=null){
            if(subQuery.length()<=0){
                subQuery+=" WHERE activityAssigned=:activityAssigned ";
                sqlParam.addValue("activityAssigned",vehicle.getActivityAssign());
            }
            else{
                subQuery+=" and activityAssigned=:activityAssigned ";
                sqlParam.addValue("activityAssigned",vehicle.getActivityAssign());
            }

        }

        if (vehicle.getVehicleTypeId() > 0) {
            if(subQuery.length()<=0) {
                subQuery += " WHERE  vehicleTypeId=:vehicleTypeId ";
                sqlParam.addValue("vehicleTypeId", vehicle.getVehicleTypeId());
            }
            else{
                subQuery += " and  vehicleTypeId=:vehicleTypeId ";
                sqlParam.addValue("vehicleTypeId", vehicle.getVehicleTypeId());
            }
        }
        if (vehicle.getDeviceId() > 0) {
            if(subQuery.length()<=0) {
                subQuery += " WHERE deviceId=:deviceId ";
                sqlParam.addValue("deviceId", vehicle.getDeviceId());
            }
            else{
                subQuery += " and deviceId=:deviceId ";
                sqlParam.addValue("deviceId", vehicle.getDeviceId());
            }
        }
/*
        if (vehicle.getWorkId() > 0) {
            if(subQuery.length()<=0) {
                subQuery += " WHERE work.id=:workId ";
                sqlParam.addValue("workId", vehicle.getWorkId());
            }
            else{
                subQuery += " and work.id=:workId ";
                sqlParam.addValue("workId", vehicle.getWorkId());
            }
        }
*/

        if (vehicle.getActivityId() > 0) {
            if(subQuery.length()<=0) {
                subQuery += " WHERE activityId=:activityId ";
                sqlParam.addValue("activityId", vehicle.getActivityId());
            }
            else{
                subQuery += " and activityId=:activityId ";
                sqlParam.addValue("activityId", vehicle.getActivityId());
            }
        }


        if (vehicle.getDistId() > 0) {
            if(subQuery.length()<=0) {
                subQuery += " WHERE vehicleList.dist_Id IN (:distIds) ";
                sqlParam.addValue("distIds", vehicle.getDistId());
            }
            else{
                subQuery += " and vehicleList.dist_Id IN (:distIds)";
                sqlParam.addValue("distIds", vehicle.getDistId());
            }
        }

        if (vehicle.getDivisionId() > 0) {
            if(subQuery.length()<=0) {
                subQuery += " WHERE vehicleList.division_id IN (:divisionIds) ";
                sqlParam.addValue("divisionIds", vehicle.getDivisionId());
            }
            else{
                subQuery += " and vehicleList.division_id IN (:divisionIds)";
                sqlParam.addValue("divisionIds", vehicle.getDivisionId());
            }
        }

        //Validation on basis of userLevel and lower level user
   /*     UserInfoDto user=userRepositoryImpl.getUserByUserId(vehicle.getUserId());
        if(user.getUserLevelId()==5){
            if(subQuery.length()<=0) {
                subQuery += " WHERE  owner.contractor_id=:contractorId ";
                sqlParam.addValue("contractorId",vehicle.getUserId());
            }
            else{
                subQuery += " and owner.contractor_id=:contractorId  ";
                sqlParam.addValue("contractorId",vehicle.getUserId());
            }
        }
       *//* else if(user.getUserLevelId()==1){
             List<Integer> userIdList= helperServiceImpl.getLowerUserByUserId(vehicle.getUserId());
           qry+=" ";
        }*//*
        else if(user.getUserLevelId()==2){
            List<Integer> distId=userRepositoryImpl.getDistIdByUserId(vehicle.getUserId());
             List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(distId);
             List<Integer> vehicleId  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
            if(subQuery.length()<=0) {
                subQuery += " WHERE  vm.id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds",vehicleId);;
            }
            else{
                subQuery += " and vm.id in(:vehicleIds)  ";
                sqlParam.addValue("vehicleIds",vehicleId);;
            }
        }
        else if(user.getUserLevelId()==3){
            List<Integer> blockId=userRepositoryImpl.getBlockIdByUserId(vehicle.getUserId());
            List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByBlockList(blockId);
            List<Integer> vehicleId  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
            if(subQuery.length()<=0) {
                subQuery += " WHERE  vm.id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds",vehicleId);;
            }
            else{
                subQuery += " and vm.id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds",vehicleId);;
            }
        }
        else if(user.getUserLevelId()==4){
            List<Integer> divisionId=userRepositoryImpl.getDivisionByUserId(vehicle.getUserId());
            List<Integer> districtId=userRepositoryImpl.getDistrictByDivisionId(divisionId);
            List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(districtId);
            List<Integer> vehicleId  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
            if(subQuery.length()<=0) {
                subQuery += " WHERE  vm.id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds",vehicleId);;
            }
            else{
                subQuery += " and vm.id in(:vehicleIds) ";
                sqlParam.addValue("vehicleIds",vehicleId);;
            }
        }*/
        String finalQry=qry+" "+subQuery;
        resultCount = count(finalQry, sqlParam);
        if (vehicle.getLimit() > 0) {
            finalQry += " LIMIT " + vehicle.getLimit() + " OFFSET " + vehicle.getOffSet();
        }
//        resultCount = count(qry, sqlParam);
        List<VehicleMasterDto> list = namedJdbc.query(finalQry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        return new PageImpl<>(list, pageable, resultCount);
    }

    @Override
    public List<VehicleTypeDto> getVehicleTypeList() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id, name FROM rdvts_oltp.vehicle_type where is_active=true order by name asc ";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleTypeDto.class));
    }

    @Override
    public List<VehicleMasterDto> getUnAssignedVehicleData(List<Integer> userIdList, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT vm.id,vm.vehicle_no,vm.vehicle_type_id,vm.model,vm.speed_limit,vm.chassis_no,vm.engine_no,vm.is_active,vm.created_by,vm.created_on,  " +
                "vm.updated_by,vm.updated_on,  " +
                "type.name as vehicleTypeName from rdvts_oltp.vehicle_m as vm   " +
                "LEFT join rdvts_oltp.vehicle_owner_mapping as vom on vom.vehicle_id=vm.id   " +
                "left join rdvts_oltp.vehicle_type as type on type.id =vm.vehicle_type_id  " +
                "where vm.id not in(select distinct vehicle_id from rdvts_oltp.vehicle_device_mapping  " +
                " where is_active=true) ";
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
        List<VehicleMasterDto> vehicle=new ArrayList<>();
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
                "deactivation_date is null ";
        sqlParam.addValue("vehicleId",vehicleId);
            count = namedJdbc.queryForObject(qry, sqlParam, Integer.class);

       if(count>0){
           device=true;
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
        try {
            count = namedJdbc.queryForObject(qry, sqlParam, Integer.class);
            return true;
        } catch (Exception e) {
            if (count > 0) {
                work = true;
            }
            return work;
        }
    }

    public boolean getTrackingLiveOrNot(Long imeiNo) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer count = 0;
        boolean tracking = false;
        String qry = "select count(id) from rdvts_oltp.vtu_location where imei=:imeiNo and date(date_time)=date(now())";
        sqlParam.addValue("imeiNo", imeiNo);
            count = namedJdbc.queryForObject(qry, sqlParam, Integer.class);
            if(count>0){
                tracking=true;
            }
      return tracking;
    }
    public boolean getActivityAssignOrNot(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer count = 0;
        boolean activity = false;
            String qry = "select count(id) from rdvts_oltp.vehicle_activity_mapping where vehicle_id=:vehicleId and is_active=true";
        sqlParam.addValue("vehicleId", vehicleId);
        count = namedJdbc.queryForObject(qry, sqlParam, Integer.class);
        if(count>0){
            activity=true;
        }
        return activity;
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

    @Override
    public Integer deactivateVehicleActivity(List<Integer> activityIds, List<Integer> vehicleIds) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String currentDateTime = dateFormat.format(new Date());
        Date date = dateFormat.parse(currentDateTime);
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        int resultCount = 0;

        String qry = "UPDATE rdvts_oltp.vehicle_activity_mapping  " +
                "SET is_active=false,deactivation_date=now()  WHERE activity_id IN (:activityIds) or vehicle_id IN (:vehicleIds) " ;
        sqlParam.addValue("activityIds", activityIds);
        sqlParam.addValue("vehicleIds", vehicleIds);
        return namedJdbc.update(qry,sqlParam);
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
    public Integer getvehicleCountByWorkId(Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT  count( vam.vehicle_id) FROM rdvts_oltp.vehicle_activity_mapping as vam " +
                " LEFT JOIN rdvts_oltp.vehicle_m as vm on vm.id=vam.vehicle_id " +
                " left join rdvts_oltp.activity_m as am on am.id=vam.activity_id " +
                " WHERE 1=1 and am.is_active=true and vm.is_active=true and vam.is_active=true ";
        if (id > 0) {
            qry += " and am.work_id=:workId";
            sqlParam.addValue("workId", id);
        }
        return   namedJdbc.queryForObject(qry, sqlParam, Integer.class);


    }


    public List<VehicleMasterDto> getVehicleHistoryList(int id){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicle_type_name,vm.model,vm.speed_limit,vm.chassis_no,vm.engine_no,vm.is_active as active, " +
                "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on  from rdvts_oltp.vehicle_m as vm " +
                " left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id = vm.id " +
                "left join rdvts_oltp.activity_m as act on vam.activity_id = act.id " +
                "left join rdvts_oltp.vehicle_type as vt on vt.id = vm.vehicle_type_id " +
                "where vm.is_active = true and vam.is_active = true and act.is_active = true and vt.is_active= true  " ;
        if (id > 0){
            qry +=" and act.work_id = :id " ;
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }

    public List<RoadMasterDto> getRoadArray(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="select gcm.*,gm.work_id from rdvts_oltp.geo_construction_m as gcm " +
                "left join rdvts_oltp.geo_master as gm on gm.road_id = gcm.id and gm.is_active = true " +
                "where gcm.is_active = true ";
        if (id > 0){
            qry +=" and gm.work_id = :id " ;
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
    }

    public List<VehicleMasterDto> getVehicleByVehicleTypeId(Integer vehicleTypeId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT vm.id, vm.vehicle_no, vm.vehicle_type_id as vehicleTypeId, vm.chassis_no, vm.engine_no " +
                "FROM rdvts_oltp.vehicle_m as vm " +
                "LEFT JOIN rdvts_oltp.vehicle_type as vt on vt.id= vm.vehicle_type_id " +
                "WHERE vm.is_active=true and vm.vehicle_type_id=:vehicleTypeId ";
        sqlParam.addValue("vehicleTypeId", vehicleTypeId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }

    public List<RoadMasterDto> getRoadDetailByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="SELECT road.id, road.package_id, road.package_name, road.road_name, road.road_length, road.road_location, " +
                "road.road_allignment, ST_AsGeoJSON(road.geom) as geom, ST_AsGeoJSON(road.geom) as geoJSON, road.road_width, road.g_road_id, road.is_active, road.created_by, " +
                "road.created_on, road.updated_by, road.updated_on, road.completed_road_length, road.sanction_date, road.road_code, road.road_status, " +
                "road.approval_status, road.approved_by, gm.work_id as workIds, am.id as activityId, vm.id as vehicleId  " +
                "FROM rdvts_oltp.geo_construction_m as road " +
                "LEFT JOIN rdvts_oltp.geo_master as gm on gm.road_id=road.id " +
                "LEFT JOIN rdvts_oltp.work_m as wm on wm.id=gm.work_id " +
                "LEFT JOIN rdvts_oltp.activity_m as am on am.work_id=wm.id " +
                "LEFT JOIN rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id=am.id " +
                "LEFT JOIN rdvts_oltp.vehicle_m as vm on vm.id=vam.vehicle_id where vm.id=:vehicleId";
        sqlParam.addValue("vehicleId", vehicleId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
    }

    public List<Integer> getVehicleIdsByActivityId(List<Integer> activityList){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select vm.id as vehicle_id from rdvts_oltp.vehicle_m as vm " +
                "join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id =vm.id " +
                "where vm.is_active = true and vam.activity_id IN (:activityList) ";
        sqlParam.addValue("activityList", activityList);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>());
    }

    public List<Integer> getDeviceIdsByVehicleIds(List<Integer> vehicleIds){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select device_id from rdvts_oltp.vehicle_device_mapping where is_active = true and vehicle_id IN (:vehicleIds) ";
        sqlParam.addValue("vehicleIds", vehicleIds);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>());
    }

    public List<String> getImeiByDeviceId(List<Integer> deviceIds){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select imei_no_1 from rdvts_oltp.device_m where is_active = true and id IN (:deviceIds) ";
        sqlParam.addValue("deviceIds", deviceIds);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>());
    }
    public Integer getActivityByVehicleId(Integer vehicleId){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct activity_id from rdvts_oltp.vehicle_activity_mapping  where is_active=true and vehicle_id=:vehicleId";
        sqlParam.addValue("vehicleId", vehicleId);
        try{
          return  namedJdbc.queryForObject(qry, sqlParam, Integer.class);
        }
        catch(Exception e){
            return null;
        }
    }
    public List<Integer> getActivityIdsByVehicleId(Integer vehicleId){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct activity_id from rdvts_oltp.vehicle_activity_mapping  where is_active=false and vehicle_id=:vehicleId";
        sqlParam.addValue("vehicleId", vehicleId);
        return  namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }



    public ActivityInfoDto getLiveActivityByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT am.id,am.activity_name as activityName,am.activity_quantity as activityQuantity,am.activity_start_date as startDate,am.activity_completion_date as activityCompletionDate,am.actual_activity_start_date as actualActivityStartDate,   " +
                "am.actual_activity_completion_date as actualActivityCompletionDate,am.executed_quantity as executedQuantity,am.activity_status as statusId,status.name as statusName from rdvts_oltp.activity_m as am   " +
                "left join rdvts_oltp.activity_status_m as status on status.id=am.activity_status  " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id = am.id  "  +
                "WHERE vam.is_active = true  ";

        if(vehicleId>0){
            qry+=" AND vam.vehicle_id=:vehicleId";
        }
        sqlParam.addValue("vehicleId", vehicleId);
        try{
         return   namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityInfoDto.class));
        }
        catch(Exception e){
            return null;
        }
    }
    public List<ActivityInfoDto> getActivityListByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT am.id,am.activity_name as activityName,am.activity_quantity as activityQuantity,am.activity_start_date as startDate,"+
                "am.activity_completion_date as activityCompletionDate,am.actual_activity_start_date as actualActivityStartDate," +
                " am.actual_activity_completion_date as actualActivityCompletionDate,am.executed_quantity as executedQuantity,"+
                "am.activity_status as statusId,status.name as statusName from rdvts_oltp.activity_m as am   " +
                "left join rdvts_oltp.activity_status_m as status on status.id=am.activity_status  " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id = am.id  " +
                "WHERE vam.is_active =false  ";

        if(vehicleId>0){
            qry+=" AND vam.vehicle_id=:vehicleId";
        }
        sqlParam.addValue("vehicleId", vehicleId);
            return   namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityInfoDto.class));
    }



    public Boolean deactivateVehicle(Integer vehicleId, Integer status) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " ";
        if (status == 0) {
            qry = "UPDATE rdvts_oltp.vehicle_m  " +
                    "SET is_active=false WHERE id=:vehicleId  ";
            sqlParam.addValue("vehicleId", vehicleId);
        } else {
            qry = "UPDATE rdvts_oltp.vehicle_m  " +
                    "SET is_active=true WHERE id=:vehicleId  ";
            sqlParam.addValue("vehicleId", vehicleId);
        }

        int update = namedJdbc.update(qry, sqlParam);
        boolean result = false;
        if (update > 0) {
            result = true;
        }
        return result;
    }


    public Boolean deactivateDeviceVehicleMapping(Integer vehicleId, Integer status) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " ";
        if (status == 0) {
            qry = "UPDATE rdvts_oltp.vehicle_device_mapping SET is_active = false WHERE vehicle_id=:vehicleId";
            sqlParam.addValue("vehicleId", vehicleId);
//        } else {
//            qry = "UPDATE rdvts_oltp.vehicle_device_mapping SET is_active = true   " +
//                    "WHERE id in(select id from rdvts_oltp.vehicle_device_mapping where vehicle_id=:vehicle_id order by id desc limit 1)   ";
//            sqlParam.addValue("vehicleId", vehicleId);
        }

            int update = namedJdbc.update(qry, sqlParam);
            boolean result = false;
            if (update > 0) {
                result = true;
            }
            return result;
        }

    public Boolean deactivateVehicleActivityMapping(Integer vehicleId, Integer status) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " ";
        if (status == 0) {
            qry = "UPDATE rdvts_oltp.vehicle_activity_mapping SET is_active = false WHERE vehicle_id=:vehicleId  ";
            sqlParam.addValue("vehicleId", vehicleId);
//        } else {
//            qry = "UPDATE rdvts_oltp.vehicle_activity_mapping SET is_active = true   " +
//                    "WHERE id in(select id from rdvts_oltp.vehicle_activity_mapping where vehicle_id=:vehicle_id order by id desc limit 1)   ";
//            sqlParam.addValue("vehicleId", vehicleId);
        }

        int update = namedJdbc.update(qry, sqlParam);
        boolean result = false;
        if (update > 0) {
            result = true;
        }
        return result;
    }

    public Integer getTotalCount(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT count(id) from rdvts_oltp.activity_m WHERE id IN(select activity_id from rdvts_oltp.vehicle_activity_mapping where vehicle_id=:vehicleId  and is_active=true) AND activity_status != 2   " +
                     "AND is_active=true  ";
       sqlParam.addValue("vehicleId",vehicleId);
        return  namedJdbc.queryForObject(qry, sqlParam,Integer.class);
    }

    public List<Integer> getDistIdByDeviceId(List<Integer> deviceIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT distinct dist_id from rdvts_oltp.device_area_mapping  " +
                     "WHERE is_active = true and device_id=:deviceIds ";
        sqlParam.addValue("deviceIds",deviceIds);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }
    public List<Integer> getDistIds() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT dist_id from rdvts_oltp.device_area_mapping where is_active = true ";
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public List<Integer> getDivisionIds() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT division_id from rdvts_oltp.device_area_mapping where is_active = true  ";
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }
}

