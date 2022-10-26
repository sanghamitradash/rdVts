package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedAndNotCompletedWorkDto {
    private Integer totalWork;
    private Integer totalCompletedWork;
    private Integer totalInCompletedWork;
    private Double completedPercentage;
    private Double inCompletedPercentage;


}
