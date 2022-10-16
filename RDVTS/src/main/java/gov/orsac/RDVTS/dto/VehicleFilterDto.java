package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleFilterDto {
    private Integer userId;
    private Integer vehicleTypeId;
    private Integer  deviceId;
    private Integer workId;
    private Integer offSet;
    private Integer limit;
    private Integer draw;
}