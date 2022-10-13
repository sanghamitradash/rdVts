package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivisionDto {

    private Integer id;
    private String divisionName;
    private Integer distId;
    private String acrnym;
    private Boolean isActive;
    private Integer divisionId;

    private String distName;



}
