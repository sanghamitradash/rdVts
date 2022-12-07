package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoConstructionDto {

    private Integer id;

    private Integer userId;

    private Integer packageId;

    private String packageName;
}
