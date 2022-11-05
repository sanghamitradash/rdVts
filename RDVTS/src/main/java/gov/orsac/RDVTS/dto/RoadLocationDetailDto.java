package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadLocationDetailDto {

    private String lattitude;
    private String longitude;
    private String altitude;
    private String accuracy;
    private String speed;

}
