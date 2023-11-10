package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DashboardCronEntity;
import gov.orsac.RDVTS.entities.PackageMasterEntity;
import gov.orsac.RDVTS.repository.DashboardRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Override
    public Integer getTotalVehicle() {
            MapSqlParameterSource sqlParam = new MapSqlParameterSource();
            String qry = "select count(id) from rdvts_oltp.vehicle_device_mapping where is_active=true " ;
            return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }

    @Override
    public Integer getTotalActive() {
            MapSqlParameterSource sqlParam = new MapSqlParameterSource();
           DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
           String currentDateTime = dateFormatter.format(new Date());
           currentDateTime = currentDateTime + " 00:00:00";
            String qry = " select count(distinct id) from rdvts_oltp.vehicle_device_mapping " +
                    "where is_active=true and device_id in(select distinct id from rdvts_oltp.device_m where imei_no_1 in  " +
                    "(select distinct imei from rdvts_oltp.vtu_location where date_time >=:currentDateTime::timestamp )) " ;
            sqlParam.addValue("currentDateTime",currentDateTime);
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
        String qry = "select count(id) from rdvts_oltp.work_m where is_active=true and work_status=2 " ;
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }

    @Override
    public List<Integer> totalActiveIds() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        currentDateTime = currentDateTime + " 00:00:00";
        String qry = "select device_id from rdvts_oltp.vehicle_device_mapping    " +
                "where is_active=true and device_id in(select distinct id from rdvts_oltp.device_m where imei_no_1 in   " +
                " (  " +
                " select distinct imei from  " +
                " rdvts_oltp.vtu_location as vtu where date_time >=:currentDateTime::timestamp  and  gps_fix::numeric =1  " +
                "  )) ";

        sqlParam.addValue("currentDateTime",currentDateTime);
        return namedJdbc.queryForList(qry,sqlParam, Integer.class);
    }

    @Override
    public List<Integer> totalInactiveIds() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        currentDateTime = currentDateTime + " 00:00:00";
        //change by SP 21 feb
