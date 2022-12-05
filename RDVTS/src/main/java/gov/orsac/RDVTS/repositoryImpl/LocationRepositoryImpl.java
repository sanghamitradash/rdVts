package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class LocationRepositoryImpl {

    @Value("${dbschema}")
    private String DBSCHEMA;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;


    public VtuLocationDto getLatestRecordByImeiNumber(Long imei2, Long imei1) {


        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

//        if (device.get(0).getImeiNo1()==null){
//            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
//                    "\tFROM rdvts_oltp.vtu_location where  imei in (:imei1) and is_active=true order by date_time desc  limit 1";
//
//           // sqlParam.addValue("imei1", device.get(0).getImeiNo1());
//            sqlParam.addValue("imei2", device.get(0).getImeiNo2());
//            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        } else if (device.get(0).getImeiNo2()==null) {
//            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
//                    "\tFROM rdvts_oltp.vtu_location where imei=:imei1 and is_active=true  order by date_time desc  limit 1";
//
//            sqlParam.addValue("imei1", device.get(0).getImeiNo1());
//            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
//            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        }
//        else {
        String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                " FROM rdvts_oltp.vtu_location where imei = :imei1 and is_active=true or imei = :imei2 and is_active=true and gps_fix::numeric=1 order by date_time desc  limit 1";

        sqlParam.addValue("imei1", imei1);
        sqlParam.addValue("imei2", imei2);
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        }


    }


    public List<VtuLocationDto> getLocationRecordByDateTime(List<Long> imei2, List<Long> imei1, Date startTime, Date endTime) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        List<Long> imei1=new ArrayList<>();
