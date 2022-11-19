package gov.orsac.RDVTS.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityWorkMappingDto {

    private Integer id;

    private Integer activityId;

    private Integer workId;

    private Double activityQuantity;

    private Date activityStartDate;

    private Date activityCompletionDate;

    private Date actualActivityStartDate;

    private Date actualActivityCompletionDate;

    private Double executedQuantity;

    private Integer activityStatus;

    private Integer gActivityId;

    private String issueReason;

    private Integer resolvedStatus;

    private Date resolvedDate;

    private Integer resolvedBy;

    private String issueImage;

    private Boolean isActive = true;

    private Integer createdBy;

    private Date createdOn;

    private Integer updatedBy;

    private Date updatedOn;

    private IssueDto issue;

}
