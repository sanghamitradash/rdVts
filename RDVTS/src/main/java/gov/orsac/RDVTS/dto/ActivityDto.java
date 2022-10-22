package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {

    private Integer id;
    private String activityName;
    private Double activityQuantity;
    private Date activityStartDate;
    private Date activityCompletionDate;
    private Date actualActivityStartDate;
    private Date actualActivityCompletionDate;
    private Double executedQuantity;
    private Integer workId;
    private Boolean isActive = true;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Integer activityStatus;
    private Integer slNo;
}
