package gov.orsac.RDVTS.dto;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private Date dateTime;
    private Double latitude;
    private Double longitude;
//    private Double speed;
    private Double distanceTravelledToday;
    private Double distanceTravelledTotal;
    private Double avgDistanceTravelled;
    private Double avgSpeedToday;
    private Double avgSpeedWork;
    private Integer totalVehicleCount;
    private Integer totalVehicleActive;
    private Integer totalInactiveVehicle;
    private Double percentageOfActiveVehicle;
    private List<Integer> totalAlertToday;
    private List<Integer> totalAlertWork;

}
