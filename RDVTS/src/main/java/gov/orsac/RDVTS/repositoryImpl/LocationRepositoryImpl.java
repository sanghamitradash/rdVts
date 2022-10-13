package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.DeviceDto;
import gov.orsac.RDVTS.dto.UserInfoDto;
import gov.orsac.RDVTS.dto.VtuLocationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepositoryImpl {

    @Value("${dbschema}")
    private String DBSCHEMA;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;


    public VtuLocationDto getLatestRecordByImeiNumber(List<DeviceDto> device){
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();

        String qry = "SELECT id, firmware_version, packet_type, alert_id, packet_status, imei, vehicle_reg, gps_fix, date_time, latitude, latitude_dir, longitude, longitude_dir, speed, heading, no_of_satellites, altitude, pdop, hdop, network_operator_name, ignition, main_power_status, main_input_voltage, internal_battery_voltage, emergency_status, tamper_alert, gsm_signal_strength, mcc, mnc, lac, cell_id, lac1, cell_id1, cell_id_sig1, lac2, cell_id2, cell_id_sig2, lac3, cell_id3, cell_id_sig3, lac4, cell_id4, cell_id_sig4, digital_input1, digital_input2, digital_input3, digital_input4, digital_output_1, digital_output_2, frame_number, checksum, odo_meter, geofence_id, is_active, created_by, created_on, updated_by, updated_on\n" +
                "\tFROM rdvts_oltp.vtu_location where imei=:imei1 and is_active=true or imei=:imei2 and is_active=true order by desc date_time limit 1";

        sqlParam.addValue("imei1", device.get(0).getImeiNo1());
        sqlParam.addValue("imei2", device.get(0).getImeiNo2());
        return namedJdbc.queryForObject(qry, sqlParam, new BeanPropertyRowMapper<>(VtuLocationDto.class));
    }
}
