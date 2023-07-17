package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityAnalysisDto {

    private Integer id;
    private Integer vehicleId;
    private Integer geoMappingId;
    private Double reqQuantity;
    private Double executedQuantity;
    private Date startTime;
    private Date endTime;
    private String activityName;
}