//        List<Long> imei2=new ArrayList<>();
//        for(DeviceDto d:device){
//            imei1.add(d.getImeiNo1());
//            imei2.add(d.getImeiNo2());
//        }


        if (imei1 == null) {
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where  imei IN(:imei2) and is_active=true and date_time BETWEEN :startTime AND :endTime and gps_fix::numeric=1 order by date_time desc  ";

            // sqlParam.addValue("imei1", device.get(0).getImeiNo1());
            sqlParam.addValue("imei2", imei2);
            sqlParam.addValue("startTime", startTime);
            sqlParam.addValue("endTime", endTime);

            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else if (imei2 == null) {
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where imei IN(:imei1) and is_active=true and date_time BETWEEN :startTime AND :endTime and gps_fix::numeric=1  order by date_time desc";

            sqlParam.addValue("imei1", imei1);
            sqlParam.addValue("startTime", startTime);
            sqlParam.addValue("endTime", endTime);

            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else {
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where imei IN(:imei1) and is_active=true and date_time BETWEEN :startTime AND :endTime  or imei IN(:imei2) and is_active=true and date_time BETWEEN :startTime AND :endTime  order by date_time desc";

            sqlParam.addValue("imei1", imei1);
            sqlParam.addValue("imei2", imei2);
            sqlParam.addValue("startTime", startTime);
            sqlParam.addValue("endTime", endTime);

            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        }


    }

    public List<VtuLocationDto> getLocationRecordByRoadData(List<Long> imei1, List<Long> imei2, List<VehicleWorkMappingDto> vehicleByWork) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        if (device.get(0).getImeiNo1()==null){
//            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
//                    "\tFROM rdvts_oltp.vtu_location where  imei in (:imei1) and is_active=true order by date_time desc  limit 1";
//
//           // sqlParam.addValue("imei1", device.get(0).getImeiNo1());
//            sqlParam.addValue("imei2", device.get(0).getImeiNo2());
//            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        } else if (device.get(0).getImeiNo2()==null) {
//            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
//                    "\tFROM rdvts_oltp.vtu_location where imei=:imei1 and is_active=true  order by date_time desc  limit 1";
//
//            sqlParam.addValue("imei1", device.get(0).getImeiNo1());
//            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
//            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        }
//        else {
        String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                "\tFROM rdvts_oltp.vtu_location where imei IN (:imei1) and is_active=true and date_time BETWEEN :startTime AND :endTime or imei IN (:imei2) and is_active=true and gps_fix::numeric=1 order by date_time desc  limit 1";

        sqlParam.addValue("imei1", imei1);
        sqlParam.addValue("imei2", imei2);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
    }

    public List<VtuLocationDto> getLocationrecordList(Long imei1, Long imei2, Date startDate, Date endDate, Date deviceVehicleCreatedOn, Date deviceVehicleDeactivationDate) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        if (imei1 == null) {
            String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on " +
                    "    FROM rdvts_oltp.vtu_location where is_active=true  ";
            qry += " and imei =:imei2 and gps_fix::numeric =1  ";
            sqlParam.addValue("imei2", imei2);

//            if (deviceVehicleDeactivationDate == null) {
//                deviceVehicleDeactivationDate = new Date();
//            }
//
//            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
//                qry += " AND  date_time BETWEEN :createdOn AND :deactivationDate ";
//                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
//                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
//            }

            if (startDate != null && endDate != null) {
                qry += " and date_time BETWEEN :startDate AND :endDate ";
                sqlParam.addValue("startDate", startDate);
                sqlParam.addValue("endDate", endDate);
            }


            qry += " order by date_time ASC limit 100";


            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else {
            String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on " +
                    " FROM rdvts_oltp.vtu_location where is_active=true ";
            qry += " and imei =:imei1 and gps_fix::numeric =1 ";
            sqlParam.addValue("imei1", imei1);
//            if (deviceVehicleDeactivationDate == null) {
//                deviceVehicleDeactivationDate = new Date();
//            }

//            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
//                qry += " AND  date_time BETWEEN :createdOn AND :deactivationDate ";
//                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
//                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
//            }

            if (startDate != null && endDate != null) {
                qry += " and date_time BETWEEN :startDate AND :endDate ";
                sqlParam.addValue("startDate", startDate);
                sqlParam.addValue("endDate", endDate);
            }


            qry += " order by date_time ASC";



            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        }
//        else {
//            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
//                    " FROM rdvts_oltp.vtu_location where imei =:imei1 and is_active=true  or imei =:imei2 and is_active=true  order by date_time desc";
//
//            if (startDate !=null && endDate !=null){
//                qry+="and date_time BETWEEN :startDate AND :endDate ";
//                sqlParam.addValue("startDate", startDate);
//                sqlParam.addValue("endDate", endDate);
//            }
//                qry+=" and imei =:imei2  order by date_time desc ";
//            sqlParam.addValue("imei1", imei1);
//            sqlParam.addValue("imei1", imei1);
//            sqlParam.addValue("imei2",imei2);
//
//            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        }
    }

    public List<VtuLocationDto> getLastLocationrecordList(Long imei1, Long imei2, Date startDate, Date endDate, Date deviceVehicleCreatedOn, Date deviceVehicleDeactivationDate) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        if (imei1 == null) {
            String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    " FROM rdvts_oltp.vtu_location where  is_active=true ";
            qry += " and imei =:imei2 and gps_fix::numeric =1 ";

            sqlParam.addValue("imei2", imei2);

//            if (deviceVehicleDeactivationDate == null) {
//                deviceVehicleDeactivationDate = new Date();
//            }

//            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
//                qry += " AND date_time BETWEEN :createdOn AND :deactivationDate ";
//                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
//                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
//            }

            Date currentDateMinus = new Date(System.currentTimeMillis() - 300 * 1000); //5 minute in millisecond 60*5
            Date currentDate = new Date();
            qry += " and date_time BETWEEN :currentDateMinus AND :currentDate  order by date_time desc LIMIT 1";
            sqlParam.addValue("currentDateMinus", currentDateMinus);
            sqlParam.addValue("currentDate", currentDate);

            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else {
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where is_active=true   ";

            qry += " and imei =:imei1 and gps_fix::numeric =1 ";
            sqlParam.addValue("imei1", imei1);

//            if (deviceVehicleDeactivationDate == null) {
//                deviceVehicleDeactivationDate = new Date();
//            }
//
//            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
//                qry += " AND date_time BETWEEN :createdOn AND :deactivationDate ";
//                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
//                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
//            }
//            qry += " and date(date_time)=date(now()) order by date_time desc LIMIT 1";
            Date currentDateMinus = new Date(System.currentTimeMillis() - 300 * 1000); //15 minute in millisecond 60*15
            Date currentDate = new Date();
            qry += " and date_time BETWEEN :currentDateMinus AND :currentDate  order by date_time desc LIMIT 1";
            sqlParam.addValue("currentDateMinus", currentDateMinus);
            sqlParam.addValue("currentDate", currentDate);



            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        }

    }


    public List<VtuLocationDto> getLastLocationRecordList(List<VehicleDeviceMappingDto> deviceDetails, Date startDate, Date endDate) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        List<Long> imeiList = new ArrayList<>();
        for (VehicleDeviceMappingDto item : deviceDetails) {
            imeiList.add(item.getImeiNo1());
        }


        String qry = "select b.*,vdm.vehicle_id as vehicleId from rdvts_oltp.vehicle_device_mapping as vdm"+
        " left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id"+
        " left join (select distinct imei,max(id) over(partition by imei) as vtuid from"+
        " rdvts_oltp.vtu_location as vtu where date(date_time)=date(now()) and  gps_fix::numeric =1  and imei in (:imei2) ) as a on dm.imei_no_1=a.imei"+
        " left join rdvts_oltp.vtu_location as b on a.vtuid=b.id"+
        " where vdm.is_active=true and b.id is not null and b.gps_fix::numeric =1 order by b.date_time desc";

        sqlParam.addValue("imei2", imeiList);




//            if (deviceVehicleDeactivationDate == null) {
//                deviceVehicleDeactivationDate = new Date();
//            }

//            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
//                qry += " AND date_time BETWEEN :createdOn AND :deactivationDate ";
//                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
//                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
//            }

//        qry += " and date(date_time)=date(now()) order by date_time";

        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));



