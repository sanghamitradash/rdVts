package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityListDto {
    private Integer workId;
    private Integer roadId;
    private Integer activityId;
    private String startDate;
    private String endDate;
    private String actualStartDate;
    private String actualEndDate;
    private Integer activityStatus;
    private Integer offSet;
    private Integer limit;
    private Integer draw;
    private Integer userId;
}
