package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleActivityDto {

    private Integer id;
    private Integer vehicleId;
    private Integer activityId;
    private String startTime;
    private String endTime;
    private Date startDate;
    private Date endDate;
    private Boolean isActive;
    private Integer createdBy;
    private Date createdOn;
    private Integer updatedBy;
    private Date updatedOn;
    private Date deactivationDate;
    private Integer gActivityId;
    private Integer geoMappingId;
    private Integer packageId;
}
