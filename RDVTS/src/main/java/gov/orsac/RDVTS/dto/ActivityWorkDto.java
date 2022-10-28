package gov.orsac.RDVTS.dto;

import gov.orsac.RDVTS.entities.VehicleActivityMappingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.AliasFor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityWorkDto {

    private Integer activityId;

    private Integer workId;

    private Integer userId;

    List<VehicleActivityMappingEntity> vehicle;

    private Integer vehicleTypeId;
}
