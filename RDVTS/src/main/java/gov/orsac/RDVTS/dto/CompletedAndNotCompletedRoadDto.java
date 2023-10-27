package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedAndNotCompletedRoadDto {

    private Integer totalRoad;
    private Integer totalCompletedRoad;
    private Integer totalInCompletedRoad;

}
