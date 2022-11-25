package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertFilterDto {

    private Integer roadId;
    private Integer vehicleId;
    private Integer deviceId;
    private Integer activityId;
    private Integer workId;
    private Integer circleId;
    private Integer distId;
    private Integer blockId;
    private Integer divisionId;

    private String startDate;
    private String endDate;
    private Integer alertTypeId;

    private Integer draw;
    private Integer offSet;
    private Integer limit;

    private Integer userId;
    private Integer alertId;
}
