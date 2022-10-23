package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadFilterDto {

    private Integer id;

    private Integer userId;

    private String roadName;

    private Double roadLength;

    private Double roadLocation;

    private Integer offSet;
    private Integer limit;
    private Integer draw;

    List<Integer> workIds;
    List<Integer> contractIds;
    List<Integer> activityIds;
}
