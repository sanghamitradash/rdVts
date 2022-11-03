package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivisionWiseVehicleDto {

    private Integer divId;
    private String divName;
    //private Integer vehicleCount;
    private String geom;
    private Integer count;
    private Integer active;
    private Integer InActive;
}
