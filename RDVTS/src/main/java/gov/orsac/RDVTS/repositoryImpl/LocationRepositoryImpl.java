package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.dto.VehicleWorkMappingDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class LocationRepositoryImpl {

    @Value("${dbschema}")
    private String DBSCHEMA;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;


    public VtuLocationDto getLatestRecordByImeiNumber(Long imei2,Long imei1){
        
        
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
                    "\tFROM rdvts_oltp.vtu_location where imei = :imei1 and is_active=true or imei = :imei2 and is_active=true order by date_time desc  limit 1";

            sqlParam.addValue("imei1", imei1);
            sqlParam.addValue("imei2", imei2);
            return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
//        }







    }


    public List<VtuLocationDto> getLocationRecordByDateTime(List<Long> imei2,List<Long> imei1, Date startTime, Date endTime){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        List<Long> imei1=new ArrayList<>();
//        List<Long> imei2=new ArrayList<>();
//        for(DeviceDto d:device){
//            imei1.add(d.getImeiNo1());
//            imei2.add(d.getImeiNo2());
//        }



        if (imei1==null){
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where  imei IN(:imei2) and is_active=true and date_time BETWEEN :startTime AND :endTime order by date_time desc  ";

            // sqlParam.addValue("imei1", device.get(0).getImeiNo1());
            sqlParam.addValue("imei2", imei2);
            sqlParam.addValue("startTime", startTime);
            sqlParam.addValue("endTime", endTime);

            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else if (imei2==null) {
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where imei IN(:imei1) and is_active=true and date_time BETWEEN :startTime AND :endTime  order by date_time desc";

            sqlParam.addValue("imei1",imei1);
            sqlParam.addValue("startTime", startTime);
            sqlParam.addValue("endTime", endTime);

            //sqlParam.addValue("imei2", device.get(0).getImeiNo2());
            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        }
        else {
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where imei IN(:imei1) and is_active=true and date_time BETWEEN :startTime AND :endTime  or imei IN(:imei2) and is_active=true and date_time BETWEEN :startTime AND :endTime  order by date_time desc";

            sqlParam.addValue("imei1", imei1);
            sqlParam.addValue("imei2",imei2);
            sqlParam.addValue("startTime", startTime);
            sqlParam.addValue("endTime", endTime);

            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        }


    }

    public List<VtuLocationDto> getLocationRecordByRoadData(List<Long> imei1,List<Long> imei2,List<VehicleWorkMappingDto> vehicleByWork){
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
                "\tFROM rdvts_oltp.vtu_location where imei IN (:imei1) and is_active=true and date_time BETWEEN :startTime AND :endTime or imei IN (:imei2) and is_active=true order by date_time desc  limit 1";

        sqlParam.addValue("imei1", imei1);
        sqlParam.addValue("imei2", imei2);
        return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
    }

    public List<VtuLocationDto> getLocationrecordList(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        if (imei1==null){
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where  is_active=true ";

//            if (startDate !=null && endDate !=null){
//                qry+="and date_time BETWEEN :startDate AND :endDate ";
//                sqlParam.addValue("startDate", startDate);
//                sqlParam.addValue("endDate", endDate);
//            }
            if (deviceVehicleDeactivationDate==null){
                deviceVehicleDeactivationDate=new Date();
            }

            if (deviceVehicleCreatedOn!=null && deviceVehicleDeactivationDate!=null){
                qry+=" OR date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            qry+=" and imei =:imei2  order by date_time desc ";

            //qry+=" and imei =:imei2  order by date_time desc ";
            sqlParam.addValue("imei2", imei2);



            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else  {
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where is_active=true ";

            if (startDate !=null && endDate !=null){
                qry+=" OR date_time BETWEEN :startDate AND :endDate ";
                sqlParam.addValue("startDate", startDate);
                sqlParam.addValue("endDate", endDate);
            }

            if (deviceVehicleDeactivationDate==null){
                deviceVehicleDeactivationDate=new Date();
            }

            if (deviceVehicleCreatedOn!=null && deviceVehicleDeactivationDate!=null){
                qry+=" OR  date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            qry+=" and imei =:imei1  order by date_time desc";
            sqlParam.addValue("imei1", imei1);
         

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

    public List<VtuLocationDto> getLastLocationrecordList(Long imei1,Long imei2,Date startDate,Date endDate,Date deviceVehicleCreatedOn,Date deviceVehicleDeactivationDate){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        if (imei1==null){
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where  is_active=true ";

            if (startDate !=null && endDate !=null){
                qry+=" OR date_time BETWEEN :startDate AND :endDate ";
                sqlParam.addValue("startDate", startDate);
                sqlParam.addValue("endDate", endDate);
            }
            if (deviceVehicleDeactivationDate==null){
                deviceVehicleDeactivationDate=new Date();
            }

            if (deviceVehicleCreatedOn!=null && deviceVehicleDeactivationDate!=null){
                qry+=" OR date_time BETWEEN :createdOn AND :deactivationDate ";
                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
            }
            qry+=" and imei =:imei2  order by date_time desc LIMIT 1";

            //qry+=" and imei =:imei2  order by date_time desc ";
            sqlParam.addValue("imei2", imei2);



            return namedJdbc.query(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
        } else  {
            String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                    "\tFROM rdvts_oltp.vtu_location where is_active=true ";

            if (startDate !=null && endDate !=null){
                qry+=" OR date_time BETWEEN :startDate AND :endDate ";
                sqlParam.addValue("startDate", startDate);
                sqlParam.addValue("endDate", endDate);
            }

            if (deviceVehicleDeactivationDate==null){
                deviceVehicleDeactivationDate=new Date();
            }

//            if (deviceVehicleCreatedOn!=null && deviceVehicleDeactivationDate!=null){
//                qry+=" OR date_time BETWEEN :createdOn AND :deactivationDate ";
//                sqlParam.addValue("createdOn", deviceVehicleCreatedOn);
//                sqlParam.addValue("deactivationDate", deviceVehicleDeactivationDate);
//            }
            qry+=" and imei =:imei1  order by date_time desc LIMIT 1";
            sqlParam.addValue("imei1", imei1);


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


    public  Double getDistance(Map<String,Object> coordinates){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry = "select st_length(st_transform(st_setsrid(ST_GeomFromGeoJSON('"+coordinates.toString()+"'),4326),26986))";
        //sqlParam.addValue("coordinates", );
        return   namedJdbc.queryForObject(qry, sqlParam,Double.class);
    }


}
