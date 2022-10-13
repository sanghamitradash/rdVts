package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictBoundaryDto {

    private Integer gid;
    private String districtName;
    private String districtCode;
    private String stateName;
    private String stateCode;
    private Integer distId;
    private Geometry geom;
    private Integer stateId;
    private String stAsgeojson;
}
