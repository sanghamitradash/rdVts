package gov.orsac.RDVTS.repositoryImpl;


import gov.orsac.RDVTS.dto.AlertDto;
import gov.orsac.RDVTS.dto.ResponseDto;
import gov.orsac.RDVTS.entities.ContractorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class CronRepositoryImpl {

    @Autowired
        private NamedParameterJdbcTemplate namedJdbc;

    public Boolean checkExists(String checkField,String table_name,String fieldName){
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = " select case when count(*) >0 then true else false  end as bool FROM rdvts_oltp."+table_name+" WHERE "+fieldName+"=:checkField " ;
        sqlParam.addValue("checkField",checkField);
        return namedJdbc.queryForObject(qry, sqlParam, (Boolean.class));
    }

    public ResponseDto insertToGeoConstructionM(ResponseDto responseDto){
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = " INSERT INTO rdvts_oltp.geo_construction_m( " +
                "  package_name, road_name, road_length,  completed_road_length, sanction_date, road_code) " +
                " VALUES ( :package_name,:road_name, :road_length,:completed_road_length,:sanction_date, :road_code); " ;
        sqlParam.addValue("package_name",responseDto.getPackageNo());
        sqlParam.addValue("road_name",responseDto.getRoadName());
        sqlParam.addValue("road_length",responseDto.getSanctionLength());
        sqlParam.addValue("completed_road_length",responseDto.getCompletedRoadLength());
        sqlParam.addValue("sanction_date",responseDto.getSanctionDate());
        sqlParam.addValue("road_code",responseDto.getRoadCode());
         namedJdbc.update(qry, sqlParam);
         return null;
    }

    public ResponseDto updateGeoConstructionM(ResponseDto responseDto) throws ParseException {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(responseDto.getSanctionDate());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String parsedDate = formatter.format(initDate);
        Date sanctionDate=new SimpleDateFormat("yyyy-MM-dd").parse(parsedDate);

        String qry = "UPDATE rdvts_oltp.geo_construction_m " +
                " SET  road_name = :road_name, road_length =:road_length,  completed_road_length =:completed_road_length, sanction_date =:sanction_date, road_code=:road_code " +
                " WHERE package_name=:package_name" ;
        sqlParam.addValue("package_name",responseDto.getPackageNo());
        sqlParam.addValue("road_name",responseDto.getRoadName());
        sqlParam.addValue("road_length",responseDto.getSanctionLength());
        sqlParam.addValue("completed_road_length",responseDto.getCompletedRoadLength());
        sqlParam.addValue("sanction_date",  sanctionDate);
        sqlParam.addValue("road_code",responseDto.getRoadCode());
        namedJdbc.update(qry, sqlParam);
        return null;
    }

    public boolean checkContractorExists(String contractorName) {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
        String qry = "\tselect case when count(*) >0 then true else false  end as bool FROM rdvts_oltp.contractor_m WHERE LOWER(name) LIKE LOWER(:contractorName)" ;
        sqlParam.addValue("contractorName",contractorName);
        return namedJdbc.queryForObject(qry, sqlParam, (Boolean.class));

    }

    public void updateWorkM(ResponseDto responseDto) throws ParseException {
        MapSqlParameterSource sqlParam=new MapSqlParameterSource();

        Date awardDate=responseDto.getAwardDate()=="" ?  null : convertDateFormat(responseDto.getAwardDate());
        Date completionDate=responseDto.getCompletionDate()=="" ?  null : convertDateFormat(responseDto.getCompletionDate()) ;
        Date pMisFinalizeDate=responseDto.getPMisFinalizeDate() =="" ? null : convertDateFormat(responseDto.getPMisFinalizeDate());

        String qry = "UPDATE rdvts_oltp.work_m " +
                " SET  g_work_name = :g_work_name, award_date =:award_date,  completion_date =:completion_date, pmis_finalize_date =:pmis_finalize_date " +
                " WHERE package_name=:package_name" ;
        sqlParam.addValue("package_name",responseDto.getPackageNo());
        sqlParam.addValue("award_date",awardDate);
        sqlParam.addValue("completion_date",completionDate);
        sqlParam.addValue("pmis_finalize_date",pMisFinalizeDate);
        sqlParam.addValue("g_work_name",responseDto.getRoadName());

        namedJdbc.update(qry, sqlParam);

    }


    public Date convertDateFormat(String mydate) throws ParseException {
        Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(mydate);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String parsedDate = formatter.format(initDate);
        Date finalDate = new SimpleDateFormat("yyyy-MM-dd").parse(parsedDate);
        return finalDate;
    }

//    public void updateContractorM(String contractorName) {
//        MapSqlParameterSource sqlParam=new MapSqlParameterSource();
//        String qry = "UPDATE rdvts_oltp.contractor_m " +
//                " SET  name=:name " +
//                " WHERE name=:name" ;
//        sqlParam.addValue("name",contractorName);
//    }
}
