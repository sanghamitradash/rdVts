package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAnalysisResponseDto {
    private Integer id;
    private String activityName;
    private Double reqQuantity;
    private Double executedQuantity;
}
