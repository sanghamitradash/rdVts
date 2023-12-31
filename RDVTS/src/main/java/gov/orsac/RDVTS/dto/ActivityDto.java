package gov.orsac.RDVTS.dto;

import gov.orsac.RDVTS.entities.VehicleActivityMappingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto {

    private Integer id;
    private Integer activityId;
    private String activityName;
    private Double activityQuantity;
    private Date activityStartDate;
    private Date activityCompletionDate;
    private Date actualActivityStartDate;
    private Date actualActivityCompletionDate;
    private Double executedQuantity;
    private Integer workId;
    private Integer packageId;
    private Boolean isActive = true;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Integer activityStatus;
    private String activityStatusName;
    private Integer slNo;
    private Integer roadId;
    private String roadName;
    private String status;
    private String workName;
    private String startDate;
    private  String endDate;
    private Integer gActivityId;

    private String issueReason;
    private Integer resolvedStatus;
    private String resolvedStatusName;
    private Date resolvedDate;
    private Integer resolvedBy;
    private String issueImage;
    private Integer geoMappingId;

    private List<VehicleMasterDto> vehicleList;


    List<VehicleActivityMappingEntity> vehicleActivity;

    private String activityStartDateStr;
    private String activityCompletionDateStr;
}
