package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import io.swagger.models.auth.In;
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
        String qry = "select distinct work_id from rdvts_oltp.geo_master where block_id in(:blockId)";
        sqlParam.addValue("blockId",blockId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }


    public Page<WorkDto> getWorkList(WorkDto workDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        if (workDto != null) {
            pageable = PageRequest.of(workDto.getOffSet(), workDto.getLimit(), Sort.Direction.fromString("desc"), "id");
            order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        }
        int resultCount = 0;
        String qry = "select wm.id, wm.g_work_id as workId,wm.g_work_name as workName,wm.is_active,wm.created_by,wm.updated_by,wm.created_on,wm.updated_on, gm.piu_id, piu.name as piuName, gcm.package_id,gcm.package_name" +
                " from rdvts_oltp.work_m as wm" +
                " join rdvts_oltp.geo_master as gm on gm.work_id=wm.id " +
                " join rdvts_oltp.geo_construction_m as gcm on gcm.geo_master_id=gm.id" +
                " join rdvts_oltp.piu_id as piu on piu.id=gm.piu_id"+
                " where wm.is_active = true";
        if (workDto.getId() > 0) {
            qry += " AND wm.id = :id";
            sqlParam.addValue("id", workDto.getId());
        }

//        UserInfoDto user=userRepositoryImpl.getUserByUserId(workDto.getUserId());
//        if(user.getUserLevelId()==5){
//            qry+="and owner.contractor_id=:contractorId ";
//            sqlParam.addValue("contractorId",workDto.getUserId());
//        }
//        else if(user.getUserLevelId()==2){
//            List<Integer> distId=userRepositoryImpl.getDistIdByUserId(workDto.getUserId());
//            List<Integer> workIdByDistId = getWorkIdByDistId(distId);
//            qry+=" and wm.id in (:workIdList)";
//            sqlParam.addValue("workIdList",workIdByDistId);
//        }
//        else if(user.getUserLevelId()==3){
//            List<Integer> blockId=userRepositoryImpl.getBlockIdByUserId(workDto.getUserId());
//            List<Integer> workIdByBlockId = getWorkIdByBlockId(blockId);
//            qry+=" and wm.id in (:workIdList)";
//            sqlParam.addValue("workIdList",workIdByBlockId);
//        }


        resultCount = count(qry, sqlParam);
        if (workDto.getLimit() > 0) {
            qry += " LIMIT " + workDto.getLimit() + " OFFSET " + workDto.getOffSet();
        }

        List<WorkDto> workDtoList = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkDto.class));
        return new PageImpl<>(workDtoList, pageable, resultCount);
    }


    public List<WorkDto> getWorkById(int id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        if (id == -1) {
            qry += "select wm.id, wm.g_work_id as workId,wm.g_work_name as workName,wm.is_active,wm.created_by,wm.updated_by,wm.created_on,wm.updated_on, gm.piu_id, piu.name as piuName, gcm.package_id,gcm.package_name\n" +
                    "from rdvts_oltp.work_m as wm\n" +
                    "join rdvts_oltp.geo_master as gm on gm.work_id=wm.id \n" +
                    "join rdvts_oltp.geo_construction_m as gcm on gcm.geo_master_id=gm.id\n" +
                    "join rdvts_oltp.piu_id as piu on piu.id=gm.piu_id\n" +
                    "where wm.is_active = true  ";
            /* " WHERE true AND id>1 Order BY id";*/
        } else {
            qry += "select wm.id, wm.g_work_id as workId,wm.g_work_name as workName,wm.is_active,wm.created_by,wm.updated_by,wm.created_on,wm.updated_on, gm.piu_id, piu.name as piuName, gcm.package_id,gcm.package_name\n" +
                    "from rdvts_oltp.work_m as wm\n" +
                    "join rdvts_oltp.geo_master as gm on gm.work_id=wm.id \n" +
                    "join rdvts_oltp.geo_construction_m as gcm on gcm.geo_master_id=gm.id\n" +
                    "join rdvts_oltp.piu_id as piu on piu.id=gm.piu_id\n" +
                    "where wm.is_active = true and wm.id = :id ";
            /*   "AND id>1 ORDER BY id";*/
            sqlParam.addValue("id", id);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(WorkDto.class));
    }

    public List<VehicleWorkMappingDto> getVehicleBywork(List<Integer> workIds){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, vehicle_id, work_id, start_time, end_time, start_date, end_date, is_active, created_by, created_on, updated_by, updated_on \n" +
                " FROM rdvts_oltp.vehicle_work_mapping where work_id IN(:workIds) ";
        /*   "AND id>1 ORDER BY id";*/
        sqlParam.addValue("workIds", workIds);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleWorkMappingDto.class));

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
}
