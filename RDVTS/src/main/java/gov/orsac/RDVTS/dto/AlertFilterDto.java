package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertFilterDto {

    private String startDate;
    private String endDate;
    private Integer alertTypeId;

    private Integer draw;
    private Integer offSet;
    private Integer limit;

    private Integer userId;
}
