package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityInfoDto {

    private Integer id;
    private String activityName;
    private Double activityQuantity;
    private String  activityCompletionDate;
    private String actualActivityStartDate;
    private String actualActivityCompletionDate;
    private Double executedQuantity;
    private Integer workId;
    private Boolean isActive = true;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Integer activityStatus;
    private Integer slNo;
    private String roadName;
    private String statusName;
    private String workName;
    private String startDate;
    private  String endDate;
    private Integer gActivityId;
    private Integer statusId;
}
