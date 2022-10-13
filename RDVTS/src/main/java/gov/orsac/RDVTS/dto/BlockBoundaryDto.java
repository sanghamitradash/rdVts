package gov.orsac.RDVTS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.NamedEntityGraph;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockBoundaryDto {

    private Integer gid;
    private String blockName;
    private String blockCode;
    private String districtName;
    private String districtCode;
    private String stateName;
    private String stateCode;
    private Integer distId;
    private Integer blockId;
    private Geometry geom;
    private String stAsgeojson;
}
