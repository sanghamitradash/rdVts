package gov.orsac.RDVTS.repository;

import gov.orsac.RDVTS.entities.VehicleDeviceMappingEntity;

import java.text.ParseException;

public interface VehicleDeviceRepository {
    Integer deactivateVehicleDevice(VehicleDeviceMappingEntity vehicleDeviceMapping) throws ParseException;
}
