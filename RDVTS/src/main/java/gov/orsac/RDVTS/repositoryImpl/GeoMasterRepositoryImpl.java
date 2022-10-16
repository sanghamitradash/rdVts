package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.GeoMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class GeoMasterRepositoryImpl {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public List<GeoMasterDto> getAllGeoMasterByAllId(int id, int geoWorkId, int geoDistId, int geoBlockId, int geoPiuId, int geoContractorId, int workId, int piuId, int distId, int blockId, int roadId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select id, g_work_id as geoWorkId, g_dist_id as geoDistId, g_block_id as geoBlockId, g_piu_id as geoPiuId, " +
                "g_contractor_id as geoContractorId, work_id as workId, piu_id as piuId, dist_id as distId, block_id as blockId, " +
                "road_id as roadId,is_active,created_by,updated_by,created_on,created_by from rdvts_oltp.geo_master where is_active = true";
        if (id > 0) {
            qry += " and id = :id";
            sqlParam.addValue("id", id);
        }
        if (geoWorkId > 0) {
            qry += "and g_work_id = :geoWorkId";
            sqlParam.addValue("geoWorkId", geoWorkId);
        }
        if (geoDistId > 0) {
            qry += "and g_dist_id = :geoDistId";
            sqlParam.addValue("geoDistId", geoDistId);
        }
        if (geoBlockId > 0) {
            qry += " and g_block_id = :geoBlockId";
            sqlParam.addValue("geoBlockId", geoBlockId);
        }
        if (geoPiuId > 0) {
            qry += " and g_piu_id = :geoPiuId";
            sqlParam.addValue("geoPiuId", geoPiuId);
        }
        if (geoContractorId > 0) {
            qry += " and g_contractor_id = :geoContractorId";
            sqlParam.addValue("geoContractorId", geoContractorId);
        }
        if (workId > 0) {
            qry += " and work_id = :workId";
            sqlParam.addValue("workId", workId);
        }
        if (piuId > 0) {
            qry += " and piu_id = :piuId";
            sqlParam.addValue("piuId", piuId);
        }
        if (distId > 0) {
            qry += " and dist_id = :distId";
            sqlParam.addValue("distId", distId);
        }
        if (blockId > 0) {
            qry += " and block_id = :blockId";
            sqlParam.addValue("blockId", blockId);
        }
        if (roadId > 0) {
            qry += " and road_id = :roadId";
            sqlParam.addValue("roadId", roadId);
        }

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(GeoMasterDto.class));
    }
    public List<Integer> getWorkIdByDistIdList(List<Integer> distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        sqlParam.addValue("distId",distId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }
    public List<Integer> getWorkIdByBlockList(List<Integer> blockId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "";
        sqlParam.addValue("blockId",blockId);
        return namedJdbc.queryForList(qry,sqlParam,Integer.class);
    }

}
