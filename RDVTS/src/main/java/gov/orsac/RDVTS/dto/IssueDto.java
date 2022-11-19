package gov.orsac.RDVTS.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDto {

    private Integer id;
    private Integer activityWorkId;
    private Integer issueReason;
    private Integer resolvedStatus;
    private Date resolvedDate;
    private Integer resolvedBy;
    private String issueImage;
    private Boolean isActive = true;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
}
