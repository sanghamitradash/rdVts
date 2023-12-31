package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.serviceImpl.HelperServiceImpl;
import lombok.extern.slf4j.Slf4j;
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

import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
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

    public List<VehicleDeviceMappingDto> getdeviceListByVehicleId(Integer vehicleId, Date vehicleWorkStartDate, Date vehicleWorkEndDate, Integer userId) throws ParseException {
        List<VehicleDeviceMappingDto> vehicleDevice = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select vdm.*,dm.imei_no_1 as imeiNo1 ,dm.imei_no_2 as imeiNo2 " +
                "FROM rdvts_oltp.vehicle_device_mapping vdm " +
                "left join rdvts_oltp.device_m dm on vdm.device_id=dm.id " +
                "left join rdvts_oltp.vehicle_m vehicleList on vdm.vehicle_id=vehicleList.id " +
                "left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vdm.id and owner.is_active=true " +
                " where vdm.is_active=true and dm.is_active='t' ";


        if (vehicleId != null && vehicleId > 0) {
            qry += " and vdm.vehicle_id =:vehicleId ";
            sqlParam.addValue("vehicleId", vehicleId);
        }


        if (userId != null) {


            UserInfoDto user = userRepositoryImpl.getUserByUserId(userId);
            if (user.getUserLevelId() == 5) {
                if (qry.length() <= 0) {
                    qry += " WHERE  owner.contractor_id=:contractorId ";
                    sqlParam.addValue("contractorId", userId);
                } else {
                    qry += " and owner.contractor_id=:contractorId  ";
                    sqlParam.addValue("contractorId", userId);
                }
            }
//       *//* else if(user.getUserLevelId()==1){
//             List<Integer> userIdList= helperServiceImpl.getLowerUserByUserId(vehicle.getUserId());
//           qry+=" ";
//        }*//*
            else if (user.getUserLevelId() == 2) {
                List<Integer> distId = userRepositoryImpl.getDistIdByUserId(userId);
                List<Integer> contractorId = geoMasterRepositoryImpl.getContractorIdByDistIdList(distId);
                List<Integer> vehicleByContractorIdList = masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
                if (qry != " " && qry.length() <= 0) {
                    if (vehicleId != null && vehicleByContractorIdList.size() > 0) {
                        qry += " WHERE  vehicleList.id in(:vehicleIds) ";
                        sqlParam.addValue("vehicleIds", vehicleByContractorIdList);
                    }
//                else{
//                    qry += " WHERE  vehicleList.id in(0) ";
//                    // sqlParam.addValue("vehicleIds",vehicleId);;
//                }

                } else {
                    if (vehicleId != null && vehicleByContractorIdList.size() > 0) {
                        qry += " and  vehicleList.id in(:vehicleIds) ";
                        sqlParam.addValue("vehicleIds", vehicleByContractorIdList);
                    }
//                else{
//                    qry += " and  vehicleList.id in(0) ";
//                    // sqlParam.addValue("vehicleIds",vehicleId);;
//                }

                }
            } else if (user.getUserLevelId() == 3) {
                List<Integer> blockId = userRepositoryImpl.getBlockIdByUserId(userId);
                List<Integer> contractorId = geoMasterRepositoryImpl.getContractorIdByBlockList(blockId);
                List<Integer> vehicleByContractorIdList = masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
                if (qry != " " && qry.length() <= 0) {
                    if (vehicleId != null && vehicleByContractorIdList.size() > 0) {
                        qry += " WHERE  vehicleList.id in(:vehicleIds) ";
                        sqlParam.addValue("vehicleIds", vehicleByContractorIdList);
                    }
//                else{
//                    qry += " WHERE  vehicleList.id in(0) ";
//                    // sqlParam.addValue("vehicleIds",vehicleId);;
//                }

                } else {
                    if (vehicleId != null && vehicleByContractorIdList.size() > 0) {
                        qry += " and  vehicleList.id in(:vehicleIds) ";
                        sqlParam.addValue("vehicleIds", vehicleByContractorIdList);
                    }
//                else{
//                    qry += " and  vehicleList.id in(0) ";
//                    // sqlParam.addValue("vehicleIds",vehicleId);;
//                }

                }
            } else if (user.getUserLevelId() == 4) {
//
//            List<Integer> districtId=userRepositoryImpl.getDistrictByDivisionId(divisionId);
//            List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(districtId);
//            List<Integer> vehicleId  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
                List<Integer> divisionId = userRepositoryImpl.getDivisionByUserId(userId);
                List<Integer> workIds = userRepositoryImpl.getWorkIdsByDivisionId(divisionId);
                List<Integer> activityIds = userRepositoryImpl.getActivityIdByWorkId(workIds);
                List<Integer> vehicleByContractorIdList = userRepositoryImpl.getVehicleIdByActivityId(activityIds);

                if (qry != " " && qry.length() <= 0) {
                    if (vehicleId != null && vehicleByContractorIdList.size() > 0) {
                        qry += " WHERE  vehicleList.id in(:vehicleIds) ";
                        sqlParam.addValue("vehicleIds", vehicleByContractorIdList);
                    }
//                else{
//                    qry += " WHERE  vehicleList.id in(0) ";
//                    // sqlParam.addValue("vehicleIds",vehicleId);;
//                }

                } else {
                    if (vehicleId != null && vehicleByContractorIdList.size() > 0) {
                        qry += " and  vehicleList.id in(:vehicleIds) ";
                        sqlParam.addValue("vehicleIds", vehicleByContractorIdList);
                    }
//                else{
//                    qry += " and  vehicleList.id in(0) ";
//                    // sqlParam.addValue("vehicleIds",vehicleId);;
//                }

                }
            }
        }


        if (vehicleWorkStartDate != null && vehicleWorkEndDate == null) {
            qry += "  and vdm.created_on BETWEEN :vehicleWorkStartDate AND now() or vdm.deactivation_date BETWEEN :vehicleWorkStartDate AND now() ";
            sqlParam.addValue("vehicleWorkStartDate", vehicleWorkStartDate);

        }
        if (vehicleWorkStartDate != null && vehicleWorkEndDate != null) {
            qry += "  and vdm.created_on BETWEEN :vehicleWorkStartDate AND :vehicleWorkEndDate or vdm.deactivation_date BETWEEN :vehicleWorkStartDate AND :vehicleWorkEndDate ";
            sqlParam.addValue("vehicleWorkStartDate", vehicleWorkStartDate);
            sqlParam.addValue("vehicleWorkEndDate", vehicleWorkEndDate);
        }

//
//        UserInfoDto user=userRepositoryImpl.getUserByUserId(userId);
//        if(user.getUserLevelId()==5){
//            if(qry.length()<=0) {
//                qry += " WHERE  owner.contractor_id=:contractorId ";
//                sqlParam.addValue("contractorId",userId);
//            }
//            else{
//                qry += " and owner.contractor_id=:contractorId  ";
//                sqlParam.addValue("contractorId",userId);
//            }
//        }
//      else if(user.getUserLevelId()==1){
//        List<Integer> userIdList= helperServiceImpl.getLowerUserByUserId(userId);
//        qry+=" ";
//    }
//            else if(user.getUserLevelId()==2){
//        List<Integer> distId=userRepositoryImpl.getDistIdByUserId(userId);
//        List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(distId);
//        List<Integer> vehicleIds  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
//        if(qry.length()<=0) {
//            qry += " WHERE  vm.id in(:vehicleIds) ";
//            sqlParam.addValue("vehicleIds",vehicleIds);;
//        }
//        else{
//            qry += " and vm.id in(:vehicleIds)  ";
//            sqlParam.addValue("vehicleIds",vehicleIds);;
//        }
//    }
//        else if(user.getUserLevelId()==3){
//        List<Integer> blockId=userRepositoryImpl.getBlockIdByUserId(userId);
//        List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByBlockList(blockId);
//        List<Integer> vehicleIds  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
//        if(qry.length()<=0) {
//            qry += " WHERE  vm.id in(:vehicleIds) ";
//            sqlParam.addValue("vehicleIds",vehicleIds);;
//        }
//        else{
//            qry += " and vm.id in(:vehicleIds) ";
//            sqlParam.addValue("vehicleIds",vehicleIds);;
//        }
//    }
//        else if(user.getUserLevelId()==4){
//        List<Integer> divisionId=userRepositoryImpl.getDivisionByUserId(userId);
//        List<Integer> districtId=userRepositoryImpl.getDistrictByDivisionId(divisionId);
//        List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(districtId);
//        List<Integer> vehicleIds  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
//        if(qry.length()<=0) {
//            qry += " WHERE  vm.id in(:vehicleIds) ";
//            sqlParam.addValue("vehicleIds",vehicleIds);;
//        }
//        else{
//            qry += " and vm.id in(:vehicleIds) ";
//            sqlParam.addValue("vehicleIds",vehicleIds);;
//        }
//    }


        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
    }

    @Override
    public VehicleWorkMappingDto getVehicleWorkMapping(Integer activityId) {
        List<VehicleWorkMappingDto> vehicleWork = new ArrayList<>();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select work.id as workId,work.g_work_id as gWorkId,work.g_work_name as workName," +
                "work.completion_date as completionDate,work.work_status as workStatusId,status.name as status," +
                "work.approval_status as approvalStatusId, work.pmis_finalize_date as pmisFinalizeDate,work.award_date as awardDate,approvalStatus.name as approvalStatus from rdvts_oltp.work_m as work " +
                "left join rdvts_oltp.activity_work_mapping  as activity on activity.work_id=work.id " +
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
                " left join rdvts_oltp.activity_work_mapping as activity on activity.work_id=work.id " +
                " left join rdvts_oltp.work_status_m as status on status.id=work.work_status " +
                " left join rdvts_oltp.approval_status_m as approvalStatus on approvalStatus.id=work.approval_status " +
                " where activity.activity_id in (:activityIds) and activity.is_active=false  ";
        sqlParam.addValue("activityIds", activityIds);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));
    }

    @Override
    public Page<VehicleMasterDto> getVehicleList(VehicleFilterDto vehicle, List<Integer> distIds, List<Integer> divisionIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

        currentDateTime = currentDateTime + " 00:00:00";
//        PageRequest pageable = null;
//        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
//        pageable = PageRequest.of(vehicle.getDraw() - 1, vehicle.getLimit(), Sort.Direction.fromString("desc"), "id");

        int pageNo = vehicle.getOffSet() / vehicle.getLimit();
        PageRequest pageable = PageRequest.of(pageNo, vehicle.getLimit(), Sort.Direction.fromString("asc"), "id");
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");

        int resultCount = 0;
//        String qry = "select distinct * from (SELECT distinct vm.id, vm.vehicle_no,vt.name as vehicleTypeName,vm.model, vm.chassis_no, \n" +
//                " vm.engine_no,vm.is_active as active, vm.created_by,vm.created_on,vm.updated_by,vm.updated_on ,    \n" +
//                " owner.user_id as userId,concat(userM.first_name,' ',userM.middle_name,' ',userM.last_name) as ownerName, \n" +
//                " contractor.name as contractorName, owner.contractor_id as contractorId,vm.vehicle_type_id as vehicleTypeId, device.device_id as deviceId, dam.dist_id as distId, dam.division_id as divisionId,\n" +
//                "--  vm.vehicle_type_id as vehicleTypeId, device.device_id as deviceId, dam.dist_id as distId, dam.division_id, awm.activity_id  as activityId,owner.contractor_id,  \n" +
//                " \n" +
//                " case when vdCount.vehicleCount>0 then true else false end as deviceAssigned, \n" +
//                " case when vtuLocation.imeiCount>0 then true else false end as trackingStatus, \n" +
//                " case when actCount.activityCount>0 then true else false end as activityAssigned  \n" +
//                " FROM rdvts_oltp.vehicle_m as vm  \n" +
//                " left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id  and vt.is_active=true \n" +
//                " left join rdvts_oltp.vehicle_device_mapping as device on device.vehicle_id=vm.id and device.is_active=true \n" +
//                " left join rdvts_oltp.device_area_mapping as dam on dam.device_id = device.device_id and dam.is_active =true    \n" +
//                " left join rdvts_oltp.vehicle_activity_mapping as activity on vm.id = activity.vehicle_id and activity.is_active=true \n" +
//                "--                 left join rdvts_oltp.activity_m as am on am.id = activity.activity_id   \n" +
//                " left join rdvts_oltp.activity_work_mapping as awm on activity.activity_id = awm.id and awm.is_active=true   \n" +
//                " left join rdvts_oltp.work_m as work on work.id = awm.work_id   and work.is_active=true \n" +
//                " left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vm.id and owner.is_active=true  \n" +
//                " left join rdvts_oltp.user_m as userM on  userM.id=owner.user_id and  userM.is_active=true \n" +
//                " left join rdvts_oltp.contractor_m as contractor on contractor.id=owner.contractor_id and contractor.is_active=true \n" +
//                " left join rdvts_oltp.device_m as dm on dm.id=device.device_id and dm.is_active=true \n" +
//                " left join (select count(id) over (partition by vehicle_id) as vehicleCount,vehicle_id from  rdvts_oltp.vehicle_device_mapping  \n" +
//                " where is_active=true and deactivation_date is null) as vdCount on vdCount.vehicle_id=device.vehicle_id  \n" +
//                " left join (select count(id) over (partition by imei) as imeiCount,imei from rdvts_oltp.vtu_location where date(date_time)=date(now()) and is_active=true ) as vtuLocation  \n" +
//                " on vtuLocation.imei=dm.imei_no_1  \n" +
//                " left join (select count(id) over (partition by vehicle_id) as activityCount,vehicle_id from rdvts_oltp.vehicle_activity_mapping where is_active=true) as actCount  \n" +
//                " on actCount.vehicle_id=activity.vehicle_id) as vehicleList ";
        String qry = "select * from (SELECT distinct vm.id, vm.vehicle_no, vm.vehicle_type_id as vehicleTypeId,vt.name as vehicleTypeName,vm.model, vm.chassis_no," +
                "vm.engine_no,vm.is_active as active,device.device_id as deviceId, vm.created_by,vm.created_on,vm.updated_by,vm.updated_on ,dam.dist_id, dam.division_id,   " +
                "owner.user_id as userId, owner.user_id as userByVehicleId,concat(userM.first_name,' ',userM.middle_name,' ',userM.last_name) as ownerName,owner.contractor_id ,   " +
                "contractor.name as contractorName,am.id  as activityId," +
                "case when vdCount.vehicleCount>0 then true else false end as deviceAssigned," +
                " case when vtuLocation.pooling_status IS NOT NULL then vtuLocation.pooling_status else false end as trackingStatus, " +
                "case when actCount.activityCount>0 then true else false end as activityAssigned " +
                "FROM rdvts_oltp.vehicle_m as vm " +
                "left join rdvts_oltp.vehicle_type as vt on vm.vehicle_type_id=vt.id  " +
                "left join rdvts_oltp.vehicle_device_mapping as device on device.vehicle_id=vm.id and device.is_active=true " +
                "left join rdvts_oltp.device_area_mapping as dam on dam.device_id = device.device_id and dam.is_active =true  " +
                "left join rdvts_oltp.vehicle_activity_mapping as activity on vm.id = activity.vehicle_id and activity.is_active=true " +
                "left join rdvts_oltp.geo_mapping as gm on gm.id=activity.geo_mapping_id and gm.is_active=true " +
                "left join rdvts_oltp.activity_m as am on am.id = gm.activity_id  " +
                "left join rdvts_oltp.activity_work_mapping as awm on am.id = awm.activity_id  " +
                "left join rdvts_oltp.work_m as work on work.id = awm.work_id  " +
                "left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vm.id " +
                "left join rdvts_oltp.user_m as userM on  userM.id=owner.user_id " +
                "left join rdvts_oltp.contractor_m as contractor on contractor.id=owner.contractor_id " +
                "left join rdvts_oltp.device_m as dm on dm.id=device.device_id " +
                "left join (select count(id) over (partition by vehicle_id) as vehicleCount,vehicle_id from  rdvts_oltp.vehicle_device_mapping " +
                " where is_active=true and deactivation_date is null) as vdCount on vdCount.vehicle_id=device.vehicle_id " +
                " left join rdvts_oltp.vehicle_pooling_status as vtuLocation on vtuLocation.vehicle_id=vm.id " +
                "left join (select count(id) over (partition by vehicle_id) as activityCount,vehicle_id from rdvts_oltp.vehicle_activity_mapping where is_active=true) as actCount " +
                "on actCount.vehicle_id=activity.vehicle_id) as vehicleList ";

        sqlParam.addValue("currentDateTime", currentDateTime);

        String subQuery = "";
        if (vehicle.getDeviceAssign() != null) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE deviceAssigned=:deviceAssigned ";
                sqlParam.addValue("deviceAssigned", vehicle.getDeviceAssign());
            } else {
                subQuery += " and deviceAssigned=:deviceAssigned ";
                sqlParam.addValue("deviceAssigned", vehicle.getDeviceAssign());
            }

        }
        if (vehicle.getTrackingAssign() != null) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE trackingStatus=:trackingStatus ";
                sqlParam.addValue("trackingStatus", vehicle.getTrackingAssign());
            } else {
                subQuery += " and trackingStatus=:trackingStatus ";
                sqlParam.addValue("trackingStatus", vehicle.getTrackingAssign());
            }

        }
        if (vehicle.getActivityAssign() != null) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE activityAssigned=:activityAssigned ";
                sqlParam.addValue("activityAssigned", vehicle.getActivityAssign());
            } else {
                subQuery += " and activityAssigned=:activityAssigned ";
                sqlParam.addValue("activityAssigned", vehicle.getActivityAssign());
            }

        }

        if (vehicle.getVehicleId() != null && vehicle.getVehicleId() > 0) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE  id=:vehicleId ";
                sqlParam.addValue("vehicleId", vehicle.getVehicleId());
            } else {
                subQuery += " and  id=:vehicleId ";
                sqlParam.addValue("vehicleId", vehicle.getVehicleId());
            }
        }


        if (vehicle.getVehicleTypeId() != null && vehicle.getVehicleTypeId() > 0) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE  vehicleTypeId=:vehicleTypeId ";
                sqlParam.addValue("vehicleTypeId", vehicle.getVehicleTypeId());
            } else {
                subQuery += " and  vehicleTypeId=:vehicleTypeId ";
                sqlParam.addValue("vehicleTypeId", vehicle.getVehicleTypeId());
            }
        }
        if (vehicle.getDeviceId() > 0) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE deviceId=:deviceId ";
                sqlParam.addValue("deviceId", vehicle.getDeviceId());
            } else {
                subQuery += " and deviceId=:deviceId ";
                sqlParam.addValue("deviceId", vehicle.getDeviceId());
            }
        }

        if (vehicle.getContractorId() != null && vehicle.getContractorId() > 0) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE  vehicleList.contractor_id=:contractorId ";
                sqlParam.addValue("contractorId", vehicle.getContractorId());
            } else {
                subQuery += " and vehicleList.contractor_id=:contractorId  ";
                sqlParam.addValue("contractorId", vehicle.getContractorId());
            }
        }


        if (vehicle.getUserByVehicleId() != null && vehicle.getUserByVehicleId() > 0) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE  userByVehicleId=:userByVehicleId ";
                sqlParam.addValue("userByVehicleId", vehicle.getUserByVehicleId());
            } else {
                subQuery += " and userByVehicleId=:userByVehicleId  ";
                sqlParam.addValue("userByVehicleId", vehicle.getUserByVehicleId());
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
            if (subQuery.length() <= 0) {
                subQuery += " WHERE activityId=:activityId ";
                sqlParam.addValue("activityId", vehicle.getActivityId());
            } else {
                subQuery += " and activityId=:activityId ";
                sqlParam.addValue("activityId", vehicle.getActivityId());
            }
        }


        if (vehicle.getDistId() > 0) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE vehicleList.dist_Id IN (:distIds) ";
                sqlParam.addValue("distIds", vehicle.getDistId());
            } else {
                subQuery += " and vehicleList.dist_Id IN (:distIds)";
                sqlParam.addValue("distIds", vehicle.getDistId());
            }
        }

        if (vehicle.getDivisionId() > 0) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE vehicleList.division_id IN (:divisionIds) ";
                sqlParam.addValue("divisionIds", vehicle.getDivisionId());
            } else {
                subQuery += " and vehicleList.division_id IN (:divisionIds)";
                sqlParam.addValue("divisionIds", vehicle.getDivisionId());
            }
        }

        //Validation on basis of userLevel and lower level user

        UserInfoDto user = userRepositoryImpl.getUserByUserId(vehicle.getUserId());
        if (user.getUserLevelId() == 5) {
            if (subQuery.length() <= 0) {
                subQuery += " WHERE  vehicleList.contractor_id=:contractorId ";
                sqlParam.addValue("contractorId", vehicle.getUserId());
            } else {
                subQuery += " and vehicleList.contractor_id=:contractorId  ";
                sqlParam.addValue("contractorId", vehicle.getUserId());
            }
        }
