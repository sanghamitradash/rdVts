package gov.orsac.RDVTS.dto;

import gov.orsac.RDVTS.entities.VehicleActivityMappingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.AliasFor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityWorkDto {

    private Integer activityId;

    private Integer workId;

    private Integer userId;

    private Double activityQuantity;

    private Date activityStartDate;

    private Date activityCompletionDate;

    private Date actualActivityStartDate;

    private Date actualActivityCompletionDate;

    private Double executedQuantity;

    private Integer activityStatus;

    List<VehicleActivityMappingEntity> vehicle;

    private Integer vehicleTypeId;
    private String vehicleNo;
}
