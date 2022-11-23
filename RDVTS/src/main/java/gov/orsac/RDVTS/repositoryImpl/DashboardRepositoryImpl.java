package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.ActiveAndInactiveVehicleDto;
import gov.orsac.RDVTS.dto.DistrictWiseVehicleDto;
import gov.orsac.RDVTS.dto.DivisionWiseVehicleDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.repository.DashboardRepository;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
            String qry = " select count(distinct id) from rdvts_oltp.vehicle_device_mapping  " +
                    "where is_active=true and device_id in(select distinct id from rdvts_oltp.device_m where imei_no_1 in  " +
                    "(select distinct imei from rdvts_oltp.vtu_location where date(date_time)=date(now()))) " ;
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
        String qry = "select distinct id from rdvts_oltp.device_m where imei_no_1 in  " +
                     "(select distinct imei from rdvts_oltp.vtu_location where date(date_time)=date(now()))  ";
        return namedJdbc.queryForList(qry,sqlParam, Integer.class);
    }

    @Override
    public List<Integer> totalInactiveIds() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select distinct id as deviceId from rdvts_oltp.device_m where imei_no_1 not in  " +
                "(select distinct imei from rdvts_oltp.vtu_location where date(date_time)=date(now()))  " +
                "And is_active=true ";
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
                if(ids!=null && ids.size()>0){
                    qry+=" and vdm.device_id in(:deviceIds) AND dam.is_active = true ";
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
        if(ids!=null && ids.size()>0){
            qry+=" and vdm.device_id in(:deviceIds) AND dam.is_active = true ";
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
}
