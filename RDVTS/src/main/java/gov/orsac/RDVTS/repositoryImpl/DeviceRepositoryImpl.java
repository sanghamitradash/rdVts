package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.ContractorDto;
import gov.orsac.RDVTS.dto.DeviceAreaMappingDto;
import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.DeviceListDto;
import gov.orsac.RDVTS.entities.DeviceMappingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class DeviceRepositoryImpl {

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

    public DeviceDto getDeviceById(Integer deviceId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT dm.id,dm.imei_no_1 as imeiNo1,dm.sim_icc_id_1 as simIccId1,dm.mobile_number_1 as mobileNumber1,dm.imei_no_2 as imeiNo2,dm.sim_icc_id_2 as simIccId2, " +
                "dm.mobile_number_2 as mobileNumber2,dm.model_name as modelName,dm.vtu_vendor_id as vtuVendorId,  " +
                "vtu.vtu_vendor_name as vtuVendorName,vtu.vtu_vendor_address as vendorAddress,  " +
                "vtu.vtu_vendor_phone as vendorPhone, vtu.customer_care_number as customerCareNumber  " +
                "from rdvts_oltp.device_m as dm   " +
                "left join rdvts_oltp.vtu_vendor_m as vtu on vtu.id = dm.vtu_vendor_id  " +
                "WHERE dm.is_active = true AND dm.id =:deviceId";

        sqlParam.addValue("deviceId", deviceId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceDto.class));
    }

    public List<DeviceAreaMappingDto> getDeviceAreaByDeviceId(Integer deviceId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT dam.id, dam.device_id,dam.block_id,block.block_name as blockName, dam.dist_id, block.district_name as distName, " +
                "dam.division_id, dam.g_dist_id,geoDist.g_district_name as gdistName, dam.g_block_id,geoBlock.g_block_name as gblockName  " +
                "from rdvts_oltp.device_area_mapping as dam  " +
                "left join rdvts_oltp.block_boundary as block on block.block_id = dam.block_id " +
                "left join rdvts_oltp.geo_block_m as geoBlock on geoBlock.id = dam.g_block_id  " +
                "left join rdvts_oltp.geo_district_m as geoDist on geoDist.id =  dam.g_dist_id " ;


        if(deviceId >0){
            qry +=" WHERE dam.device_id=:deviceId ";
            sqlParam.addValue("deviceId",deviceId);
        }
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceAreaMappingDto.class));
    }

    public Boolean deactivateDeviceArea(Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.device_area_mapping SET is_active = false WHERE device_id=:id";
        sqlParam.addValue("id", id);
        Integer update = namedJdbc.update(qry, sqlParam);
        return update > 0;
    }

    public Page<DeviceDto> getDeviceList(DeviceListDto deviceDto) {
        deviceDto.setSortOrder("DESC");
        deviceDto.setSortBy("id");
        PageRequest pageable = PageRequest.of(deviceDto.getPage(), deviceDto.getSize(), Sort.Direction.fromString(deviceDto.getSortOrder()), deviceDto.getSortBy());
        Sort.Order order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer  resultCount = 0;
        String queryString = " ";


        queryString = "SELECT dm.id,dm.imei_no_1 as imeiNo1,dm.sim_icc_id_1 as simIccId1,dm.mobile_number_1 as mobileNumber1,dm.imei_no_2 as imeiNo2,dm.sim_icc_id_2 as simIccId2,  " +
                "dm.mobile_number_2 as mobileNumber2,dm.model_name as modelName,dm.vtu_vendor_id as vtuVendorId,  " +
                "vtu.vtu_vendor_name as vtuVendorName,vtu.vtu_vendor_address as vendorAddress,    " +
                "vtu.vtu_vendor_phone as vendorPhone, vtu.customer_care_number as customerCareNumber,  " +
                "dam.device_id,dam.block_id,block.block_name as blockName, dam.dist_id, block.district_name as distName, " +
                "dam.division_id, dam.g_dist_id,geoDist.g_district_name as gdistName, dam.g_block_id,geoBlock.g_block_name as gblockName   " +
                "from rdvts_oltp.device_m as dm   " +
                "left join rdvts_oltp.vtu_vendor_m as vtu on vtu.id = dm.vtu_vendor_id  " +
                "left join rdvts_oltp.device_area_mapping as dam on dam.device_id = dm.id  " +
                "left join rdvts_oltp.block_boundary as block on block.block_id = dam.block_id  " +
                "left join rdvts_oltp.geo_block_m as geoBlock on geoBlock.id = dam.g_block_id  " +
                "left join rdvts_oltp.geo_district_m as geoDist on geoDist.id =  dam.g_dist_id  " +
                "WHERE dm.is_active = true  " ;


        if(deviceDto.getImeiNo1() != null && deviceDto.getImeiNo1() > 0){
            queryString += " AND dm.imei_no_1=:imeiNo1 ";
            sqlParam.addValue("imeiNo1", deviceDto.getImeiNo1());
        }

        if(deviceDto.getImeiNo2() != null && deviceDto.getImeiNo2() > 0){
            queryString += " AND dm.imei_no_2=:imeiNo2 ";
            sqlParam.addValue("imeiNo2", deviceDto.getImeiNo2());
        }

        if(deviceDto.getSimIccId1() != null && deviceDto.getSimIccId1() > 0){
            queryString += " AND dm.sim_icc_id_1=:simIccId1 ";
            sqlParam.addValue("simIccId1", deviceDto.getSimIccId1());
        }

        if(deviceDto.getSimIccId2() != null && deviceDto.getSimIccId2() > 0){
            queryString += " AND dm.sim_icc_id_2=:simIccId2 ";
            sqlParam.addValue("simIccId2", deviceDto.getSimIccId2());
        }

        if(deviceDto.getMobileNumber1() != null && deviceDto.getMobileNumber1() > 0){
            queryString += " AND dm.mobile_number_1=:mobileNumber1 ";
            sqlParam.addValue("mobileNumber1", deviceDto.getMobileNumber1());
        }

        if(deviceDto.getMobileNumber2() != null && deviceDto.getMobileNumber2() > 0){
            queryString += " AND dm.mobile_number_2=:mobileNumber2 ";
            sqlParam.addValue("mobileNumber2", deviceDto.getMobileNumber2());
        }

        if(deviceDto.getVtuVendorId() != null && deviceDto.getVtuVendorId() > 0){
            queryString += " AND dm.vtu_vendor_id=:vtuVendorId ";
            sqlParam.addValue("vtuVendorId", deviceDto.getVtuVendorId());
        }

        if(deviceDto.getBlockId() != null && deviceDto.getBlockId() > 0){
            queryString += " AND dam.block_id=:blockId ";
            sqlParam.addValue("blockId", deviceDto.getBlockId());
        }

        if(deviceDto.getDistId() != null && deviceDto.getDistId() > 0){
            queryString += " AND dam.dist_id=:distId ";
            sqlParam.addValue("distId", deviceDto.getDistId());
        }

        if(deviceDto.getGBlockId() != null && deviceDto.getGBlockId() > 0){
            queryString += " AND dam.g_block_id=:gBlockId ";
            sqlParam.addValue("gBlockId", deviceDto.getGBlockId());
        }


        queryString += " ORDER BY dm. " + order.getProperty() + " " + order.getDirection().name();
        resultCount = count(queryString, sqlParam);
        queryString += " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

        List<DeviceDto> deviceInfo = namedJdbc.query(queryString, sqlParam, new BeanPropertyRowMapper<>(DeviceDto.class));
        return new PageImpl<>(deviceInfo,pageable,resultCount);

    }
    }
