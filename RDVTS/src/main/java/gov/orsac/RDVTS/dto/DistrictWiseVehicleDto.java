package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.ObjIntConsumer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictWiseVehicleDto {
    private Integer districtId;
    private String districtName;
    private Integer vehicleCount;

}
