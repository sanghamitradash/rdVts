package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictWiseVehicleDto {
    private Integer districtId;
    private String districtName;
    //private Integer vehicleCount;
    private String geom;
    private Integer count;
    private Integer active;
    private Integer InActive;
    private String processTime;


}
