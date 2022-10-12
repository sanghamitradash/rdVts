package gov.orsac.RDVTS.serviceImpl;

import gov.orsac.RDVTS.dto.VehicleMasterDto;
import gov.orsac.RDVTS.entities.RoleEntity;
import gov.orsac.RDVTS.entities.VehicleMaster;
import gov.orsac.RDVTS.exception.RecordNotFoundException;
import gov.orsac.RDVTS.repository.VehicleMasterSaveRepository;
import gov.orsac.RDVTS.repository.VehicleRepository;
import gov.orsac.RDVTS.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService {
       @Autowired
       private VehicleMasterSaveRepository vehicleMasterSaveRepository;
       @Autowired
       private VehicleRepository vehicleRepository;
       @Override
       public VehicleMaster saveVehicle(VehicleMaster vehicle) {
        return vehicleMasterSaveRepository.save(vehicle);
        }

       @Override
       public VehicleMasterDto getVehicleByVId(Integer vehicleId) {
       return vehicleRepository.getVehicleByVId(vehicleId);
       }

       @Override
       public VehicleMaster updateVehicle(int id, VehicleMaster vehicle) {
              VehicleMaster existingVehicle= vehicleMasterSaveRepository.findVehicleById(id);
              if (existingVehicle == null) {
                     throw new RecordNotFoundException("Role", "id", id);
              }
              existingVehicle.setVehicleNo(vehicle.getVehicleNo());
              existingVehicle.setVehicleTypeId(vehicle.getVehicleTypeId());
              existingVehicle.setModel(vehicle.getModel());
              existingVehicle.setSpeedLimit(vehicle.getSpeedLimit());
              existingVehicle.setChassisNo(vehicle.getChassisNo());
              existingVehicle.setEngineNo(vehicle.getEngineNo());
              existingVehicle.setActive(vehicle.isActive());
              existingVehicle.setUpdatedBy(vehicle.getUpdatedBy());

              return vehicleMasterSaveRepository.save(existingVehicle);
       }
}