//       *//* else if(user.getUserLevelId()==1){
//             List<Integer> userIdList= helperServiceImpl.getLowerUserByUserId(vehicle.getUserId());
//           qry+=" ";
//        }*//*
        else if (user.getUserLevelId() == 2) {
            List<Integer> distId = userRepositoryImpl.getDistIdByUserId(vehicle.getUserId());
            // List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(distId);
            List<Integer> deviceId = userRepositoryImpl.getDeviceIdByDistIds(distId);
            List<Integer> vehicleId = masterRepositoryImpl.getVehicleByDeviceIdList(deviceId);
            if (subQuery != " " && subQuery.length() <= 0) {
                if (vehicleId != null && vehicleId.size() > 0) {
                    subQuery += " WHERE  vehicleList.id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds", vehicleId);
                } else {
                    subQuery += " WHERE  vehicleList.id in(0) ";
                    // sqlParam.addValue("vehicleIds",vehicleId);;
                }

            } else {
                if (vehicleId != null && vehicleId.size() > 0) {
                    subQuery += " and  vehicleList.id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds", vehicleId);
                } else {
                    subQuery += " and  vehicleList.id in(0) ";
                    // sqlParam.addValue("vehicleIds",vehicleId);;
                }

            }
        } else if (user.getUserLevelId() == 3) {
            List<Integer> blockId = userRepositoryImpl.getBlockIdByUserId(vehicle.getUserId());
            List<Integer> distId = userRepositoryImpl.getDistIdByBlockId(blockId);
            // List<Integer> deviceIds =userRepositoryImpl.getContractorIdByBlockList(blockId);
            List<Integer> deviceIds = userRepositoryImpl.getDeviceIdByDistIds(distId);
            if (deviceIds != null && deviceIds.size() > 0) {
                List<Integer> vehicleId = masterRepositoryImpl.getVehicleByDeviceIdList(deviceIds);
                if (subQuery != " " && subQuery.length() <= 0) {
                    if (vehicleId != null && vehicleId.size() > 0) {
                        subQuery += " WHERE  vehicleList.id in(:vehicleIds) ";
                        sqlParam.addValue("vehicleIds", vehicleId);
                    } else {
                        subQuery += " WHERE  vehicleList.id in(0) ";
                        // sqlParam.addValue("vehicleIds",vehicleId);;
                    }

                } else {
                    if (vehicleId != null && vehicleId.size() > 0) {
                        subQuery += " and  vehicleList.id in(:vehicleIds) ";
                        sqlParam.addValue("vehicleIds", vehicleId);
                    } else {
                        subQuery += " and  vehicleList.id in(0) ";
                        // sqlParam.addValue("vehicleIds",vehicleId);;
                    }

                }
            } else subQuery += " WHERE  vehicleList.id in(0) ";
        } else if (user.getUserLevelId() == 4) {
//
//            List<Integer> districtId=userRepositoryImpl.getDistrictByDivisionId(divisionId);
//            List<Integer> contractorId =geoMasterRepositoryImpl.getContractorIdByDistIdList(districtId);
//            List<Integer> vehicleId  =masterRepositoryImpl.getVehicleByContractorIdList(contractorId);
            List<Integer> divisionId = userRepositoryImpl.getDivisionByUserId(vehicle.getUserId());
            List<Integer> workIds = userRepositoryImpl.getWorkIdsByDivisionId(divisionId);
            List<Integer> activityIds = userRepositoryImpl.getActivityIdByWorkId(workIds);
            List<Integer> vehicleId = userRepositoryImpl.getVehicleIdByActivityId(activityIds);

            if (subQuery != " " && subQuery.length() <= 0) {
                if (vehicleId != null && vehicleId.size() > 0) {
                    subQuery += " WHERE  vehicleList.id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds", vehicleId);
                } else {
                    subQuery += " WHERE  vehicleList.id in(0) ";
                    // sqlParam.addValue("vehicleIds",vehicleId);;
                }

            } else {
                if (vehicleId != null && vehicleId.size() > 0) {
                    subQuery += " and  vehicleList.id in(:vehicleIds) ";
                    sqlParam.addValue("vehicleIds", vehicleId);
                } else {
                    subQuery += " and  vehicleList.id in(0) ";
                    // sqlParam.addValue("vehicleIds",vehicleId);;
                }

            }
        }

        String finalQry = qry + " " + subQuery;
        resultCount = count(finalQry, sqlParam);
        if (vehicle.getLimit() > 0) {
            finalQry += " Order by id desc LIMIT " + vehicle.getLimit() + " OFFSET " + vehicle.getOffSet();
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
        List<VehicleMasterDto> vehicle = new ArrayList<>();
//        String qry = "SELECT ve.id, ve.vehicle_no, ve.vehicle_type_id, vt.name as vehicleTypeName , ve.model, ve.speed_limit, ve.chassis_no, ve.engine_no, ve.engine_no, ve.is_active, ve.is_active, ve.created_by, " +
//                "ve.created_on, ve.updated_by, ve.updated_on " +
//                "FROM rdvts_oltp.vehicle_m AS ve " +
//                "LEFT JOIN rdvts_oltp.vehicle_type AS vt ON vt.id=ve.vehicle_type_id " +
//                "WHERE ve.is_active = true";
//
//        if (id > 0) {
//            qry += " AND ve.id=:id";
//        }
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
        if (id > 0) {
            qry += " where vm.id=:vehicleId";
        }
        sqlParam.addValue("vehicleId", id);
//        sqlParam.addValue("id", id);
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
        if (count > 0) {
            tracking = true;
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
        if (count > 0) {
            activity = true;
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
                "SET is_active=false,deactivation_date=now()  WHERE activity_id IN (:activityIds) or vehicle_id IN (:vehicleIds) ";
        sqlParam.addValue("activityIds", activityIds);
        sqlParam.addValue("vehicleIds", vehicleIds);
        return namedJdbc.update(qry, sqlParam);
    }

    public LocationDto getLatestLocationByDeviceId(Long imeiNo) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        LocationDto locationDto = null;

        //String qry = "select  * from rdvts_oltp.vtu_location  where imei=:imeiNo order by date_time desc limit 1 ";
        // changes 21 feb sp
        String qry = "   select b.*  from rdvts_oltp.vehicle_device_mapping as vdm " +
                "        left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id " +
                "        left join (select distinct imei,max(id) over(partition by imei) as vtuid from " +
                "        rdvts_oltp.vtu_location as vtu where   gps_fix::numeric =1  and imei in (:imeiNo) ) as a on dm.imei_no_1=a.imei " +
                "        left join rdvts_oltp.vtu_location as b on a.vtuid=b.id " +
                "        where vdm.is_active=true and b.id is not null and b.gps_fix::numeric =1 order by b.date_time desc";
        sqlParam.addValue("imeiNo", imeiNo);
        try {
            locationDto = namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(LocationDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }


        return locationDto;
    }

    public List<AlertDto> getAlertList(AlertFilterDto filterDto, Long imeiNo) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        PageRequest pageable = null;
//
//        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
//        pageable = PageRequest.of(filterDto.getDraw()-1,filterDto.getLimit(), Sort.Direction.fromString("desc"), "id");
//
//        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC,"id");
//        int resultCount=0;

        String qry = " select alert.id as alertId,imei,alert_type_id,type.alert_type as alertTypeName,latitude,longitude,altitude,accuracy,speed,gps_dtm,vdm.vehicle_id as vehicleId, " +
                " is_resolve,resolved_by as resolvedBy,userM.first_name as resolvedByUser from  rdvts_oltp.alert_data  as alert  " +
                " left join rdvts_oltp.alert_type_m as type on type.id=alert.alert_type_id  " +
                " left join rdvts_oltp.user_m as userM on userM.id=alert.resolved_by  " +
                " left join rdvts_oltp.device_m as dm on dm.imei_no_1=alert.imei " +
                " left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.device_id=dm.id where imei=:imeiNo Order by alert.id ";

        sqlParam.addValue("imeiNo", imeiNo);

        if (filterDto.getAlertTypeId() != null && filterDto.getAlertTypeId() > 0) {
            qry += " AND alert.alert_type_id=:alertTypeId ";
            sqlParam.addValue("alertTypeId", filterDto.getAlertTypeId());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (filterDto.getStartDate() != null && !filterDto.getStartDate().isEmpty()) {
            qry += " AND date(alert.gps_dtm) >= :startDate";
            Date startDate = null;
            try {
                startDate = format.parse(filterDto.getStartDate());
            } catch (Exception exception) {
                log.info("From Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("startDate", startDate, Types.DATE);
        }
        if (filterDto.getEndDate() != null && !filterDto.getEndDate().isEmpty()) {
            qry += " AND date(alert.gps_dtm) <= :endDate";
            Date endDate = null;
            try {
                endDate = format.parse(filterDto.getEndDate());
            } catch (Exception exception) {
                log.info("To Date Parsing exception : {}", exception.getMessage());
            }
            sqlParam.addValue("endDate", endDate, Types.DATE);
        }


//        resultCount = count(qry, sqlParam);
//        if (filterDto.getLimit() > 0){
//            qry += " Order by alert.id desc LIMIT " + filterDto.getLimit() + " OFFSET " + filterDto.getOffSet();
//        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class));

//        List<AlertDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDto.class));
//        return new PageImpl<>(list, pageable, resultCount);
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
                "left join rdvts_oltp.activity_work_mapping as awm on awm.activity_id = am.id " +
                " WHERE 1=1 and am.is_active=true and vm.is_active=true and vam.is_active=true ";
        if (id > 0) {
            qry += " and awm.work_id=:workId";
            sqlParam.addValue("workId", id);
        }
        return namedJdbc.queryForObject(qry, sqlParam, Integer.class);


    }


    public List<VehicleMasterDto> getVehicleHistoryList(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = "select vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicle_type_name,vm.model,vm.speed_limit,vm.chassis_no,vm.engine_no,vm.is_active as active, " +
//                "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on,dm.imei_no_1 as imeiNo1 from rdvts_oltp.vehicle_m as vm " +
//                " left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id = vm.id " +
//                "left join rdvts_oltp.activity_work_mapping as awm on vam.activity_id = awm.id " +
//                "left join rdvts_oltp.vehicle_type as vt on vt.id = vm.vehicle_type_id " +
//                "left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.vehicle_id = vm.id and vdm.is_active =true " +
//                "left join rdvts_oltp.device_m as dm on dm.id = vdm.device_id and dm.is_active = true " +
//                "where vm.is_active = true and vam.is_active = true and vt.is_active= true and awm.is_active = true " ;

//        String qry = "select vm.id, vm.vehicle_no, vm.vehicle_type_id,vt.name as vehicle_type_name,vm.model,vm.speed_limit,vm.chassis_no,vm.engine_no,vm.is_active as active, " +
//                "vm.created_by,vm.created_on,vm.updated_by,vm.updated_on,dm.imei_no_1 as imeiNo1, owner.is_contractor as contractor, owner.user_id as userId,concat(userM.first_name,' ',userM.middle_name,' ',userM.last_name) as ownerName,owner.contractor_id , " +
//                "contractor.name as contractorName from rdvts_oltp.vehicle_m as vm " +
//                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id = vm.id " +
//                "left join rdvts_oltp.activity_work_mapping as awm on vam.activity_id = awm.id " +
//                "left join rdvts_oltp.vehicle_type as vt on vt.id = vm.vehicle_type_id " +
//                "left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.vehicle_id = vm.id and vdm.is_active =true " +
//                "left join rdvts_oltp.device_m as dm on dm.id = vdm.device_id and dm.is_active = true " +
//                "left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vm.id and owner.is_active = true " +
//                "left join rdvts_oltp.user_m as userM on  userM.id=owner.user_id and userM.is_active = true " +
//                "left join rdvts_oltp.contractor_m as contractor on contractor.id=owner.contractor_id  and contractor.is_active = true " +
//                "where vm.is_active = true and vam.is_active = true and vt.is_active= true and awm.is_active = true ";

        String qry = " select distinct vm.id,vm.vehicle_no,vm.vehicle_type_id,type.name as vehicleTypeName,vm.model,vm.speed_limit,vm.chassis_no,vm.engine_no," +
                "  owner.is_contractor as contractor, owner.user_id as userId,concat(userM.first_name,' ',userM.middle_name,' ',userM.last_name) as ownerName,owner.contractor_id , \n" +
                " contractor.name as contractorName, dm.imei_no_1 as imeiNo1 \n" +
                "from  rdvts_oltp.vehicle_activity_mapping as vam\n" +
                "left join rdvts_oltp.geo_mapping as geoMapping on geoMapping.id=vam.geo_mapping_id\n" +
                "left join rdvts_oltp.vehicle_m as vm on vam.vehicle_id = vm.id and vam.is_active = true\n" +
                "left join rdvts_oltp.vehicle_type as type on type.id = vm.vehicle_type_id\n" +
                " left join rdvts_oltp.vehicle_owner_mapping as owner on owner.vehicle_id=vm.id\n" +
                " left join rdvts_oltp.contractor_m as contractor on contractor.id=owner.contractor_id\n" +
                " left join rdvts_oltp.user_m as userM on userM.id=owner.user_id" +
                " left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.vehicle_id=vm.id and vdm.is_active=true\n" +
                " left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id and dm.is_active=true " +
                " WHERE vm.is_active = true  ";
        if (id > 0) {
            qry += " and geoMapping.package_id= :id ";
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }

    public List<RoadMasterDto> getRoadArray(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = " select distinct gm.*, rm.road_name as roadName from rdvts_oltp.geo_mapping as gm " +
//                " left join rdvts_oltp.road_m as rm on gm.road_id = rm.id and rm.is_active = true " +
//                " where gm.is_active = true ";
        String qry = "select distinct gm.package_id, gm.piu_id, gm.road_id, gm.contractor_id, rm.road_name, rm.sanction_length,rm.sanction_date, dbm.district_name as roadName from rdvts_oltp.geo_mapping as gm \n" +
                " left join rdvts_oltp.road_m as rm on gm.road_id = rm.id and rm.is_active = true \n" +
                " left join rdvts_oltp.district_boundary as dbm on dbm.dist_id = gm.dist_id\n" +
                " where gm.is_active = true ";
        if (id > 0) {
            qry += " and gm.package_id = :id ";
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
    }

    public List<VehicleMasterDto> getVehicleByVehicleTypeId(Integer vehicleTypeId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        qry += "SELECT vm.id, vm.vehicle_no, vm.vehicle_type_id as vehicleTypeId, vm.chassis_no, vm.engine_no " +
                "FROM rdvts_oltp.vehicle_m as vm " +
                "LEFT JOIN rdvts_oltp.vehicle_type as vt on vt.id= vm.vehicle_type_id " +
                "WHERE vm.is_active=true  ";
        if (vehicleTypeId > 0) {
            qry += " and vm.vehicle_type_id=:vehicleTypeId ";
        }
        sqlParam.addValue("vehicleTypeId", vehicleTypeId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
    }

    public List<RoadMasterDto> getRoadDetailByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select id, package_id, package_name, road_name, road_length, road_location, " +
                "  road_allignment, ST_AsGeoJSON(geom) as geom, ST_AsGeoJSON(geom) as geoJSON, road_width, g_road_id, is_active, created_by, " +
                " created_on, updated_by, updated_on, completed_road_length, sanction_date, road_code, road_status, " +
                "  approval_status, approved_by from rdvts_oltp.geo_construction_m where id in (select road_id from rdvts_oltp.geo_master where work_id in " +
                " (select work_id from rdvts_oltp.activity_work_mapping where activity_id in " +
                "(select activity_id from rdvts_oltp.vehicle_activity_mapping where vehicle_id in (:vehicleId)))) and is_active=true  ";
        sqlParam.addValue("vehicleId", vehicleId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
    }

    public List<Integer> getVehicleIdsByActivityId(List<Integer> activityList) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select vm.id as vehicle_id from rdvts_oltp.vehicle_m as vm " +
                "join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id =vm.id " +
                "where vm.is_active = true and vam.activity_id IN (:activityList) ";
        sqlParam.addValue("activityList", activityList);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>());
    }

    public List<Integer> getDeviceIdsByVehicleIds(List<Integer> vehicleIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select device_id from rdvts_oltp.vehicle_device_mapping where is_active = true and vehicle_id IN (:vehicleIds) ";
        sqlParam.addValue("vehicleIds", vehicleIds);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>());
    }

    public List<String> getImeiByDeviceId(List<Integer> deviceIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select imei_no_1 from rdvts_oltp.device_m where is_active = true and id IN (:deviceIds) ";
        sqlParam.addValue("deviceIds", deviceIds);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>());
    }

    public Integer getActivityByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct vam.geo_mapping_id from rdvts_oltp.vehicle_activity_mapping as vam where vam.is_active=true and vam.vehicle_id = :vehicleId";
        sqlParam.addValue("vehicleId", vehicleId);
        try {
            return namedJdbc.queryForObject(qry, sqlParam, Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Integer> getActivityIdsByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select distinct activity_id from rdvts_oltp.vehicle_activity_mapping where is_active=false and vehicle_id=:vehicleId ";
        sqlParam.addValue("vehicleId", vehicleId);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }


    public ActivityInfoDto getLiveActivityByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select  am.activity_name, gm.activity_quantity, gm.activity_start_date, gm.activity_completion_date, gm.actual_activity_start_date, gm.actual_activity_completion_date,\n" +
                "gm.executed_quantity, (case when (gm.completion_date is null) then 'IN-PROGRESS' else 'COMPLETED' end) as statusName\n" +
                "from rdvts_oltp.geo_mapping as gm\n" +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id=gm.id and vam.is_active=true\n" +
                "left join rdvts_oltp.activity_m as am on am.id=gm.activity_id\n" +
                "where gm.is_active=true  ";

        if (vehicleId > 0) {
            qry += " and vam.vehicle_id=:vehicleId  ";
        }
        sqlParam.addValue("vehicleId", vehicleId);
        try {
            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityInfoDto.class));
        } catch (Exception e) {
            return null;
        }
    }

    public List<ActivityInfoDto> getActivityListByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = "SELECT am.id,am.activity_name as activityName,awm.activity_quantity as activityQuantity,awm.activity_start_date as startDate,  " +
//                "awm.activity_completion_date as activityCompletionDate,awm.actual_activity_start_date as actualActivityStartDate,  " +
//                "awm.actual_activity_completion_date as actualActivityCompletionDate,awm.executed_quantity as executedQuantity,  " +
//                "awm.activity_status as statusId,status.name as statusName from rdvts_oltp.activity_m as am   " +
//                "left join rdvts_oltp.activity_work_mapping as awm on awm.activity_id = am.id  " +
//                "left join rdvts_oltp.activity_status_m as status on status.id=awm.activity_status  " +
//                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.activity_id = am.id   " +
//                "WHERE vam.is_active =false  ";
        String qry = "  select distinct am.id, am.activity_name, gm.activity_quantity, gm.activity_start_date, gm.activity_completion_date, gm.actual_activity_start_date, gm.actual_activity_completion_date,\n" +
                "gm.executed_quantity, (case when (gm.completion_date is null) then 'IN-PROGRESS' else 'COMPLETED' end) as workStatusName\n" +
                "from rdvts_oltp.geo_mapping as gm\n" +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id=gm.id \n" +
                "left join rdvts_oltp.activity_m as am on am.id=gm.activity_id\n" +
                "where vam.is_active=false  ";
        if (vehicleId > 0) {
            qry += " and vam.vehicle_id=:vehicleId";
        }
        sqlParam.addValue("vehicleId", vehicleId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityInfoDto.class));
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
        sqlParam.addValue("vehicleId", vehicleId);
        return namedJdbc.queryForObject(qry, sqlParam, Integer.class);
    }

    public List<Integer> getDistIdByDeviceId(List<Integer> deviceIds) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT distinct dist_id from rdvts_oltp.device_area_mapping  " +
                "WHERE is_active = true and device_id=:deviceIds ";
        sqlParam.addValue("deviceIds", deviceIds);
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

    public List<VehicleActivityMappingDto> getVehicleByActivityId(Integer activityId, Integer userId, Date actualActivityStartDate, Date actualActivityCompletionDate) {

        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT id, vehicle_id, activity_id, start_time, end_time, " +
                "start_date, end_date, is_active, created_by, created_on, updated_by, updated_on, deactivation_date, g_activity_id,geo_mapping_id " +
                " FROM rdvts_oltp.vehicle_activity_mapping where is_active=true  ";
//        if (actualActivityCompletionDate == null) {
//            actualActivityCompletionDate = new Date();
//        }

//        if (actualActivityStartDate != null && actualActivityCompletionDate != null) {
//            qry += " AND  created_on BETWEEN :activityStartDate AND :activityCompletionDate ";
//            sqlParam.addValue("activityStartDate", actualActivityStartDate);
//            sqlParam.addValue("activityCompletionDate", actualActivityCompletionDate);
//        }
        if (activityId != null && activityId > 0) {
            qry += " AND geo_mapping_id=:activityId ";
            sqlParam.addValue("activityId", activityId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleActivityMappingDto.class));
    }

    public List<Integer> getRoadIdsByVehicleIdsForFilter(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select id from rdvts_oltp.geo_construction_m where id in (select road_id from rdvts_oltp.geo_master where work_id in " +
                " (select work_id from rdvts_oltp.activity_m where id in " +
                " (select activity_id from rdvts_oltp.vehicle_activity_mapping where vehicle_id in (:vehicleId)))) and is_active=true ";
        sqlParam.addValue("vehicleId", vehicleId);
        return namedJdbc.queryForList(qry, sqlParam, Integer.class);
    }

    public void saveVehiclePoolingStatus() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        currentDateTime = currentDateTime + " 00:00:00";
        String qry = "select * from (SELECT distinct vm.id,vtuLocation.imei,\n" +
                "     case when vtuLocation.imeiCount>0 then true else false end as trackingStatus \n" +
                "     FROM rdvts_oltp.vehicle_m as \n" +
                "     vm \n" +
                "     left join rdvts_oltp.vehicle_device_mapping as device on device.vehicle_id=vm.id and device.is_active=true \n" +
                "     left join rdvts_oltp.device_m as dm on dm.id=device.device_id left join (select count(id) over (partition by vehicle_id) as vehicleCount,vehicle_id  \n" +
                "     from  rdvts_oltp.vehicle_device_mapping  where is_active=true and deactivation_date is null) as vdCount on vdCount.vehicle_id=device.vehicle_id  \n" +
                "     left join ( select distinct imei, max(id) over (partition by imei) as imeiCount from rdvts_oltp.vtu_location where date_time >=:currentDateTime::timestamp and gps_fix::numeric =1 ) as vtuLocation  \n" +
                "     on vtuLocation.imei=dm.imei_no_1 where vm.is_active=true ) as v";
        sqlParam.addValue("currentDateTime", currentDateTime);
        List<VehicleMasterDto> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        for (VehicleMasterDto item : list) {

            String selectQry = "SELECT case when count(*) >0 then true else false  end as bool" +
                    " FROM rdvts_oltp.vehicle_pooling_status where vehicle_id=" + item.getId() + "";

            if (namedJdbc.queryForObject(selectQry, sqlParam, (Boolean.class))) {
                String qryInsert1 = "UPDATE rdvts_oltp.vehicle_pooling_status " +
                        " SET  pooling_status=" + item.isTrackingStatus() + "" +
                        " WHERE vehicle_id=" + item.getId() + ";";
                namedJdbc.update(qryInsert1, sqlParam);
            } else {
                String qryInsert2 = "INSERT INTO rdvts_oltp.vehicle_pooling_status( " +
                        "vehicle_id, pooling_status)\n" +
                        " VALUES (" + item.getId() + "," + item.isTrackingStatus() + ");";
                namedJdbc.update(qryInsert2, sqlParam);
            }

        }
        // System.out.println("h");
    }

    public List<WorkDto> getPackageHistoryByVehicleId(Integer vehicleId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select  distinct pm.id,piu.name as piuName,pm.package_no as packageName, \n" +
                "   (case when (completion_date is null) then 'IN-PROGRESS' else 'COMPLETED' end) as workStatusName, rm.road_name as roadName \n" +
                "  from rdvts_oltp.geo_mapping gm  \n" +
                "  LEFT join rdvts_oltp.package_m pm on pm.id = gm.package_id  \n" +
                "  LEFT join rdvts_oltp.piu_id piu on piu.id = gm.piu_id \n" +
                "  left join rdvts_oltp.road_m as rm on rm.id=gm.road_id  " +
                "  left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id=gm.id  \n" +
                "  where vam.is_active=false and vam.vehicle_id=:vehicleId ";
        sqlParam.addValue("vehicleId", vehicleId);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkDto.class));
    }

    public Integer getGeoMappingIdByActivityId(Integer activityId, Integer packageId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select id from rdvts_oltp.geo_mapping where is_active=true and activity_id=:activityId and package_id=:packageId  ";
        sqlParam.addValue("activityId", activityId);
        sqlParam.addValue("packageId", packageId);
        return namedJdbc.queryForObject(qry, sqlParam, Integer.class);
    }

    public List<RoadMasterDto> getVehicleByPackageId(Integer packageId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadMasterDto> vehicle;
        String query = "select  distinct vam.vehicle_id as vehicleId,vm.vehicle_no as vehicleName\n" +
                "from rdvts_oltp.package_m as pm\n" +
                "left join rdvts_oltp.geo_mapping as gm on gm.package_id = pm.id and gm.is_active ='true'\n" +
                "left join rdvts_oltp.road_m as road on road.id = gm.road_id and road.is_active= 'true'\n" +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id = gm.id and vam.is_active ='true'\n" +
                "left join rdvts_oltp.vehicle_m as vm on vam.vehicle_id = vm.id and vm.is_active ='true'\n" +
                "where road.is_active = 'true' and package_id = :packageId  and vam.vehicle_id is not null";

        //sqlParam.addValue("packageId", packageId);
        sqlParam.addValue("packageId", packageId);
        try {
            vehicle = namedJdbc.query(query, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return vehicle;
    }

    public List<RoadMasterDto> getVehicleByRoadId(Integer id, Integer packageId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        List<RoadMasterDto> vehicleByRoadId;
        String query = "Select distinct pm.id as package_id,pm.package_no as package_name,rm.id as road_id,rm.road_name,vam.vehicle_id as vehicle_id,vm.vehicle_no as vehicle_name\n" +
                "from rdvts_oltp.road_m as rm\n" +
                "left join rdvts_oltp.geo_mapping as gm on gm.road_id = rm.id and gm.is_active ='true'\n" +
                "left join rdvts_oltp.package_m as pm on pm.id = gm.package_id and gm.is_active ='true'\n" +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.geo_mapping_id = gm.id and vam.is_active ='true'\n" +
                "left join rdvts_oltp.vehicle_m as vm on vam.vehicle_id = vm.id and vm.is_active ='true'\n" +
                "where package_id = :packageId and rm.id = :id and vam.vehicle_id is not null ";
        sqlParam.addValue("id", id);
        sqlParam.addValue("packageId", packageId);
        try {
            vehicleByRoadId = namedJdbc.query(query, sqlParam, new BeanPropertyRowMapper<>(RoadMasterDto.class));
        } catch (EmptyResultDataAccessException emx) {
            return null;
        }
        return vehicleByRoadId;
    }

//    public Integer updateVehicleOwner(Integer id, Integer contractorId)
//    {
//        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String query = "UPDATE rdvts_oltp.vehicle_owner_mapping set contractor_id = :contractorId " +
//                " where vehicle_id = :vehicleId and is_active= true ";
//        sqlParam.addValue("id",id);
//        sqlParam.addValue("contractorId",contractorId);
//     //   sqlParam.addValue("updatedBy",vehicleData.getUpdatedBy());
//        try
//        {
//            Integer update =namedJdbc.update(query, sqlParam);
//            return  update;
//        }
//        catch(Exception e)
//        {
//            return null;
//        }
//}

}