//        String qry = "select device_id from rdvts_oltp.vehicle_device_mapping   " +
//                "where is_active=true and device_id not in(select distinct id from rdvts_oltp.device_m where imei_no_1 in  " +
//                "(select distinct imei from rdvts_oltp.vtu_location where date_time >=:currentDateTime::timestamp )) ";

        String qry = " select device_id from rdvts_oltp.vehicle_device_mapping    " +
                "where is_active=true and device_id in(select distinct id from rdvts_oltp.device_m where imei_no_1 not  in   " +
                " (  " +
                " select distinct imei from  " +
                " rdvts_oltp.vtu_location as vtu where date_time >=:currentDateTime::timestamp  and  gps_fix::numeric =1  " +
                "   )) ";
        sqlParam.addValue("currentDateTime",currentDateTime);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }

    @Override
    public List<DistrictWiseVehicleDto> getDistrictWiseVehicleCount(List<Integer> ids,Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct dist.dist_id as districtId ,dist.district_name,count(vehicle.vehicleCountId) over(partition by vehicle.dist_id)     " +
                "from rdvts_oltp.district_boundary as dist      " +
                "left join (select distinct vdm.id as vehicleCountId,dam.dist_id from rdvts_oltp.vehicle_device_mapping as  vdm      " +
                "left join rdvts_oltp.device_area_mapping as dam on dam.device_id=vdm.device_id       " +
                "where vdm.is_active=true  " ;
                if(ids!=null && ids.size()>0 ||  ids.size()==0){
                    if(ids.size() == 0)
                    {
                        qry+=" and AND dam.is_active = true ";
                    }
                    else {
                        qry += " and vdm.device_id in(:deviceIds) AND dam.is_active = true ";
                    }
                }
                qry+=" )as vehicle on vehicle.dist_id=dist.dist_id   ";
                sqlParam.addValue("deviceIds",ids);

        UserInfoDto user=userRepositoryImpl.getUserByUserId(userId);
        if(user.getUserLevelId()==2){
            List<Integer> distIds=userRepositoryImpl.getDistIdByUserId(userId);
            if (qry != null && qry.length() > 0) {
                qry += " AND  dist.dist_id in(:distIds) ";
                sqlParam.addValue("distIds", distIds);
            } else {
                qry += " AND dist.dist_id in(:distIds) ";
                sqlParam.addValue("distIds", distIds);
            }
        }
        else if(user.getUserLevelId()==3){
            List<Integer> blockIds=userRepositoryImpl.getBlockIdByUserId(userId);
            List<Integer> distIds = userRepositoryImpl.getDistIdByBlockId(blockIds);
            if (qry != null && qry.length() > 0) {
                qry += " AND  dist.dist_id in(:distIds) ";
                sqlParam.addValue("distIds", distIds);
            } else {
                qry += " AND dist.dist_id in(:distIds) ";
                sqlParam.addValue("distIds", distIds);
            }
        }

        else if(user.getUserLevelId()==4){
            List<Integer> divisionIds= userRepositoryImpl.getDivisionByUserId(userId);
            List<Integer> distIds = userRepositoryImpl.getDistIdByDivisionId(divisionIds);
            if (qry != null && qry.length() > 0) {
                qry += " AND  dist.dist_id in(:distIds) ";
                sqlParam.addValue("distIds", distIds);
            } else {
                qry += " AND dist.dist_id in(:distIds) ";
                sqlParam.addValue("distIds", distIds);
            }
        }

        else if(user.getUserLevelId()==5) {
            List<Integer> contractorId = userRepositoryImpl.getContractorByUserId(userId);
            List<Integer> vehicleIds = userRepositoryImpl.getVehicleIdByContractorId(contractorId);
            List<Integer> deviceIds = userRepositoryImpl.getDeviceIdsByVehicleIds(vehicleIds);
            if (deviceIds != null && deviceIds.size() > 0) {
                List<Integer> distIds = userRepositoryImpl.getDistIdsByDeviceIds(deviceIds);
                if (qry != null && qry.length() > 0) {
                    qry += " AND  dist.dist_id in(:distIds) ";
                    sqlParam.addValue("distIds", distIds);
                } else {
                    qry += " AND  dist.dist_id in(:distIds) ";
                    sqlParam.addValue("distIds", distIds);
                }
            }
            else qry += " AND dist.dist_id in(0)";
        }

        qry += " order by dist.district_name ";
        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DistrictWiseVehicleDto.class));
    }

    @Override
    public List<DivisionWiseVehicleDto> getDivisionWiseVehicleCount(List<Integer> ids,Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct div.division_id as divId ,div.division_name as divName,count(vehicle.vehicleCountId) over(partition by vehicle.division_id)     " +
                "from rdvts_oltp.division_m as div  " +
                "left join (select distinct vdm.id as vehicleCountId,dam.division_id from rdvts_oltp.vehicle_device_mapping as  vdm     " +
                "left join rdvts_oltp.device_area_mapping as dam on dam.device_id=vdm.device_id      " +
                "where vdm.is_active=true " ;
        if(ids!=null && ids.size()>0 ||  ids.size()==0){
            if(ids.size() == 0)
            {
                qry+=" AND dam.is_active = true ";
            }
            else {
                qry += " and vdm.device_id in(:deviceIds) AND dam.is_active = true ";
            }
        }
        qry+=" )as vehicle on vehicle.division_id=div.division_id   ";
        sqlParam.addValue("deviceIds",ids);

        UserInfoDto user=userRepositoryImpl.getUserByUserId(userId);
        if(user.getUserLevelId()==2){
            List<Integer> divisionIds= userRepositoryImpl.getDivisionByUserId(userId);
            List<Integer> distIds = userRepositoryImpl.getDistIdByDivisionId(divisionIds);
            if (qry != null && qry.length() > 0) {
                qry += " AND  dist.dist_id in(:distIds) ";
                sqlParam.addValue("distIds", distIds);
            } else {
                qry += " AND dist.dist_id in(:distIds) ";
                sqlParam.addValue("distIds", distIds);
            }
        }
        else if(user.getUserLevelId()==3){
            List<Integer> blockIds=userRepositoryImpl.getBlockIdByUserId(userId);
            List<Integer> distIds = userRepositoryImpl.getDistIdByBlockId(blockIds);
            List<Integer> divisionIds = userRepositoryImpl.getDivisionByDistId(distIds);
            if (qry != null && qry.length() > 0) {
                qry += " AND  div.division_id in(:divisionIds)  ";
                sqlParam.addValue("divisionIds", divisionIds);
            } else {
                qry += " AND div.division_id in(:divisionIds) ";
                sqlParam.addValue("divisionIds", divisionIds);
            }
        }

        else if(user.getUserLevelId()==4){
            List<Integer> divisionIds= userRepositoryImpl.getDivisionByUserId(userId);
            //List<Integer> distIds = userRepositoryImpl.getDistIdByDivisionId(divisionIds);
            if (qry != null && qry.length() > 0) {
                qry += " AND  div.division_id in(:divisionIds) ";
                sqlParam.addValue("divisionIds", divisionIds);
            } else {
                qry += " AND div.division_id in(:divisionIds) ";
                sqlParam.addValue("divisionIds", divisionIds);
            }
        }

        else if(user.getUserLevelId()==5) {
            List<Integer> contractorId = userRepositoryImpl.getContractorByUserId(userId);
            List<Integer> vehicleIds = userRepositoryImpl.getVehicleIdByContractorId(contractorId);
            List<Integer> deviceIds = userRepositoryImpl.getDeviceIdsByVehicleIds(vehicleIds);
            if (deviceIds != null && deviceIds.size() > 0) {
                List<Integer> divisionIds = userRepositoryImpl.getDivisionByDeviceIds(deviceIds);
                if (qry != null && qry.length() > 0) {
                    qry += " AND  div.division_id in(:divisionIds) ";
                    sqlParam.addValue("divisionIds", divisionIds);
                } else {
                    qry += " AND div.division_id in(:divisionIds) ";
                    sqlParam.addValue("divisionIds", divisionIds);
                }
            }
            else qry += " AND div.division_id in(0)";
        }

        qry += "  order by div.division_name ";

        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DivisionWiseVehicleDto.class));
    }
    public Integer updateActiveAndInActiveCronDistrictWise(DashboardCronDto dw) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.dashboard_cron set active=:active,in_active=:inActive where area_type_id=1 and area_id=:areaId ";
        sqlParam.addValue("active", dw.getActive());
        sqlParam.addValue("inActive", dw.getInActive());
        sqlParam.addValue("areaId", dw.getAreaId());
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }
    public Integer updateActiveAndInActiveCronDivisionWise(DashboardCronDto dw) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.dashboard_cron set active=:active,in_active=:inActive where area_type_id=2 and area_id=:areaId ";
        sqlParam.addValue("active", dw.getActive());
        sqlParam.addValue("inActive", dw.getInActive());
        sqlParam.addValue("areaId", dw.getAreaId());
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }
    public DashboardCronEntity findDashBoardCronByAreaIdForDistrict(Integer areaId, Integer typeId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT * from rdvts_oltp.dashboard_cron where area_id=:areaId and area_type_id=:areaTypeId";
        sqlParam.addValue("areaId", areaId);
        sqlParam.addValue("areaTypeId", typeId);
        return namedJdbc.queryForObject(qry,sqlParam,new BeanPropertyRowMapper<>(DashboardCronEntity.class));
    }
    public DashboardCronEntity findDashBoardCronByAreaIdForDivision(Integer areaId, Integer typeId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT * from rdvts_oltp.dashboard_cron where area_id=:areaId and area_type_id=:areaTypeId";
        sqlParam.addValue("areaId", areaId);
        sqlParam.addValue("areaTypeId", typeId);
        try {
            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(DashboardCronEntity.class));
        } catch (Exception e){
            return null;
        }
    }
    public List<DashboardDto> getDistrictWiseDashboardData() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select dashboard.id,dashboard.active as activeCount,dashboard.in_active as inActiveCount,dashboard.area_id as areaId,district.district_name as districtName,process_time as processTime " +
                "from rdvts_oltp.dashboard_cron as dashboard left join rdvts_oltp.district_boundary as district on district.dist_id=dashboard.area_id where dashboard.area_type_id=1 order by district.district_name";

        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DashboardDto.class));
    }
    public List<DashboardDto> getDivisionWiseDashboardData() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select dashboard.id,dashboard.active as activeCount,dashboard.in_active as inActiveCount,dashboard.area_id as areaId,division.division_name as divisionName " +
                "from rdvts_oltp.dashboard_cron as dashboard left join rdvts_oltp.division_m as division on division.division_id=dashboard.area_id where dashboard.area_type_id=2 order by division_name";

        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DashboardDto.class));
    }
    public Integer getActiveData() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT sum(active)  from rdvts_oltp.dashboard_cron where area_type_id=1 ";
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }
    public Integer getInActiveData() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT sum(in_active)  from rdvts_oltp.dashboard_cron where area_type_id=1 ";
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }
    public Integer getTotalData() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT sum(active+in_active)  from rdvts_oltp.dashboard_cron where area_type_id=1 ";
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }
    public ActiveInactiveDto getActiveInactiveVehicle(Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT sum(active) as active ,sum(in_active) as inactive,sum(active+in_active) as total from rdvts_oltp.dashboard_cron where area_type_id=1";

        return namedJdbc.queryForObject(qry,sqlParam,new BeanPropertyRowMapper<>(ActiveInactiveDto.class));
    }



    public Integer getPackageById() {

        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT count(distinct package_id) FROM rdvts_oltp.geo_mapping ";
//        if (i>0){
//            qry += "and id=:id";
//            sqlParam.addValue("id",i);
//        }

        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }


    public Integer getPackageIncompled() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT count(distinct package_id) FROM rdvts_oltp.geo_mapping WHERE completion_date is null  ";
         return   namedJdbc.queryForObject(qry,sqlParam,Integer.class);


    }

    public Integer getTotalRoadCountById() {

        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT count(distinct road_id) FROM rdvts_oltp.geo_mapping where is_active = true";
//        if (i>0){
//            qry += "and id=:id";
//            sqlParam.addValue("id",i);
//        }
        return namedJdbc.queryForObject(qry,sqlParam,Integer.class);
    }

    public Integer getRoadIncompleted() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT count(distinct road_id) FROM rdvts_oltp.geo_mapping WHERE completion_date is null and is_active = true  ";
        return   namedJdbc.queryForObject(qry,sqlParam,Integer.class);


    }

    public List<RoadLengthDto> getRoadLengthByDistIdOrPackageId(Integer distId,Integer packageId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select rdata.packageId,pm.package_no,rdata.sanctionedLength,rdata.completedRoadLength,rdata.distId " +
                "from (select distinct gm.package_id as packageId,sum(coalesce(rm.sanction_length,0.0)) over(partition by gm.package_id) as sanctionedLength, " +
                "sum(cLength.completedLength) over(partition by gm.package_id) as completedRoadLength, gm.dist_id as distId " +
                "from (select distinct package_id,road_id,dist_id,state_id from rdvts_oltp.geo_mapping where is_active=true) as gm " +
                "left join rdvts_oltp.road_m as rm on rm.id=gm.road_id " +
                "left join (select distinct road_id,coalesce(completed_road_length,0.0) as completedLength from rdvts_oltp.geo_mapping) as cLength on cLength.road_id=gm.road_id " +
                "where gm.state_id=1 ";
        if(distId !=null && distId>0){
            qry += " and gm.dist_id=:distId ";
            sqlParam.addValue("distId", distId);
        }
        if(packageId !=null && packageId>0){
            qry += " and gm.package_id=:packageId ";
            sqlParam.addValue("packageId", packageId);
        }
        qry +=" order by gm.package_id) as rdata " +
                "left join rdvts_oltp.package_m as pm on pm.id=rdata.packageId";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(RoadLengthDto.class));
    }

    public Integer saveVehicleTypeWiseHourlyReport() {
        truncateHourlyReport();
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "insert into rdvts_oltp.hourly_report(imei,dates,hours) " +
                "select vd.imei,date_time::date,max(date_time)-min(date_time) as totalhours " +
                "from (select distinct imei,date_time,longitude,latitude " +
                "from rdvts_oltp.vtu_location where gps_fix::integer=1 and ignition::integer=1 and date_time >= current_date - interval '30 days')as ld " +
                "join " +
                "(select distinct vdm.vehicle_id,vdm.device_id,dm.imei_no_1 as imei, vdm.is_active,case when vdm.deactivation_date is null then current_date else vdm.deactivation_date end as deactivation_date " +
                ",rm.geom roadGeom " +
                "from rdvts_oltp.vehicle_device_mapping as vdm " +
                "left join rdvts_oltp.vehicle_activity_mapping as vam on vam.vehicle_id=vdm.vehicle_id " +
                "left join rdvts_oltp.geo_mapping as gm on gm.id=vam.geo_mapping_id " +
                "left join rdvts_oltp.road_m as rm on rm.id=gm.road_id and rm.geom is not null " +
                "left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id " +
                "where " +
                "(case when vdm.deactivation_date is null then current_date else vdm.deactivation_date end) >= current_date - interval '30 days') as vd " +
                "on ld.imei=vd.imei and ST_Intersects(ST_SetSRID(ST_Point(ld.longitude::numeric,ld.latitude::numeric),4326), ST_Buffer(vd.roadGeom, 50, 'endcap=square join=round'))-- and date_time between data1.start_date and data1.end_date " +
                "group by vd.imei,date_time::date order by vd.imei,date_time::date ";

        return namedJdbc.update(qry,sqlParam);
    }
    public  Integer truncateHourlyReport(){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "TRUNCATE TABLE rdvts_oltp.hourly_report";
        return namedJdbc.update(qry,sqlParam);
    }


    public List<HourlyReportDto> getHourlyReportVehicleTypeWise() {

        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " select distinct hr.dates,vt.name,sum(hr.hours) as hours " +
                "from rdvts_oltp.hourly_report as hr " +
                "left join rdvts_oltp.device_m as dm on dm.imei_no_1=hr.imei " +
                "left join rdvts_oltp.vehicle_device_mapping as vdm on dm.id=vdm.device_id " +
                "left join rdvts_oltp.vehicle_m as vm on vm.id=vdm.vehicle_id " +
                "left join rdvts_oltp.vehicle_type as vt on vt.id=vm.vehicle_type_id " +
                "where (case when vdm.deactivation_date is null then current_date else vdm.deactivation_date end) >= current_date - interval '30 days' " +
                "group by hr.dates,vt.name " +
                "order by vt.name, hr.dates ";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(HourlyReportDto.class));

    }
}
