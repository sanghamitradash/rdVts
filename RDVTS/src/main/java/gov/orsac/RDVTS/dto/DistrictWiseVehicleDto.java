package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictWiseVehicleDto {
    private Integer districtId;
    private String districtName;
    //private Integer vehicleCount;
    private Integer count;
    private Integer active;
    private Integer InActive;


}