//        if (imei1 == null) {
//            String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
//                    " FROM rdvts_oltp.vtu_location where  is_active=true ";
//            qry += " and imei in (:imei2) and gps_fix::numeric =1 ";
//
//            sqlParam.addValue("imei2", imei2);
//
////            if (deviceVehicleDeactivationDate == null) {
////                deviceVehicleDeactivationDate = new Date();
////            }
//
////            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
////                qry += " AND date_time BETWEEN :createdOn AND :deactivationDate ";
////                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
////                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
////            }
//
//            Date currentDateMinus = new Date(System.currentTimeMillis() - 300 * 1000); //5 minute in millisecond 60*5
//            Date currentDate=new Date();
//            qry += " and date_time BETWEEN :currentDateMinus AND :currentDate  order by date_time desc LIMIT 1";
//            sqlParam.addValue("currentDateMinus", currentDateMinus);
//            sqlParam.addValue("currentDate", currentDate);
//
//            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        } else {
//            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
//                    "\tFROM rdvts_oltp.vtu_location where is_active=true   ";
//
//            qry += " and imei in (:imei1) and gps_fix::numeric =1 ";
//            sqlParam.addValue("imei1", imei1);
//
////            if (deviceVehicleDeactivationDate == null) {
////                deviceVehicleDeactivationDate = new Date();
////            }
////
////            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
////                qry += " AND date_time BETWEEN :createdOn AND :deactivationDate ";
////                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
////                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
////            }
////            qry += " and date(date_time)=date(now()) order by date_time desc LIMIT 1";
//            Date currentDateMinus = new Date(System.currentTimeMillis() - 300 * 1000); //15 minute in millisecond 60*15
//            Date currentDate=new Date();
//            qry += " and date_time BETWEEN :currentDateMinus AND :currentDate  order by date_time desc LIMIT 1";
//            sqlParam.addValue("currentDateMinus", currentDateMinus);
//            sqlParam.addValue("currentDate", currentDate);
//
//
//
//            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
//            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        }

    }


    public Double getDistance(Long imei1, Long imei2, Date startDate, Date endDate, Date deviceVehicleCreatedOn, Date deviceVehicleDeactivationDate) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        if (imei1 == null) {
            String qry = "with c as " +
                    "(select st_setsrid(st_makepoint(longitude::numeric,latitude::numeric),4326) as geomPoint " +
                    "from rdvts_oltp.vtu_location where  is_active=true ";
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate == null) {
                qry += "  OR date_time BETWEEN :createdOn AND now() ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += "  OR date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            qry += "and imei=:imei2 and gps_fix::numeric =1   and longitude::numeric between 81 and 88  " +
                    "  and latitude::numeric between 17 and 23   order by date_time desc )";
            sqlParam.addValue("imei2", imei2);
            qry += "select round(st_length(st_transform(st_makeline(c.geomPoint),26986))::numeric,3) from c";
            return namedJdbc.queryForObject(qry, sqlParam, Double.class);


        } else {
            String qry = "with c as " +
                    "(select st_setsrid(st_makepoint(longitude::numeric,latitude::numeric),4326) as geomPoint " +
                    "from rdvts_oltp.vtu_location where  is_active=true  ";

            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate == null) {
                qry += "  AND date_time BETWEEN :createdOn AND now() ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += "  AND date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }


            qry += "and imei=:imei1 and gps_fix::numeric =1 and longitude::numeric between 81 and 88 " +
                    " and latitude::numeric between 17 and 23 order by date_time desc ) ";
            sqlParam.addValue("imei1", imei1);
            qry += "select round(st_length(st_transform(st_makeline(c.geomPoint),26986))::numeric,3) from c";
            if (namedJdbc.queryForObject(qry, sqlParam, Double.class) == null) {
                return 0.0;
            } else {
                return namedJdbc.queryForObject(qry, sqlParam, Double.class);
            }

        }


    }

    public Double getTodayDistance(Long imei1, Long imei2, Date startDate, Date endDate, Date deviceVehicleCreatedOn, Date deviceVehicleDeactivationDate) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        if (imei1 == null) {
            String qry = "with c as " +
                    "(select st_setsrid(st_makepoint(longitude::numeric,latitude::numeric),4326) as geomPoint " +
                    "from rdvts_oltp.vtu_location where  is_active=true ";
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate == null) {
                qry += "  OR date_time BETWEEN :createdOn AND now() ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += "  OR date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }

            qry += "and imei=:imei2 and date(date_time)=date(now()) and gps_fix::numeric=1  /*and longitude::numeric between 81 and 88 and latitude::numeric between 17 and 23*/  " +
                    "order by date_time desc )";
            sqlParam.addValue("imei2", imei2);
            qry += "select round(st_length(st_transform(st_makeline(c.geomPoint),26986))::numeric,3) from c";
            return namedJdbc.queryForObject(qry, sqlParam, Double.class);

        } else {
            String qry = "with c as " +
                    "(select st_setsrid(st_makepoint(longitude::numeric,latitude::numeric),4326) as geomPoint " +
                    "from rdvts_oltp.vtu_location where  is_active=true and gps_fix::numeric=1 ";

            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate == null) {
                qry += "  AND date_time BETWEEN :createdOn AND now() ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += "  AND date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            qry += "and imei=:imei1 and date(date_time)=date(now()) order by date_time desc ) ";
            sqlParam.addValue("imei1", imei1);
            qry += "select round(st_length(st_transform(st_makeline(c.geomPoint),26986))::numeric,3) from c";
            Double result = namedJdbc.queryForObject(qry, sqlParam, Double.class);
            if (result != null) {
                return result;
            } else {
                return 0.0;
            }


        }


    }

    public List<VtuLocationDto> getAvgSpeedToday(Long imei1, Long imei2, Date startDate, Date endDate, Date deviceVehicleCreatedOn, Date deviceVehicleDeactivationDate) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        if (imei1 == null) {
            String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on " +
                    "    FROM rdvts_oltp.vtu_location where is_active=true  ";
            qry += " and imei =:imei2 and gps_fix::numeric =1  ";
            sqlParam.addValue("imei2", imei2);

            if (deviceVehicleDeactivationDate == null) {
                deviceVehicleDeactivationDate = new Date();
            }

            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += " AND  date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }

            if (startDate != null && endDate != null) {
                qry += " and date_time BETWEEN :startDate AND :endDate ";
                sqlParam.addValue("startDate", startDate);
                sqlParam.addValue("endDate", endDate);
            }


            qry += " and date(date_time)=date(now()) order by date_time ASC";


            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else {
            String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on " +
                    " FROM rdvts_oltp.vtu_location where is_active=true ";
            qry += " and imei =:imei1 and gps_fix::numeric =1 ";
            sqlParam.addValue("imei1", imei1);
            if (deviceVehicleDeactivationDate == null) {
                deviceVehicleDeactivationDate = new Date();
            }

            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += " AND  date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }

            if (startDate != null && endDate != null) {
                qry += " and date_time BETWEEN :startDate AND :endDate ";
                sqlParam.addValue("startDate", startDate);
                sqlParam.addValue("endDate", endDate);
            }


            qry += " and date(date_time)=date(now()) order by date_time ASC";


            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        }


    }

    public Double getspeed(Long imei1, Long imei2, Date startDate, Date endDate, Date deviceVehicleCreatedOn, Date deviceVehicleDeactivationDate) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        if (imei1 == null) {
            String qry = "with c as " +
                    "(select st_setsrid(st_makepoint(longitude::numeric,latitude::numeric),4326) as geomPoint " +
                    "from rdvts_oltp.vtu_location where  is_active=true ";
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate == null) {
                qry += "  AND date_time BETWEEN :createdOn AND now() ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += "  AND date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            qry += "and imei=:imei2 and date(date_time)=date(now()) and gps_fix::numeric =1 order by date_time desc )";
            sqlParam.addValue("imei2", imei2);

            return namedJdbc.queryForObject(qry, sqlParam, Double.class);


        } else {
            String qry = "with c as " +
                    "(select st_setsrid(st_makepoint(longitude::numeric,latitude::numeric),4326) as geomPoint " +
                    "from rdvts_oltp.vtu_location where  is_active=true  ";

            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate == null) {
                qry += "  AND date_time BETWEEN :createdOn AND now() ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += "  AND date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }


            qry += "and imei=:imei1 and date(date_time)=date(now()) and gps_fix::numeric =1 order by date_time desc ) ";
            sqlParam.addValue("imei1", imei1);
            qry += "select round(st_length(st_transform(st_makeline(c.geomPoint),26986))::numeric,3) from c";
            return namedJdbc.queryForObject(qry, sqlParam, Double.class);
        }


    }

    public Integer getActiveVehicle(Long imei1, Long imei2, Date startDate, Date endDate, Date deviceVehicleCreatedOn, Date deviceVehicleDeactivationDate) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        if (imei1 == null) {
            String qry = "select count(*) from rdvts_oltp.vtu_location where   is_active=true and gps_fix::numeric=1";
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate == null) {
                qry += "  AND date_time BETWEEN :createdOn AND now() ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += "  AND date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            qry += "  and imei=:imei2 and date(date_time)=date(now()) )";
            sqlParam.addValue("imei2", imei2);

            if (namedJdbc.queryForObject(qry, sqlParam, Integer.class) > 0) {
                return 1;
            } else {
                return 0;
            }


        } else {
            String qry = "select count(*) from rdvts_oltp.vtu_location where   is_active=true and gps_fix::numeric=1  ";

            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate == null) {
                qry += "  AND date_time BETWEEN :createdOn AND now() ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
                qry += "  AND date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }


            qry += "and imei=:imei1 and date(date_time)=date(now())  ";
            sqlParam.addValue("imei1", imei1);

            if (namedJdbc.queryForObject(qry, sqlParam, Integer.class) > 0) {
                return 1;
            } else {
                return 0;
            }

        }


    }


    public VtuLocationDto getLastLocationByImei(Long imeiNo) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        VtuLocationDto locationDto = null;

        String qry = "select dm.id,b.* from rdvts_oltp.vehicle_device_mapping as vdm " +
                "left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id " +
                "left join (select distinct imei,max(id) over(partition by imei) as vtuid from " +
                "rdvts_oltp.vtu_location as vtu  where imei=:imeiNo and gps_fix::numeric=1 order by imei ) as a on dm.imei_no_1=a.imei\n" +
                "left join rdvts_oltp.vtu_location as b on a.vtuid=b.id " +
                "where vdm.is_active=true and b.id is not null";
        sqlParam.addValue("imeiNo",imeiNo);

        try {
            locationDto = namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }


        return locationDto;
    }


    public List<VtuLocationDto> getLocationrecordList(Long imeiNo1, Long imeiNo2, Date startDate, Date endDate, Date createdOn, Date deactivationDate, Integer recordLimit) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT id,  imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on " +
                " FROM rdvts_oltp.vtu_location where is_active=true ";
        qry += " and imei =:imei1 and gps_fix::numeric =1 ";

        Date currentDateMinus = new Date(System.currentTimeMillis() - 300 * 1000); //5 minute in millisecond 60*5
