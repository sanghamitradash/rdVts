package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Date dateTime;
    private Double latitude;
    private Double longitude;
    private Integer speed;
    private Double distanceTravelledToday;
    private Double distanceTravelledTotal;
    private Double avgDistanceTravelled;
    private Double avgSpeed;
}
