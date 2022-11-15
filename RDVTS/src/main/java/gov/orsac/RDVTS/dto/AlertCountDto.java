package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertCountDto {

    private Integer totalAlertToday;
    private Integer totalAlertWork;
    private List<Integer> vehicleId;
    private List<Integer> workId;

}
