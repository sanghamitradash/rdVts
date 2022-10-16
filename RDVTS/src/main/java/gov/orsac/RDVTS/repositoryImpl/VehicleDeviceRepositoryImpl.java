package gov.orsac.RDVTS.repositoryImpl;

import gov.orsac.RDVTS.dto.VehicleTypeDto;
import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;
import gov.orsac.RDVTS.repository.VehicleDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VehicleDeviceRepositoryImpl implements VehicleDeviceRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;
    @Override
    public Integer deactivateVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String currentDateTime = dateFormat.format(new Date());
        Date date = dateFormat.parse(currentDateTime);
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        String qry ="UPDATE rdvts_oltp.vehicle_device_mapping " +
                "SET is_active=false,deactivation_date=:deactivationDate  WHERE vehicle_id=:vehicleId or device_id=:deviceId ";
        sqlParam.addValue("vehicleId",vehicleDeviceMapping.getVehicleId());
        sqlParam.addValue("deviceId",vehicleDeviceMapping.getDeviceId());
        sqlParam.addValue("deactivationDate",date);


        return namedJdbc.update(qry, sqlParam);
    }
}
