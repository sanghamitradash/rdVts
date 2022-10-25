package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveAndInactiveVehicleDto {
    private Integer activeCount;
    private Integer inActiveCount;
    private Double activePercentage;
    private Double inActivePercentage;
    private Integer totalVehicle;
}
