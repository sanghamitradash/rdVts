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
    private String roadName;
    private String status;
    private String workName;
    private String startDate;
    private  String endDate;
    List<VehicleActivityMappingEntity> vehicleActivity;
}
