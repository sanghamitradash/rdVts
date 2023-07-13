package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDto {

    private Integer id;
    private Integer userId;
    private Integer geoWorkId;
    private Integer activityId;
    private String geoWorkName;
    private Boolean isActive=true;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;

    private Date awardDate;
    private Date completionDate;
    private Date pmisFinalizeDate;
    private Integer workStatus;
    private Integer approvalStatus;
    private Integer approvedBy;

    private String approvalStatusName;
    private String workStatusName;

    private Integer piuId;
    private String piuName;
    private Integer packageId;
    private String packageName;
    private String contractorId;

    private Integer distId;
    private Integer divisionId;
    private Integer circleId;

    private Integer offSet;
    private Integer limit;
    private Integer draw;
    private Integer slNo;


    private Integer gWorkId;
    private String activityStatus;
    private String activityName;
    private String roadName;

}
