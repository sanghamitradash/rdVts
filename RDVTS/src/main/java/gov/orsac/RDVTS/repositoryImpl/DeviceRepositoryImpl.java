package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import gov.orsac.RDVTS.repository.DeviceMasterRepository;
import gov.orsac.RDVTS.service.HelperService;
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

import java.util.List;

@Repository
public class DeviceRepositoryImpl implements DeviceMasterRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    @Autowired
    private HelperService helperService;


    public int count(String qryStr, MapSqlParameterSource sqlParam) {
        String sqlStr = "SELECT COUNT(*) from (" + qryStr + ") as t";
        Integer intRes = namedJdbc.queryForObject(sqlStr, sqlParam, Integer.class);
        if (null != intRes) {
            return intRes;
        }
        return 0;
    }

    public List<DeviceDto> getDeviceByIds(List<Integer> deviceId, Integer userId) {
        List<DeviceDto> device;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT dm.id,dm.imei_no_1 as imeiNo1,dm.sim_icc_id_1 as simIccId1,dm.mobile_number_1 as mobileNumber1,dm.imei_no_2 as imeiNo2,dm.sim_icc_id_2 as simIccId2, " +
                "dm.mobile_number_2 as mobileNumber2,dm.model_name as modelName,dm.vtu_vendor_id as vtuVendorId,dm.device_no as deviceNo,  " +
                "vtu.vtu_vendor_name as vtuVendorName,vtu.vtu_vendor_address as vendorAddress,  " +
                "vtu.vtu_vendor_phone as vendorPhone, vtu.customer_care_number as customerCareNumber, " +
                "dm.created_by,dm.created_on,dm.updated_by,dm.updated_on  " +
                "from rdvts_oltp.device_m as dm   " +
                "left join rdvts_oltp.vtu_vendor_m as vtu on vtu.id = dm.vtu_vendor_id  " +
                "WHERE dm.is_active = true  ";

        if (deviceId.size() > 0) {
            qry += " AND dm.id IN (:deviceId)";
        }
        sqlParam.addValue("deviceId", deviceId);
//        sqlParam.addValue("userId",userId);
        try {
            device = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return device;
    }

    public List<DeviceDto> getDeviceById(Integer deviceId, Integer userId) {
        List<DeviceDto> device;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT dm.id,dm.imei_no_1 as imeiNo1,dm.sim_icc_id_1 as simIccId1,dm.mobile_number_1 as mobileNumber1,dm.imei_no_2 as imeiNo2,dm.sim_icc_id_2 as simIccId2,  " +
                "dm.mobile_number_2 as mobileNumber2,dm.model_name as modelName,dm.vtu_vendor_id as vtuVendorId,dm.device_no as deviceNo,dm.user_level_id,um.name as userLevelName,     " +
                "vtu.vtu_vendor_name as vtuVendorName,vtu.vtu_vendor_address as vendorAddress,    " +
                "vtu.vtu_vendor_phone as vendorPhone, vtu.customer_care_number as customerCareNumber,  " +
                "dm.created_by,dm.created_on,dm.updated_by,dm.updated_on   " +
                "from rdvts_oltp.device_m as dm  " +
                "left join rdvts_oltp.vtu_vendor_m as vtu on vtu.id = dm.vtu_vendor_id  " +
                "left join rdvts_oltp.user_level_m as um on um.id = dm.user_level_id  " +
                "WHERE dm.is_active = true ";

        if (deviceId > 0) {
            qry += " AND dm.id=:deviceId";
        }
        sqlParam.addValue("deviceId", deviceId);
        sqlParam.addValue("userId", userId);
        try {
            device = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return device;
    }


    public List<DeviceAreaMappingDto> getDeviceAreaByDeviceId(Integer deviceId, Integer userId) {
        List<DeviceAreaMappingDto> device;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT dam.id, dam.device_id,dam.block_id,block.block_name as blockName, dam.dist_id, dist.district_name as distName,dam.state_id,state.state_name as stateName,  " +
                "dam.division_id,div.division_name as divName, dam.g_dist_id,geoDist.g_district_name as gdistName, dam.g_block_id,geoBlock.g_block_name as gblockName,   " +
                "dam.created_by,dam.created_on,dam.updated_by,dam.updated_on   " +
                "from rdvts_oltp.device_area_mapping as dam  " +
                "left join rdvts_oltp.district_boundary as dist on dist.dist_id=dam.dist_id   " +
                "left join rdvts_oltp.block_boundary as block on block.block_id = dam.block_id  " +
                "left join rdvts_oltp.division_m as div on div.id =dam.division_id  " +
                "left join rdvts_oltp.state_m as state on state.id = dam.state_id  " +
                "left join rdvts_oltp.geo_block_m as geoBlock on geoBlock.id = dam.g_block_id   " +
                "left join rdvts_oltp.geo_district_m as geoDist on geoDist.id =  dam.g_dist_id  " +
                "WHERE dam.is_active = true ";

        if (deviceId > 0) {
            qry += " AND dam.device_id=:deviceId";
        }
        sqlParam.addValue("deviceId", deviceId);
        sqlParam.addValue("userId", userId);
        try {
            device = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceAreaMappingDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return device;
    }

    public Boolean deactivateDeviceArea(Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.device_area_mapping SET is_active = false WHERE device_id=:id";
        sqlParam.addValue("id", id);
        Integer update = namedJdbc.update(qry, sqlParam);
        return update > 0;
    }

    public Page<DeviceInfo> getDeviceList(DeviceListDto deviceDto) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        PageRequest pageable = null;
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        pageable = PageRequest.of(deviceDto.getDraw() - 1, deviceDto.getLimit(), Sort.Direction.fromString("desc"), "id");

        order = !pageable.getSort().isEmpty() ? pageable.getSort().toList().get(0) : new Sort.Order(Sort.Direction.DESC, "id");
        int resultCount = 0;


        String qry = "SELECT dm.id,dm.imei_no_1 as imeiNo1,dm.sim_icc_id_1 as simIccId1,dm.mobile_number_1 as mobileNumber1,dm.imei_no_2 as imeiNo2,dm.sim_icc_id_2 as simIccId2,   " +
                "dm.mobile_number_2 as mobileNumber2,dm.model_name as modelName,dm.vtu_vendor_id as vtuVendorId,dm.device_no,  " +
                "vtu.vtu_vendor_name as vtuVendorName,vtu.vtu_vendor_address as vendorAddress,     " +
                "vtu.vtu_vendor_phone as vendorPhone, vtu.customer_care_number as customerCareNumber,    " +
                "dam.device_id,dam.block_id,block.block_name as blockName, dam.dist_id, block.district_name as distName,   " +
                "dam.division_id, dam.g_dist_id,geoDist.g_district_name as gdistName, dam.g_block_id,geoBlock.g_block_name as gblockName,vdm.vehicle_id,  " +
                "vm.vehicle_no  " +
                "from rdvts_oltp.device_m as dm   " +
                "left join rdvts_oltp.vtu_vendor_m as vtu on vtu.id = dm.vtu_vendor_id   " +
                "left join rdvts_oltp.device_area_mapping as dam on dam.device_id = dm.id    " +
                "left join rdvts_oltp.vehicle_device_mapping as vdm on vdm.device_id = dm.id AND vdm.is_active = true   " +
                "left join rdvts_oltp.vehicle_m as vm on vm.id = vdm.vehicle_id  " +
                "left join rdvts_oltp.block_boundary as block on block.block_id = dam.block_id    " +
                "left join rdvts_oltp.geo_block_m as geoBlock on geoBlock.id = dam.g_block_id    " +
                "left join rdvts_oltp.geo_district_m as geoDist on geoDist.id =  dam.g_dist_id  " +
                "WHERE dm.is_active = true AND dam.is_active = true   ";


        if (deviceDto.getImeiNo1() != null && deviceDto.getImeiNo1() > 0) {
            qry += " AND dm.imei_no_1=:imeiNo1 ";
            sqlParam.addValue("imeiNo1", deviceDto.getImeiNo1());
        }

        if (deviceDto.getImeiNo2() != null && deviceDto.getImeiNo2() > 0) {
            qry += " AND dm.imei_no_2=:imeiNo2 ";
            sqlParam.addValue("imeiNo2", deviceDto.getImeiNo2());
        }

        if (deviceDto.getSimIccId1() != null && deviceDto.getSimIccId1().length() > 0) {
            qry += " AND dm.sim_icc_id_1=:simIccId1 ";
            if (deviceDto.getSimIccId1() != null) {
                qry += " AND dm.sim_icc_id_1 LIKE(:simIccId1) ";
                sqlParam.addValue("simIccId1", deviceDto.getSimIccId1());
            }
        }

            if (deviceDto.getSimIccId2() != null && deviceDto.getSimIccId2().length() > 0) {
                qry += " AND dm.sim_icc_id_2=:simIccId2 ";
                if (deviceDto.getSimIccId2() != null) {
                    qry += " AND dm.sim_icc_id_2 LIKE(:simIccId2) ";
                    sqlParam.addValue("simIccId2", deviceDto.getSimIccId2());
                }
            }

             if (deviceDto.getMobileNumber1() != null && deviceDto.getMobileNumber1() > 0) {
                    qry += " AND dm.mobile_number_1=:mobileNumber1 ";
                    sqlParam.addValue("mobileNumber1", deviceDto.getMobileNumber1());
                }

                if (deviceDto.getMobileNumber2() != null && deviceDto.getMobileNumber2() > 0) {
                    qry += " AND dm.mobile_number_2=:mobileNumber2 ";
                    sqlParam.addValue("mobileNumber2", deviceDto.getMobileNumber2());
                }

                if ((deviceDto.getVtuVendorId() != null && deviceDto.getVtuVendorId() > 0)) {
                    qry += " AND vtu.id=:vtuVendorId ";
                    sqlParam.addValue("vtuVendorId", deviceDto.getVtuVendorId());
                }

        if ((deviceDto.getVehicleId() != null && deviceDto.getVehicleId() > 0)) {
            qry += " AND vm.id=:vehicleId ";
            sqlParam.addValue("vehicleId", deviceDto.getVehicleId());
        }

                if (deviceDto.getBlockId() != null && deviceDto.getBlockId() > 0) {
                    qry += " AND dam.block_id=:blockId ";
                    sqlParam.addValue("blockId", deviceDto.getBlockId());
                }

                if (deviceDto.getDistId() != null && deviceDto.getDistId() > 0) {
                    qry += " AND dam.dist_id=:distId ";
                    sqlParam.addValue("distId", deviceDto.getDistId());
                }

                if (deviceDto.getGBlockId() != null && deviceDto.getGBlockId() > 0) {
                    qry += " AND dam.g_block_id=:gBlockId ";
                    sqlParam.addValue("gBlockId", deviceDto.getGBlockId());
                }

                // qry += " ORDER BY dm.  " + order.getProperty() + " " + order.getDirection().name();


        resultCount = count(qry, sqlParam);
        if (deviceDto.getLimit() > 0) {
            qry += " Order by dm.id desc LIMIT " + deviceDto.getLimit() + " OFFSET " + deviceDto.getOffSet();
        }

        List<DeviceInfo> list = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceInfo.class));
        return new PageImpl<>(list, pageable, resultCount);
        }

    public boolean getDeviceAssignedOrNot(Integer deviceId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        Integer count=0;
        boolean device=false;
        String qry = "select count(id)  from  rdvts_oltp.vehicle_device_mapping  " +
                "where device_id=:deviceId and is_active=true and   " +
                "deactivation_date is null and created_on <=now()  ";
        sqlParam.addValue("deviceId", deviceId);
        count=  namedJdbc.queryForObject(qry, sqlParam,Integer.class);
        if(count>0){
            device=true;
        }
        return device;
    }




    @Override
    public List<DeviceDto> getUnassignedDeviceData(Integer userId) {
        //Integer userLevelId = helperService.getUserLevelByUserId(userId);
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String query = " ";
        // if(userLevelId == 2)
        query = "SELECT dm.id,dm.imei_no_1 as imeiNo1,dm.sim_icc_id_1 as simIccId1,dm.mobile_number_1 as mobileNumber1,dm.model_name as modelName,dm.device_no as deviceNo from rdvts_oltp.device_m as dm where id not in  " +
                "(select distinct device_id from rdvts_oltp.vehicle_device_mapping as vdm where is_active=true) order by id  ";
        return namedJdbc.query(query, sqlParam, new BeanPropertyRowMapper<>(DeviceDto.class));
    }

    @Override
    public List<VehicleDeviceMappingDto> getVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String query = "SELECT dv.id, dv.device_id,dv.vehicle_id,vm.vehicle_no, installation_date as installationDate ,dv.installed_by,dv.is_active as active,dv.created_by,dv.created_on,  " +
                "dv.updated_by,dv.updated_on from rdvts_oltp.vehicle_device_mapping as dv   " +
                "left join rdvts_oltp.vehicle_m as vm on vm.id = dv.vehicle_id WHERE dv.device_id=:deviceId AND dv.is_active=true   ";
        sqlParam.addValue("deviceId", deviceId);
        sqlParam.addValue("userId", userId);
        return namedJdbc.query(query, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
    }

    @Override
    public List<userLevelDto> getDeviceUserLevel() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = " SELECT um.id, um.name, um.is_active as isactive,um.created_by,um.created_on,um.updated_by,um.updated_on FROM rdvts_oltp.user_level_m as um where id != 5 ";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(userLevelDto.class));
    }

    @Override
    public DeviceAreaMappingDto getStateByDistId(Integer distId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT dist.state_id,state.state_name from rdvts_oltp.district_boundary as dist  " +
                "left join rdvts_oltp.state_m  as state on state.id = dist.state_id WHERE dist.dist_id =:distId ";
        sqlParam.addValue("distId", distId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceAreaMappingDto.class));
    }

    @Override
    public DeviceAreaMappingDto getStateDistByBlockId(Integer blockId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT block.dist_id, block.district_name,dist.state_id,state.state_name from rdvts_oltp.block_boundary as block " +
                "left join rdvts_oltp.district_boundary as dist on dist.dist_id = block.dist_id  " +
                "left join rdvts_oltp.state_m as state on state.id = dist.state_id  " +
                "WHERE block.gid =:blockId";
        sqlParam.addValue("blockId", blockId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceAreaMappingDto.class));
    }

    @Override
    public DeviceAreaMappingDto getStateDistByDivisionId(Integer divisionId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT div.dist_id,dist.district_name,dist.state_id,state.state_name  from rdvts_oltp.division_m as div  " +
                "left join rdvts_oltp.district_boundary as dist on dist.dist_id= div.dist_id  " +
                "left join rdvts_oltp.state_m as state on state.id= dist.state_id  " +
                "WHERE  div.id =:divisionId ";
        sqlParam.addValue("divisionId", divisionId);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceAreaMappingDto.class));
    }

    public List<DeviceDto> getImeiListByDeviceId(Integer deviceId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT dm.id,dm.imei_no_1 as imeiNo1,dm.sim_icc_id_1 as simIccId1,dm.mobile_number_1 as mobileNumber1,dm.imei_no_2 as imeiNo2,dm.sim_icc_id_2 as simIccId2, " +
                " dm.mobile_number_2 as mobileNumber2,dm.model_name as modelName,dm.vtu_vendor_id as vtuVendorId,dm.device_no as deviceNo ,  " +
                " dm.created_by,dm.created_on,dm.updated_by,dm.updated_on   from rdvts_oltp.device_m as dm WHERE dm.is_active = true  ";
        if (deviceId > 0) {
            qry += " and dm.id=:deviceId ";
            sqlParam.addValue("deviceId", deviceId);
        }


        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceDto.class));
    }

    public DeviceDto getDeviceByIdForTracking(Integer deviceId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT dm.id,dm.imei_no_1 as imeiNo1,dm.sim_icc_id_1 as simIccId1,dm.mobile_number_1 as mobileNumber1,dm.imei_no_2 as imeiNo2,dm.sim_icc_id_2 as simIccId2, " +
                " dm.mobile_number_2 as mobileNumber2,dm.model_name as modelName,dm.vtu_vendor_id as vtuVendorId,dm.device_no as deviceNo ,  " +
                " dm.created_by,dm.created_on,dm.updated_by,dm.updated_on  from rdvts_oltp.device_m as dm WHERE dm.is_active = true and dm.id=:deviceId ";
        sqlParam.addValue("deviceId", deviceId);

        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(DeviceDto.class));
    }

    public List<VTUVendorMasterDto> getVtuVendorDropDown() {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT vm.id,vm.vtu_vendor_name,vm.vtu_vendor_address,vm.vtu_vendor_phone,vm.customer_care_number from rdvts_oltp.vtu_vendor_m as vm ";
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VTUVendorMasterDto.class));
    }

    public Boolean deactivateDeviceVehicle(Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.vehicle_device_mapping SET is_active = false WHERE device_id=:id";
        sqlParam.addValue("id", id);
        Integer update = namedJdbc.update(qry, sqlParam);
        return update > 0;
    }

    public Boolean deactivateDevice(Integer deviceId) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "UPDATE rdvts_oltp.device_m  " +
                "SET is_active=false WHERE id=:deviceId";
        sqlParam.addValue("deviceId", deviceId);

        int update = namedJdbc.update(qry, sqlParam);
        boolean result = false;
        if (update > 0) {
            result = true;
        }
        return result;
    }

    public List<VehicleDeviceMappingDto> getAllVehicleDeviceMappingByDeviceId(Integer deviceId, Integer userId) {
        List<VehicleDeviceMappingDto> vehicleDevice;
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT vdm.id, vdm.vehicle_id,vm.vehicle_no,vm.vehicle_type_id,type.name as typeName,vm.model,vm.chassis_no,  " +
                "vm.engine_no,vdm.device_id, vdm.installation_date, vdm.installed_by, vdm.is_active, vdm.created_by, vdm.created_on,  " +
                "vdm.updated_by, vdm.updated_on, vdm.deactivation_date  " +
                "FROM rdvts_oltp.vehicle_device_mapping as vdm   " +
                "left join rdvts_oltp.vehicle_m as vm on vm.id =vdm.vehicle_id  " +
                "left join rdvts_oltp.vehicle_type as type on type.id =vm.vehicle_type_id  " ;
               // "WHERE vdm.device_id=:deviceId  ";

        if (deviceId > 0) {
            qry += " WHERE vdm.device_id=:deviceId";
        }
        sqlParam.addValue("deviceId", deviceId);
        sqlParam.addValue("userId", userId);
        try {
            vehicleDevice = namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VehicleDeviceMappingDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return vehicleDevice;
    }
    }