//        DateTime dateTime = new DateTime();
//        dateTime = dateTime.minusMinutes(5);
//        Date modifiedDate = dateTime.toDate();
        Date currentDate = new Date();

        if (deactivationDate == null) {
            deactivationDate = new Date();
        }

        if (createdOn != null && deactivationDate != null) {
            qry += " AND  date_time BETWEEN :createdOn AND :deactivationDate ";

        }

        qry += "   AND date(date_time)=date(now()) AND  date_time BETWEEN :currentDateMinus AND :currentDate  order by date_time ASC  limit :recordLimit ";
        sqlParam.addValue("imei1", imeiNo1);
        sqlParam.addValue("createdOn", createdOn);
        sqlParam.addValue("deactivationDate", deactivationDate);
        sqlParam.addValue("recordLimit", recordLimit);
        sqlParam.addValue("currentDateMinus", currentDateMinus);
        sqlParam.addValue("currentDate", currentDate);


        //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
    }

    public List<VtuLocationDto> getLastLocationByDeviceId(List<Integer> IdList, Integer checkArea) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select b.*,vdm.vehicle_id as vehicleId,dm.id as deviceId from rdvts_oltp.vehicle_device_mapping as vdm" +
                " left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id" +
                " left join (select distinct imei,max(id) over(partition by imei) as vtuid from" +
                " rdvts_oltp.vtu_location as vtu where date(date_time)=date(now()) and  gps_fix::numeric =1 ) as a on dm.imei_no_1=a.imei" +
                " left join rdvts_oltp.vtu_location as b on a.vtuid=b.id" +
                " where vdm.is_active=true and dm.is_active=true and b.id is not null and b.gps_fix::numeric =1  ";

        if (IdList.get(0) > 1) {
            if (checkArea !=null){
            if (checkArea == 1) {
                qry += " and dm.id in (SELECT device_id FROM rdvts_oltp.device_area_mapping where dist_id IN(:IdList))  ";
                sqlParam.addValue("IdList", IdList);
            }
            if (checkArea == 2) {
                qry += " and dm.id in (SELECT device_id FROM rdvts_oltp.device_area_mapping where block_id IN(:IdList)) ";
                sqlParam.addValue("IdList", IdList);
            }
            if (checkArea == 3) {
                qry += " and dm.id in (SELECT device_id FROM rdvts_oltp.device_area_mapping where division_id IN(:IdList)) ";
                sqlParam.addValue("IdList", IdList);
            }
            if (checkArea == 4) {
                qry += " and dm.id in (SELECT device_id  FROM rdvts_oltp.device_area_mapping where division_id IN(select id from rdvts_oltp.division_m where circle_id in(:IdList))) ";
                sqlParam.addValue("IdList", IdList);
            }
            }
            else {
                qry += " and dm.id=:IdList";
                sqlParam.addValue("IdList", IdList);
            }
        }
        qry += " order by b.date_time desc";


        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));

    }

    public List<VtuLocationDto> getLocationRecordListWithGeofence(Long imeiNo1, Long imeiNo2, Date createdOn, Date deactivationDate, Integer id) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        if (imeiNo1 == null) {

            String qry = "with c as" +
                    "(select *,st_setsrid(st_makepoint(longitude::numeric,latitude::numeric),4326) as geomPoint   " +
                    "from rdvts_oltp.vtu_location where " +
                    " imei=:imei2 and gps_fix::numeric=1 and date(date_time)='2022-11-28' order by date_time ) " +
                    " select c.*,st_asgeojson(c.geomPoint) from c ,rdvts_oltp.geo_construction_m as rd where st_intersects(c.geomPoint,st_buffer(rd.geom::geography,100)::geometry) and rd.id=:roadId ";

            sqlParam.addValue("imei2", imeiNo2);
            sqlParam.addValue("roadId", id);

//            if (deviceVehicleDeactivationDate == null) {
//                deviceVehicleDeactivationDate = new Date();
//            }
//
//            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
//                qry += " AND  date_time BETWEEN :createdOn AND :deactivationDate ";
//                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
//                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
//            }



            qry += " order by date_time ASC ";


            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else {
            String qry = "with c as" +
                    "(select *,st_setsrid(st_makepoint(longitude::numeric,latitude::numeric),4326) as geomPoint    " +
                    "from rdvts_oltp.vtu_location where " +
                    " imei=:imei1 and gps_fix::numeric=1 and date(date_time)='2022-11-29' order by date_time desc) " +
                    " select c.*,st_asgeojson(c.geomPoint) from c ,rdvts_oltp.geo_construction_m as rd where st_intersects(c.geomPoint,st_buffer(rd.geom::geography,100)::geometry) and rd.id=:roadId ";

            sqlParam.addValue("imei1", imeiNo1);
            sqlParam.addValue("roadId", id);

//            if (deviceVehicleDeactivationDate == null) {
//                deviceVehicleDeactivationDate = new Date();
//            }

//            if (deviceVehicleCreatedOn != null && deviceVehicleDeactivationDate != null) {
//                qry += " AND  date_time BETWEEN :createdOn AND :deactivationDate ";
//                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
//                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
//            }

//            if (startDate != null && endDate != null) {
//                qry += " and date_time BETWEEN :startDate AND :endDate ";
//                sqlParam.addValue("startDate", startDate);
//                sqlParam.addValue("endDate", endDate);
//            }


            qry += " order by date_time ASC ";



            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));

        }

    }


    public AlertDegreeDistanceDto getRotationDetails(String longitude, String latitude, String longitude1, String latitude1) {
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "SELECT degrees(ST_Azimuth(ST_Point("+longitude+", "+latitude+"),ST_Point("+longitude1+", "+latitude1+"))), " +
                " st_distance(st_transform(st_setsrid(ST_Point("+longitude+", "+latitude+"),4326),26986), " +
                "     st_transform(st_setsrid(ST_Point("+longitude1+", "+latitude1+"),4326),26986)) ";

        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(AlertDegreeDistanceDto.class));

    }

//    public List<VtuLocationDto> getLastLocationRecordListByDevice(List<DeviceAreaMappingDto> deviceAreaMappingDtoList) {
//        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        String qry = "select b.*,vdm.vehicle_id as vehicleId from rdvts_oltp.vehicle_device_mapping as vdm"+
//                " left join rdvts_oltp.device_m as dm on dm.id=vdm.device_id"+
//                " left join (select distinct imei,max(id) over(partition by imei) as vtuid from"+
//                " rdvts_oltp.vtu_location as vtu where date(date_time)=date(now()) and  gps_fix::numeric =1 ) as a on dm.imei_no_1=a.imei"+
//                " left join rdvts_oltp.vtu_location as b on a.vtuid=b.id"+
//                " where vdm.is_active=true and b.id is not null and b.gps_fix::numeric =1 and dm.id in (:deviceIdList) order by b.date_time desc ";
//
//        sqlParam.addValue("deviceIdList", deviceIdList);
//
//        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//    }
}