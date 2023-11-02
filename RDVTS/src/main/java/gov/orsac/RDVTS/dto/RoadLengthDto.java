package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadLengthDto {

    private Integer packageId;

    private String packageNo;

    private Integer distId;

    private Double completedRoadLength;

    private Double sanctionedLength;

}
