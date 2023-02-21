package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardCronDto {

    private Integer id;
    private Integer active;
    private Integer inActive;
    private Integer areaTypeId;
    private Integer areaId;
}
