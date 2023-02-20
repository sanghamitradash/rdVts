package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.ActivityWorkMapping;
import gov.orsac.RDVTS.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class WorkRepositoryImpl {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private GeoMasterRepositoryImpl geoMasterRepositoryImpl;
    @Autowired
    private HelperService helperService;



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

    public List<Integer> getWorkIdByDistId(List<Integer> distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct work_id from rdvts_oltp.geo_master where dist_id in(:distId)";
        sqlParam.addValue("distId",distId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
    public List<Integer> getWorkIdByBlockId(List<Integer> blockId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct work_id from rdvts_oltp.geo_master where block_id in (:blockId)";
        sqlParam.addValue("blockId",blockId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
    public List<Integer> getWorkIdByDivisionId(List<Integer> divisionId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct work_id from rdvts_oltp.geo_master where division_id in (:divisionId)";
        sqlParam.addValue("divisionId",divisionId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
    public List<Integer> getWorkIdByContractorId(List<Integer> contractorId){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct work_id from rdvts_oltp.geo_master where contractor_id in (:contractorId)";
        sqlParam.addValue("contractorId",contractorId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }


    public Page<WorkDto> getWorkList(WorkDto workDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        PageRequest pageable = null;
//        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
//        if (workDto != null) {
//            pageable = PageRequest.of(workDto.getOffSet(), workDto.getLimit(), Sort.Direction.fromString("desc"), "id");
//            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
//        }
        int pageNo = workDto.getOffSet()/workDto.getLimit();
        PageRequest pageable = PageRequest.of(pageNo, workDto.getLimit(),Sort.Direction.fromString("asc"),"id");
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0): new Sort.Order(Sort.Direction.DESC,"id");

        int resultCount = 0;
        String qry = "select distinct  wm.id, wm.g_work_id as geoWorkId,wm.g_work_name as geoWorkName,wm.award_date,wm.completion_date,wm.pmis_finalize_date, " +
                " wm.work_status,wm.approval_status,wm.approved_by,wm.is_active,wm.created_by,wm.updated_by,wm.created_on,wm.updated_on, " +
                " geo.contractor_id, geo.piu_id, piu.name as piuName, gcm.package_id,gcm.package_name, geo.contractor_id,wsm.name as work_status_name " +
                " from rdvts_oltp.work_m as wm " +
                " left join rdvts_oltp.geo_master as geo on geo.work_id=wm.id " +
                " left join rdvts_oltp.geo_construction_m as gcm on gcm.id=geo.road_id and gcm.is_active = true " +
                " left join rdvts_oltp.piu_id as piu on piu.id=geo.piu_id and piu.is_active = true " +
                " left join rdvts_oltp.work_status_m as wsm on wsm.id = wm.work_status and wsm.is_active = true " +
                " where wm.is_active = true ";
        if (workDto.getId() > 0) {
            qry += " and wm.id = :id";
            sqlParam.addValue("id", workDto.getId());
        }
        if (workDto.getWorkStatus() != null && workDto.getWorkStatus() > 0) {
            qry += " and wm.work_status = :work_status";
            sqlParam.addValue("work_status", workDto.getWorkStatus());
        }
        if (workDto.getDistId() != null && workDto.getDistId() > 0){
            qry += " and geo.dist_id = :distId";
            sqlParam.addValue("distId", workDto.getDistId());
        }
        if (workDto.getDivisionId() != null && workDto.getDivisionId() > 0){
            qry += " and geo.division_id = :divisionId";
            sqlParam.addValue("divisionId", workDto.getDivisionId());
        }
        if (workDto.getCircleId() != null && workDto.getCircleId() > 0){
            qry += " and geo.circle_id = :circleId";
            sqlParam.addValue("circleId", workDto.getCircleId());
        }
        if (workDto.getPackageId() != null && workDto.getPackageId() > 0  ){
            qry += " and gcm.package_id = :packageId ";
            sqlParam.addValue("packageId", workDto.getPackageId());
        }
//        if (workDto.getActivityId() > 0) {
//            qry += " and act.id = :activityId";
//            sqlParam.addValue("activityId", workDto.getActivityId());
//        }
//        qry+= "order by geoWorkName DESC ";

        UserInfoDto user=userRepositoryImpl.getUserByUserId(workDto.getUserId());

         if(user.getUserLevelId() == 2){
            List<Integer> distIdList=userRepositoryImpl.getDistIdByUserId(workDto.getUserId());
            List<Integer> workIds = getWorkIdByDistId(distIdList);
            qry+=" and wm.id in (:workIds) ";
            sqlParam.addValue("workIds",workIds);
        }
        else if(user.getUserLevelId()==3){
            List<Integer> blockIdList=userRepositoryImpl.getBlockIdByUserId(workDto.getUserId());
            List<Integer> workIds = getWorkIdByBlockId(blockIdList);
            qry+=" and wm.id in (:workIds) ";
            sqlParam.addValue("workIds",workIds);
        }
        else if(user.getUserLevelId() == 4){
            List<Integer> divisionIds=userRepositoryImpl.getDivisionByUserId(workDto.getUserId());
            List<Integer> workIds = getWorkIdByDivisionId(divisionIds);
            qry+=" and wm.id in (:workIds) ";
            sqlParam.addValue("workIds",workIds);
        }
        else if(user.getUserLevelId()==5){
            List<Integer> contractorIds = userRepositoryImpl.getContractorByUserId(workDto.getUserId());
            List<Integer> workIds = getWorkIdByContractorId(contractorIds);
            qry+=" and wm.id=:workIds ";
            sqlParam.addValue("workIds",workIds);
        }

        qry+= " order by wm.id DESC ";

        resultCount = count(qry, sqlParam);
        if (workDto.getLimit() > 0) {
            qry += " LIMIT " + workDto.getLimit() + " OFFSET " + workDto.getOffSet();
        }

        List<WorkDto> workDtoList = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkDto.class));
        return new PageImpl<>(workDtoList, pageable, resultCount);
    }


    public List<WorkDto> getWorkById(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct  wm.id, wm.g_work_id as geoWorkId,wm.g_work_name as geoWorkName,wm.award_date,wm.completion_date,wm.pmis_finalize_date, " +
                " wm.work_status,wm.approval_status,wm.approved_by,wm.is_active,wm.created_by,wm.updated_by,wm.created_on,wm.updated_on, " +
                " geo.contractor_id, geo.piu_id, piu.name as piuName, gcm.package_id,gcm.package_name,asm.name as approval_status_name,wsm.name as work_status_name " +
                " from rdvts_oltp.work_m as wm " +
                " left join rdvts_oltp.geo_master as geo on geo.work_id=wm.id " +
                " left join rdvts_oltp.geo_construction_m as gcm on gcm.id=geo.road_id and gcm.is_active = true " +
                " left join rdvts_oltp.piu_id as piu on piu.id=geo.piu_id and piu.is_active = true " +
                " left join rdvts_oltp.approval_status_m as asm on asm.id=wm.approval_status and asm.is_active = true " +
                " left join rdvts_oltp.work_status_m as wsm on wsm.id = wm.work_status and wsm.is_active = true " +
                " where wm.is_active = true ";
//        UserInfoDto user=userRepositoryImpl.getUserByUserId(userId);
        if (id > 0) {
            qry += " and wm.id = :id ";
            sqlParam.addValue("id", id);
        }
//         else {
//            if(user.getUserLevelId() == 2) {
//                List<Integer> distIdList=userRepositoryImpl.getDistIdByUserId(userId);
//                qry+=" and gm.dist_id in (:distIdList)";
//                sqlParam.addValue("distIdList",distIdList);
//            }
//            if(user.getUserLevelId() == 3) {
//                List<Integer> blockIdList=userRepositoryImpl.getBlockIdByUserId(userId);
//                qry+=" and gm.block_id in (:blockIdList)";
//                sqlParam.addValue("blockIdList",blockIdList);
//            }
//        }

        qry+=" order by geoWorkName";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkDto.class));
    }

    public List<ActivityDto> getActivityByWorkId(int id){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select am.activity_name , awm.*,asm.name as activity_status_name from rdvts_oltp.activity_m as am " +
                "left join rdvts_oltp.activity_work_mapping as awm on am.id = awm.activity_id and awm.is_active = true " +
                "left join rdvts_oltp.activity_status_m as asm on asm.id = awm.activity_status and asm.is_active = true " +
                "where am.is_active = true " ;
        if (id > 0){
            qry +=" and awm.work_id= :id " ;
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityDto.class));
    }

    public List<VehicleMasterDto> getVehicleBywork(List<Integer> workIds){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        if (workIds.get(0) > 0){
            String qry = "SELECT vm.* \n " +
                    "\t FROM rdvts_oltp.vehicle_work_mapping vwm left join rdvts_oltp.vehicle_m as vm on vm.id=vwm.vehicle_id  where  work_id IN(:workIds) and vwm.is_active=true  ";
            /*   "AND id>1 ORDER BY id";*/

            sqlParam.addValue("workIds", workIds);
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        }

        else {
            String qry = "SELECT vm.* \n " +
                    "\t FROM rdvts_oltp.vehicle_work_mapping vwm left join rdvts_oltp.vehicle_m as vm on vm.id=vwm.vehicle_id  where   vwm.is_active=true  ";
            /*   "AND id>1 ORDER BY id";*/

            sqlParam.addValue("workIds", workIds);
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleMasterDto.class));
        }




    }


    public List<VehicleWorkMappingDto> getVehicleListByWorkId(Integer workId){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, vehicle_id, work_id, start_time, end_time, start_date, end_date, is_active, created_by, created_on, updated_by, updated_on " +
                " FROM rdvts_oltp.vehicle_work_mapping where is_active=true ";
        /*   "AND id>1 ORDER BY id";*/
        if (workId>0){
            qry+="  and work_id =:workId ";
            sqlParam.addValue("workId", workId);
        }

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));
    }

    public List<UnassignedWorkDto> getUnAssignedWorkData(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select id,g_work_name as geoWorkName from rdvts_oltp.work_m where id not in (select work_id from rdvts_oltp.geo_master where is_active = true) and is_active = true ";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(UnassignedWorkDto.class));
    }

    public List<WorkStatusDto> getWorkStatusDD(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select id, name from rdvts_oltp.work_status_m where is_active = true ";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkStatusDto.class));
    }

    public List<ActivityWorkMapping> getActivityDetailsByWorkId(Integer workId) {

        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id, activity_id, work_id, activity_quantity, activity_start_date, activity_completion_date, actual_activity_start_date, actual_activity_completion_date, " +
                "executed_quantity, activity_status, g_activity_id, g_work_id, is_active, created_by, created_on, updated_by, updated_on " +
                " FROM rdvts_oltp.activity_work_mapping where is_active=true   " ;
        if (workId > 0){
            qry +=" and work_id= :workId " ;
            sqlParam.addValue("workId", workId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(ActivityWorkMapping.class));
    }

    public List<GeoConstructionDto> getPackageDD(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select package_id,package_name from rdvts_oltp.geo_construction_m where is_active = true ";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoConstructionDto.class));
    }
}
