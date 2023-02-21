package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.entities.DashboardCronEntity;
import gov.orsac.RDVTS.repository.DashboardRepository;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
//        String qry = "select device_id from rdvts_oltp.vehicle_device_mapping    " +
//                "  where is_active=true and device_id in(select distinct id from rdvts_oltp.device_m where imei_no_1 in  " +
//                " (select distinct imei from rdvts_oltp.vtu_location where date_time >=:currentDateTime::timestamp ))    ";
        //Change by SP 21 feb
        String qry = "select device_id from rdvts_oltp.vehicle_device_mapping   " +
                "where is_active=true and device_id in(select distinct id from rdvts_oltp.device_m where imei_no_1 in   " +
                " ( " +
                " " +
                "  select b.imei from  rdvts_oltp.device_m as dm  " +
                "    left join (select distinct imei,max(id) over(partition by imei) as vtuid from " +
                "    rdvts_oltp.vtu_location as vtu where date_time >=:currentDateTime::timestamp  and  gps_fix::numeric =1 ) as a on dm.imei_no_1=a.imei " +
                "    left join rdvts_oltp.vtu_location as b on a.vtuid=b.id " +
                "  " +
                " ))  ";
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

        String qry = " select device_id from rdvts_oltp.vehicle_device_mapping   where is_active=true and device_id \n" +
                " not in(select distinct id from rdvts_oltp.device_m where imei_no_1 in \n" +
                " ( " +
                " select b.imei from  rdvts_oltp.device_m as dm \n" +
                "    left join (select distinct imei,max(id) over(partition by imei) as vtuid from\n" +
                "    rdvts_oltp.vtu_location as vtu where date_time >=:currentDateTime::timestamp  and  gps_fix::numeric =1 ) as a on dm.imei_no_1=a.imei\n" +
                "    left join rdvts_oltp.vtu_location as b on a.vtuid=b.id\n" +

                "  )) ";
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
                        qry+=" and vdm.device_id in(0) AND dam.is_active = true ";
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
                qry+=" and vdm.device_id in(0) AND dam.is_active = true ";
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
        return namedJdbc.queryForObject(qry,sqlParam,new BeanPropertyRowMapper<>(DashboardCronEntity.class));
    }
    public List<DashboardDto> getDistrictWiseDashboardData() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select dashboard.id,dashboard.active as activeCount,dashboard.in_active as inActiveCount,dashboard.area_id as areaId,district.district_name as districtName,process_time as processTime " +
                "from rdvts_oltp.dashboard_cron as dashboard left join rdvts_oltp.district_boundary as district on district.dist_id=dashboard.area_id where dashboard.area_type_id=1";

        return namedJdbc.query(qry,sqlParam,new BeanPropertyRowMapper<>(DashboardDto.class));
    }
    public List<DashboardDto> getDivisionWiseDashboardData() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select dashboard.id,dashboard.active as activeCount,dashboard.in_active as inActiveCount,dashboard.area_id as areaId,division.division_name as divisionName " +
                "from rdvts_oltp.dashboard_cron as dashboard left join rdvts_oltp.division_m as division on division.division_id=dashboard.area_id where dashboard.area_type_id=2";

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

}
