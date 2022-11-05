package gov.orsac.RDVTS.dto;

import gov.orsac.RDVTS.entities.ActivityEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleActivityMappingDto {

    private Integer id;
    private Integer vehicleId;
    private Integer activityId;
    private Date startTime;
    private Date endTime;
    private Date startDate;
    private Date endDate;
    private Boolean isActive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Date deactivationDate;
    private Integer gActivityId;

    private String vehicleNo;
    private Integer vehicleTypeId;
    private String model;
    private Double speedLimit;
    private String chassisNo;
    private String engineNo;

    private String start;
    private String end;

    List<ActivityEntity> activityEntityList;
}
